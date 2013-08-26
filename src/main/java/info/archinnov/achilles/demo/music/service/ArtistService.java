package info.archinnov.achilles.demo.music.service;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import info.archinnov.achilles.demo.music.entity.user.Artist;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.List;
import org.apache.commons.lang.math.RandomUtils;
import com.google.common.base.Preconditions;

public class ArtistService {

    private CQLEntityManager em;

    public Long createArtist(Artist artist)
    {
        Preconditions.checkNotNull(artist, "Artist should not be null");
        Preconditions.checkNotNull(artist.getFirstname(), "Artist firstname should not be null");
        Preconditions.checkNotNull(artist.getLastname(), "Artist lastname should not be null");
        Preconditions.checkArgument(isNotEmpty(artist.getStyles()), "Artist should have at least one music style");

        Long userId = RandomUtils.nextLong();

        artist.setId(userId);

        em.persist(artist);

        /*
         * TODO
         * 
         * Index artist by firstname & lastname (lower case)
         * Index artist by style
         * Index artist by band
         */

        return userId;
    }

    public List<Artist> findArtistsByName(String string) {

        /*
         * TODO
         * 
         * Retrieve artist by firstname OR lastname, lowercase
         */
        return null;
    }

    public List<Artist> findArtistByStyle(String name) {
        /*
         * TODO
         * 
         * Retrieve artist by style, lowercase
         */
        return null;
    }

    public List<Artist> findArtistByBand(String string) {
        /*
         * TODO
         * 
         * Retrieve artist by band, lowercase
         */
        return null;
    }

}
