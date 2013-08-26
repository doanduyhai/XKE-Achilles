package info.archinnov.achilles.demo.music.entity.cart;

import info.archinnov.achilles.demo.music.entity.song.Song;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cart {

    @Id
    private Long userId;

    @Column
    private List<Song> songs;

    public Cart() {
    }

    public Cart(Long userId, List<Song> songs) {
        this.userId = userId;
        this.songs = songs;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

}
