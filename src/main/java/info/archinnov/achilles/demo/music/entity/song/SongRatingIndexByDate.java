package info.archinnov.achilles.demo.music.entity.song;

import info.archinnov.achilles.annotations.Order;
import info.archinnov.achilles.demo.music.model.SongRating;
import java.util.UUID;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SongRatingIndexByDate extends SongRatingIndex {

    @EmbeddedId
    private RatingByDateKey id;

    public SongRatingIndexByDate() {
    }

    public SongRatingIndexByDate(UUID songId, SongRating songRating, UUID ratingDate) {
        this.id = new RatingByDateKey(songId, ratingDate);
        this.songRating = songRating;
    }

    public RatingByDateKey getId() {
        return id;
    }

    public void setId(RatingByDateKey id) {
        this.id = id;
    }

    public static class RatingByDateKey
    {
        @Order(1)
        private UUID songId;

        @Order(2)
        private UUID date;

        public RatingByDateKey() {
        }

        public RatingByDateKey(UUID songId, UUID date) {
            this.songId = songId;
            this.date = date;
        }

        public UUID getSongId() {
            return songId;
        }

        public void setSongId(UUID songId) {
            this.songId = songId;
        }

        public UUID getDate() {
            return date;
        }

        public void setDate(UUID date) {
            this.date = date;
        }
    }
}
