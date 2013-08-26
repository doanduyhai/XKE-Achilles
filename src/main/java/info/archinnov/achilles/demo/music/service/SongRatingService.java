package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.model.SongRating;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.List;
import java.util.UUID;

public class SongRatingService {

    private CQLEntityManager em;

    public void rateSong(UUID songId, SongRating songRating) {

        /*
         * TODO
         * 
         * 1. Créer une ou des entités pour persister le rating
         * 
         * 2. Incrémenter le compteur de rating de la chanson
         */
    }

    public List<SongRating> getRatingByDate(UUID songId, boolean newestFirst) {

        /*
         * TODO
         * 
         * Retourner les ratings, triés par ordre décroissant de date
         */

        return null;
    }

    public List<SongRating> getRatingByRate(UUID songId, boolean bestFirst) {

        /*
         * TODO
         * 
         * Retourner les ratings, triés par ordre décroissant de note (meilleures notes d'abord)
         */

        return null;
    }

}
