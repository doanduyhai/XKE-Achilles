package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.constants.MusicStyle.POP;
import static org.fest.assertions.api.Assertions.assertThat;
import info.archinnov.achilles.demo.music.constants.Rating;
import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.demo.music.model.SongRating;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.junit.AchillesCQLResource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class SongRatingServiceTest {

    @Rule
    public AchillesCQLResource resource = new AchillesCQLResource("info.archinnov.achilles.demo.music.entity",
            "Song", "SongIndex", "SongRatingIndexByDate", "SongRatingIndexByRate", "achilles_counter_table");

    private CQLEntityManager em = resource.getEm();

    @InjectMocks
    private SongService songService;

    @InjectMocks
    private SongRatingService ratingService;

    private final Song yesterday = new Song("Yesterday",
            Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
            Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP),
            125, UUIDGen.getTimeUUID(), "Help!");

    private final String johnDoe = "johnDoe";
    private final String helenSue = "helenSue";
    private final String richardSmith = "richardSmith";
    private final String alice = "alice";
    private final String bob = "bob";
    private final String chuck = "chuck";
    private final String eve = "eve";

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(songService, CQLEntityManager.class, em);
        Whitebox.setInternalState(ratingService, CQLEntityManager.class, em);
    }

    @Test
    public void should_rate_a_song() throws Exception
    {
        UUID songId = songService.createSong(yesterday);

        ratingService.rateSong(songId, new SongRating(Rating.THREE_AND_HALF, "comment", "userLogin"));

        Song foundYesterday = em.getReference(Song.class, songId);

        assertThat(foundYesterday.getRatingValue().get()).isEqualTo(Rating.THREE_AND_HALF.ratingValue());

    }

    @Test
    public void should_list_ratings_by_descending_date() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();

        ratingService.rateSong(yesterdayId, new SongRating(Rating.FOUR, "This song is really cool!", johnDoe));
        Thread.sleep(1);
        ratingService
                .rateSong(yesterdayId, new SongRating(Rating.ONE, "This song is awfull, so cheesy!", helenSue));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.FIVE, "Awesome, this is a must-have!",
                richardSmith));

        List<SongRating> ratings = ratingService.getRatingByDate(yesterdayId, true);

        assertThat(ratings).hasSize(3);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(richardSmith);
        assertThat(ratings.get(0).getRating()).isEqualTo(Rating.FIVE);
        assertThat(ratings.get(0).getComment()).isEqualTo("Awesome, this is a must-have!");

        assertThat(ratings.get(1).getUserLogin()).isEqualTo(helenSue);
        assertThat(ratings.get(1).getRating()).isEqualTo(Rating.ONE);
        assertThat(ratings.get(1).getComment()).isEqualTo("This song is awfull, so cheesy!");

        assertThat(ratings.get(2).getUserLogin()).isEqualTo(johnDoe);
        assertThat(ratings.get(2).getRating()).isEqualTo(Rating.FOUR);
        assertThat(ratings.get(2).getComment()).isEqualTo("This song is really cool!");
    }

    @Test
    public void should_list_ratings_by_descending_rate() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();

        ratingService.rateSong(yesterdayId, new SongRating(Rating.FOUR, "This song is really cool!", johnDoe));
        Thread.sleep(1);
        ratingService
                .rateSong(yesterdayId, new SongRating(Rating.ONE, "This song is awfull, so cheesy!", helenSue));
        Thread.sleep(1);
        ratingService
                .rateSong(yesterdayId, new SongRating(Rating.FIVE, "Awesome, this is a must-have!", richardSmith));

        List<SongRating> ratings = ratingService.getRatingByRate(yesterdayId, true);

        assertThat(ratings).hasSize(3);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(richardSmith);
        assertThat(ratings.get(0).getRating()).isEqualTo(Rating.FIVE);
        assertThat(ratings.get(0).getComment()).isEqualTo("Awesome, this is a must-have!");

        assertThat(ratings.get(1).getUserLogin()).isEqualTo(johnDoe);
        assertThat(ratings.get(1).getRating()).isEqualTo(Rating.FOUR);
        assertThat(ratings.get(1).getComment()).isEqualTo("This song is really cool!");

        assertThat(ratings.get(2).getUserLogin()).isEqualTo(helenSue);
        assertThat(ratings.get(2).getRating()).isEqualTo(Rating.ONE);
        assertThat(ratings.get(2).getComment()).isEqualTo("This song is awfull, so cheesy!");

    }

    @Test
    public void should_paginate_rating_by_date() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();

        ratingService.rateSong(yesterdayId, new SongRating(Rating.FOUR, "This song is really cool!", johnDoe));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.ONE, "This song is awfull, so cheesy!", helenSue));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.FIVE, "Awesome, this is a must-have!",
                richardSmith));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.TWO_AND_HALF, "Yet another Beattles song!", alice));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.ZERO, "This song sucks", bob));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.THREE, "Nice song but kind of oldy", chuck));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.TWO_AND_HALF, "Not unforgettable", eve));

        // Tri descendant par date
        List<SongRating> ratings = ratingService.paginateRatingByDate(yesterdayId, true, 2, null);

        assertThat(ratings).hasSize(2);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(eve);
        assertThat(ratings.get(1).getUserLogin()).isEqualTo(chuck);

        ratings = ratingService.paginateRatingByDate(yesterdayId, true, 10, ratings.get(1).getDate());

        assertThat(ratings).hasSize(5);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(bob);
        assertThat(ratings.get(1).getUserLogin()).isEqualTo(alice);
        assertThat(ratings.get(2).getUserLogin()).isEqualTo(richardSmith);
        assertThat(ratings.get(3).getUserLogin()).isEqualTo(helenSue);
        assertThat(ratings.get(4).getUserLogin()).isEqualTo(johnDoe);
    }

    @Test
    public void should_paginate_rating_by_rate() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();

        ratingService.rateSong(yesterdayId, new SongRating(Rating.FOUR, "This song is really cool!", johnDoe));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.ONE, "This song is awfull, so cheesy!", helenSue));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.FIVE, "Awesome, this is a must-have!",
                richardSmith));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.TWO_AND_HALF, "Yet another Beattles song!", alice));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.ZERO, "This song sucks", bob));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.THREE, "Nice song but kind of oldy", chuck));
        Thread.sleep(1);
        ratingService.rateSong(yesterdayId, new SongRating(Rating.TWO_AND_HALF, "Not unforgettable", eve));

        // Tri ascendant par note
        List<SongRating> ratings = ratingService.paginateRatingByRate(yesterdayId, false, 2, null);

        assertThat(ratings).hasSize(2);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(bob);
        assertThat(ratings.get(1).getUserLogin()).isEqualTo(helenSue);

        ratings = ratingService.paginateRatingByRate(yesterdayId, false, 10, ratings.get(1));

        assertThat(ratings).hasSize(5);

        assertThat(ratings.get(0).getUserLogin()).isEqualTo(alice);
        assertThat(ratings.get(1).getUserLogin()).isEqualTo(eve);
        assertThat(ratings.get(2).getUserLogin()).isEqualTo(chuck);
        assertThat(ratings.get(3).getUserLogin()).isEqualTo(johnDoe);
        assertThat(ratings.get(4).getUserLogin()).isEqualTo(richardSmith);
    }
}
