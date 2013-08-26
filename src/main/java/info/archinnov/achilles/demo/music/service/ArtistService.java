package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.user.Artist;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import org.apache.commons.lang.math.RandomUtils;

public class ArtistService {

    private CQLEntityManager em;

    public Long createArtist(Artist artist)
    {
        Long userId = RandomUtils.nextLong();

        /*
         * TODO
         * 
         * Assigner un nouveau userId à l'entité Artist
         * 
         * Sauvegarder l'artiste dans Cassandra
         * 
         * Retourner l'userId
         */

        return userId;
    }

}
