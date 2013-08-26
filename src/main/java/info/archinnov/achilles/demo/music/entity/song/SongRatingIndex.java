package info.archinnov.achilles.demo.music.entity.song;

import info.archinnov.achilles.demo.music.model.SongRating;
import javax.persistence.Column;

public abstract class SongRatingIndex {

    @Column
    protected SongRating songRating;

    public SongRating getSongRating() {
        return songRating;
    }

    public void setSongRating(SongRating songRating) {
        this.songRating = songRating;
    }
}
