package info.archinnov.achilles.demo.music.entity.cart;

import info.archinnov.achilles.annotations.Order;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class RateLimiting {

    @EmbeddedId
    private RateLimitingKey id;

    public RateLimiting() {
    }

    public RateLimiting(Long userId, UUID songId) {
        this.id = new RateLimitingKey(userId, songId);
    }

    public RateLimitingKey getId() {
        return id;
    }

    public void setId(RateLimitingKey id) {
        this.id = id;
    }

    public static class RateLimitingKey
    {
        @Order(1)
        @Column
        private Long userId;

        @Order(2)
        @Column
        private UUID songId;

        public RateLimitingKey() {
        }

        public RateLimitingKey(Long userId, UUID songId) {
            this.userId = userId;
            this.songId = songId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public UUID getSongId() {
            return songId;
        }

        public void setSongId(UUID songId) {
            this.songId = songId;
        }

    }
}
