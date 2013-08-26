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
            "Song", "SongIndex");

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

}
