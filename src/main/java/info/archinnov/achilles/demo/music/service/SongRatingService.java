package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.demo.music.entity.song.SongRatingIndex;
import info.archinnov.achilles.demo.music.entity.song.SongRatingIndexByDate;
import info.archinnov.achilles.demo.music.entity.song.SongRatingIndexByRate;
import info.archinnov.achilles.demo.music.model.SongRating;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.type.BoundingMode;
import info.archinnov.achilles.type.OrderingMode;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

public class SongRatingService {

    private static final Function<SongRatingIndex, SongRating> extractSongRating = new Function<SongRatingIndex, SongRating>() {

        @Override
        public SongRating apply(SongRatingIndex index)
        {
            return index.getSongRating();
        }
    };

    private CQLEntityManager em;

    public void rateSong(UUID songId, SongRating songRating) {
        Preconditions.checkNotNull(songId, "Song id should not be null for rating");
        Preconditions.checkNotNull(songRating, "Song rating should not be null for rating");
        Preconditions.checkNotNull(songRating.getRating(), "Song rating value should not be null for rating");

        Song song = em.getReference(Song.class, songId);
        song.getRatingValue().incr(songRating.getRating().ratingValue());
        em.merge(song);

        UUID ratingDate = UUIDGen.getTimeUUID();

        songRating.setDate(new Date(UUIDGen.getAdjustedTimestamp(ratingDate)));
        em.persist(new SongRatingIndexByDate(songId, songRating, ratingDate));
        em.persist(new SongRatingIndexByRate(songId, songRating, ratingDate));

    }

    public List<SongRating> getRatingByDate(UUID songId, boolean newestFirst) {

        List<SongRatingIndexByDate> ratingIndex = em.sliceQuery(SongRatingIndexByDate.class)
                .partitionKey(songId)
                .ordering(newestFirst ? OrderingMode.DESCENDING : OrderingMode.ASCENDING)
                .get(10);

        return FluentIterable.from(ratingIndex).transform(extractSongRating).toImmutableList();
    }

    public List<SongRating> getRatingByRate(UUID songId, boolean bestFirst) {

        List<SongRatingIndexByRate> ratingIndex = em.sliceQuery(SongRatingIndexByRate.class)
                .partitionKey(songId)
                .ordering(bestFirst ? OrderingMode.DESCENDING : OrderingMode.ASCENDING)
                .get(10);

        return FluentIterable.from(ratingIndex).transform(extractSongRating).toImmutableList();
    }

    public List<SongRating> paginateRatingByDate(UUID songId, boolean newestFirst, int pagingNumber, Date fromDate) {
        UUID fromUUID = fromDate != null ? UUIDGen.minTimeUUID(fromDate.getTime()) : null;

        List<SongRatingIndexByDate> ratingIndex = em.sliceQuery(SongRatingIndexByDate.class)
                .partitionKey(songId)
                .ordering(newestFirst ? OrderingMode.DESCENDING : OrderingMode.ASCENDING)
                .fromClusterings(fromUUID)
                .bounding(BoundingMode.INCLUSIVE_END_BOUND_ONLY)
                .get(pagingNumber);

        return FluentIterable.from(ratingIndex).transform(extractSongRating).toImmutableList();
    }

    public List<SongRating> paginateRatingByRate(UUID songId, boolean bestFirst, int pagingNumber,
            SongRating fromRating) {

        List<SongRatingIndexByRate> ratingIndex;
        OrderingMode ordering = bestFirst ? OrderingMode.DESCENDING : OrderingMode.ASCENDING;
        if (fromRating != null)
        {
            ratingIndex = getNextSongRatingPageByRate(songId, ordering, pagingNumber, fromRating);
        }
        else
        {
            ratingIndex = em.sliceQuery(SongRatingIndexByRate.class)
                    .partitionKey(songId)
                    .ordering(ordering)
                    .get(pagingNumber);
        }

        return FluentIterable.from(ratingIndex).transform(extractSongRating).toImmutableList();
    }

    private List<SongRatingIndexByRate> getNextSongRatingPageByRate(UUID songId, OrderingMode ordering,
            int pagingNumber, SongRating fromRating) {
        List<SongRatingIndexByRate> ratingIndex;
        long ratingValue = fromRating.getRating().ratingValue();
        UUID date = UUIDGen.maxTimeUUID(fromRating.getDate().getTime());
        ratingIndex = em.sliceQuery(SongRatingIndexByRate.class)
                .partitionKey(songId)
                .ordering(ordering)
                .fromClusterings(ratingValue, date)
                .toClusterings(ratingValue)
                .bounding(BoundingMode.INCLUSIVE_END_BOUND_ONLY)
                .get(pagingNumber);

        if (ratingIndex.size() == 0)
            ratingIndex = em.sliceQuery(SongRatingIndexByRate.class)
                    .partitionKey(songId)
                    .ordering(ordering)
                    .fromClusterings(ratingValue + 1)
                    .bounding(BoundingMode.INCLUSIVE_END_BOUND_ONLY)
                    .get(pagingNumber);
        return ratingIndex;
    }

}
