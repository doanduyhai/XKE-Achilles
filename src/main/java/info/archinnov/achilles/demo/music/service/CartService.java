package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.entity.manager.CQLEntityManager;

public class CartService {

    private CQLEntityManager em;

    private Integer cartDuration = 3600; // Le panier dure 3600 secs soit 1H   

    public void addSong(Long userId, Song song) {

        /*
         * TODO
         * 
         * Ajouter la chanson au panier
         * 
         * Pour l'utilisation du TTL: https://github.com/doanduyhai/Achilles/wiki/Quick-Reference#working-with-ttl
         */

    }

}
