package info.archinnov.achilles.demo.music.entity.song;

import info.archinnov.achilles.annotations.Order;
import info.archinnov.achilles.demo.music.constants.Rating;
import info.archinnov.achilles.demo.music.model.SongRating;
import java.util.UUID;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class SongRatingIndexByRate extends SongRatingIndex {

    @EmbeddedId
    private RatingByRateKey id;

    public SongRatingIndexByRate() {
    }

    public SongRatingIndexByRate(UUID songId, SongRating songRating, UUID date) {
        this.id = new RatingByRateKey(songId, songRating.getRating(), date);
        this.songRating = songRating;
    }

    public RatingByRateKey getId() {
        return id;
    }

    public void setId(RatingByRateKey id) {
        this.id = id;
    }

    public static class RatingByRateKey
    {
        @Order(1)
        private UUID songId;

        @Order(2)
        private Long ratingValue;

        @Order(3)
        private UUID date;

        public RatingByRateKey() {
        }

        public RatingByRateKey(UUID songId, Rating rating, UUID date) {
            this.songId = songId;
            this.ratingValue = rating.ratingValue();
            this.date = date;
        }

        public UUID getSongId() {
            return songId;
        }

        public void setSongId(UUID songId) {
            this.songId = songId;
        }

        public Long getRatingValue() {
            return ratingValue;
        }

        public void setRatingValue(Long ratingValue) {
            this.ratingValue = ratingValue;
        }

        public UUID getDate() {
            return date;
        }

        public void setDate(UUID date) {
            this.date = date;
        }
    }
}
