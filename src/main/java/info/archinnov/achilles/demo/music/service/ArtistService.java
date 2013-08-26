package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.entity.user.ArtistIndex.ArtistIndexType.*;
import static org.apache.commons.collections.CollectionUtils.*;
import info.archinnov.achilles.demo.music.constants.MusicStyle;
import info.archinnov.achilles.demo.music.entity.user.Artist;
import info.archinnov.achilles.demo.music.entity.user.ArtistIndex;
import info.archinnov.achilles.demo.music.utils.LuceneUtils;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

public class ArtistService {

    private CQLEntityManager em;

    private static final Function<ArtistIndex, Artist> extractArtist = new Function<ArtistIndex, Artist>() {

        @Override
        public Artist apply(ArtistIndex index)
        {
            return index.getArtist();
        }
    };

    public Long createArtist(Artist artist)
    {
        Preconditions.checkNotNull(artist, "Artist should not be null");
        Preconditions.checkNotNull(artist.getFirstname(), "Artist firstname should not be null");
        Preconditions.checkNotNull(artist.getLastname(), "Artist lastname should not be null");
        Preconditions.checkArgument(isNotEmpty(artist.getStyles()), "Artist should have at least one music style");

        Long userId = RandomUtils.nextLong();

        artist.setId(userId);

        em.persist(artist);

        indexArtistName(artist);
        indexMusicStyles(artist);
        indexBands(artist);

        return userId;
    }

    public List<Artist> findArtistsByName(String name) {

        Preconditions.checkNotNull(name, "Artist name should not be null for search");
        String normalizedName = name.toLowerCase();

        List<ArtistIndex> foundArtists = em.sliceQuery(ArtistIndex.class)
                .partitionKey(NAME)
                .fromClusterings(normalizedName)
                .toClusterings(normalizedName + "z")
                .get(10);

        return new ArrayList<Artist>(FluentIterable.from(foundArtists).transform(extractArtist).toImmutableSet());
    }

    public List<Artist> findArtistByStyle(String style) {

        Preconditions.checkNotNull(style, "Artist style should not be null for search");
        String normalizedStyle = style.toUpperCase();

        List<ArtistIndex> foundArtists = em.sliceQuery(ArtistIndex.class)
                .partitionKey(STYLE)
                .getFirst(10, normalizedStyle);

        return FluentIterable.from(foundArtists).transform(extractArtist).toImmutableList();
    }

    public List<Artist> findArtistByBand(String band) {

        Preconditions.checkNotNull(band, "Artist band should not be null for search");
        String normalizedBand = band.toLowerCase();

        List<ArtistIndex> foundArtists = em.sliceQuery(ArtistIndex.class)
                .partitionKey(BAND)
                .fromClusterings(normalizedBand)
                .toClusterings(normalizedBand + "z")
                .get(10);

        return new ArrayList<Artist>(FluentIterable.from(foundArtists).transform(extractArtist).toImmutableSet());
    }

    private void indexArtistName(Artist artist) {
        em.persist(new ArtistIndex(NAME, artist.getFirstname().toLowerCase(), artist));
        em.persist(new ArtistIndex(NAME, artist.getLastname().toLowerCase(), artist));
    }

    private void indexMusicStyles(Artist artist) {
        Set<MusicStyle> styles = artist.getStyles();
        if (styles != null)
        {
            for (MusicStyle style : styles)
            {
                em.persist(new ArtistIndex(STYLE, style.name(), artist));
            }
        }
    }

    private void indexBands(Artist artist) {
        Set<String> bands = artist.getBands();
        if (CollectionUtils.isNotEmpty(bands))
        {
            for (String band : bands)
            {
                for (String bandToken : LuceneUtils.tokenizeString(band))
                {
                    em.persist(new ArtistIndex(BAND, bandToken.toLowerCase(), artist));
                }
            }
        }
    }

}
