package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.entity.song.SongIndex.SongIndexType.*;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import info.archinnov.achilles.demo.music.constants.MusicStyle;
import info.archinnov.achilles.demo.music.entity.song.Album;
import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.demo.music.entity.song.SongIndex;
import info.archinnov.achilles.demo.music.utils.LuceneUtils;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

public class SongService {

    private static final Function<SongIndex, Song> extractSong = new Function<SongIndex, Song>() {

        @Override
        public Song apply(SongIndex index)
        {
            return index.getSong();
        }
    };

    private CQLEntityManager em;

    public Album createAlbum(Album album) {

        Preconditions.checkNotNull(album, "Album should not be null");
        Preconditions.checkNotNull(album.getTitle(), "Album title should not be null");
        Preconditions.checkArgument(isNotEmpty(album.getArtistIds()), "Album should have at least one artist id");
        Preconditions.checkArgument(isNotEmpty(album.getArtistNames()), "Album should have at least one artist name");

        UUID albumId = UUIDGen.getTimeUUID();
        album.setId(albumId);

        for (Song song : album.getSongs())
        {
            song.setAlbumId(albumId);
            song.setId(UUIDGen.getTimeUUID());
        }

        em.persist(album);

        return album;
    }

    public UUID createSong(Song song)
    {
        Preconditions.checkNotNull(song, "Song should not be null");
        Preconditions.checkNotNull(song.getTitle(), "Song title should not be null");
        Preconditions.checkNotNull(song.getDurationInSecs(), "Song duration should not be null");
        Preconditions.checkArgument(song.getDurationInSecs() > 0, "Song duration should be positive");
        Preconditions.checkArgument(isNotEmpty(song.getAuthorIds()), "Song should have at least one author id");
        Preconditions.checkArgument(isNotEmpty(song.getAuthorNames()), "Song should have at least one author name");

        UUID songId = UUIDGen.getTimeUUID();

        song.setId(songId);
        em.persist(song);

        indexSongByTitle(song);
        indexSongByAuthor(song);
        indexSongByStyle(song);

        return songId;
    }

    public List<Song> findSongsByTitle(String title) {

        Preconditions.checkNotNull(title, "Song title for search should not be null");
        String normalizedTitle = title.toLowerCase();

        List<SongIndex> foundSongs = em.sliceQuery(SongIndex.class)
                .partitionKey(TITLE)
                .fromClusterings(normalizedTitle)
                .toClusterings(normalizedTitle + "z")
                .get(10);

        return new ArrayList<Song>(FluentIterable.from(foundSongs).transform(extractSong).toImmutableSet());

    }

    public List<Song> findSongsByArtist(String artistName) {
        Preconditions.checkNotNull(artistName, "Song artistName for search should not be null");
        String normalizedArtistName = artistName.toLowerCase();

        List<SongIndex> foundSongs = em.sliceQuery(SongIndex.class)
                .partitionKey(ARTIST)
                .fromClusterings(normalizedArtistName)
                .toClusterings(normalizedArtistName + "z")
                .get(10);

        return new ArrayList<Song>(FluentIterable.from(foundSongs).transform(extractSong).toImmutableSet());
    }

    public List<Song> findSongsByStyle(String style) {

        Preconditions.checkNotNull(style, "Song style for search should not be null");
        String normalizedStyle = style.toUpperCase();

        List<SongIndex> foundSongs = em.sliceQuery(SongIndex.class)
                .partitionKey(MUSIC_STYLE)
                .getFirst(10, normalizedStyle);

        return FluentIterable.from(foundSongs).transform(extractSong).toImmutableList();
    }

    private void indexSongByTitle(Song song) {
        for (String token : LuceneUtils.tokenizeString(song.getTitle()))
            em.persist(new SongIndex(TITLE, token, song));
    }

    private void indexSongByAuthor(Song song) {
        for (String authorName : song.getAuthorNames())
        {
            for (String normalizedName : LuceneUtils.tokenizeString(authorName))
                em.persist(new SongIndex(ARTIST, normalizedName, song));
        }
    }

    private void indexSongByStyle(Song song) {
        for (MusicStyle style : song.getStyles())
            em.persist(new SongIndex(MUSIC_STYLE, style.name(), song));
    }

}
