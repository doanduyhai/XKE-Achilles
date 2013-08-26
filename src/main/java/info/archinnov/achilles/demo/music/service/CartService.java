package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.cart.Cart;
import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.type.OptionsBuilder;
import java.util.Arrays;
import java.util.List;

public class CartService {

    private CQLEntityManager em;

    private Integer cartDuration = 3600; // Le panier dure 3600 secs soit 1H   

    private Integer purchaseThreshold = 10; // 10 chansons par seconde mis dans le panier

    public void addSong(Long userId, Song song) {

        Cart foundCart = em.find(Cart.class, userId);
        if (foundCart != null)
        {
            List<Song> existingSongs = foundCart.getSongs();
            existingSongs.add(song);
            foundCart = new Cart(userId, existingSongs);
        }
        else
        {
            foundCart = new Cart(userId, Arrays.asList(song));
        }

        em.persist(foundCart, OptionsBuilder.withTtl(cartDuration));
    }

}
