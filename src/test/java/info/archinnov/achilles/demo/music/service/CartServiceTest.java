package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.constants.MusicStyle.POP;
import static org.fest.assertions.api.Assertions.assertThat;
import info.archinnov.achilles.demo.music.entity.cart.Cart;
import info.archinnov.achilles.demo.music.entity.song.Song;
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
public class CartServiceTest {

    @Rule
    public AchillesCQLResource resource = new AchillesCQLResource("info.archinnov.achilles.demo.music.entity",
            "Cart", "RateLimiting");

    @InjectMocks
    private CartService service;

    private CQLEntityManager em = resource.getEm();

    private final Long userId = RandomUtils.nextLong();

    private final Song yesterday = new Song("Yesterday",
            Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
            Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP),
            125, UUIDGen.getTimeUUID(), "Help!");

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(service, "em", em);
        yesterday.setId(UUIDGen.getTimeUUID());
    }

    @Test
    public void should_add_song_to_new_cart() throws Exception
    {
        service.addSong(userId, yesterday);

        Cart foundCart = em.rawTypedQuery(Cart.class, "SELECT * FROM Cart WHERE userId=" + userId).getFirst();

        assertThat(foundCart).isNotNull();

        List<Song> songs = foundCart.getSongs();

        assertThat(songs).hasSize(1);
        assertThat(songs.get(0)).isEqualTo(yesterday);
    }

    @Test
    public void should_expire_cart_after_a_while() throws Exception
    {
        Whitebox.setInternalState(service, "cartDuration", 1);

        service.addSong(userId, yesterday);

        Cart foundCart = em.rawTypedQuery(Cart.class, "SELECT * FROM Cart WHERE userId=" + userId).getFirst();
        assertThat(foundCart).isNotNull();

        //Wait for 2 secs
        Thread.sleep(2000);

        foundCart = em.rawTypedQuery(Cart.class, "SELECT * FROM Cart WHERE userId=" + userId).getFirst();

        assertThat(foundCart).isNull();
    }

    @Test
    public void should_reset_expiration_counter_when_adding_song_to_cart() throws Exception
    {
        Whitebox.setInternalState(service, "cartDuration", 2);

        Song nightBefore = new Song("The Night Before",
                Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
                Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP),
                155, UUIDGen.getTimeUUID(), "Help!");
        nightBefore.setId(UUIDGen.getTimeUUID());

        service.addSong(userId, yesterday);

        Thread.sleep(1000);

        service.addSong(userId, nightBefore);

        Thread.sleep(1000);

        Cart foundCart = em.rawTypedQuery(Cart.class, "SELECT * FROM Cart WHERE userId=" + userId).getFirst();

        assertThat(foundCart).isNotNull();

        List<Song> songs = foundCart.getSongs();

        assertThat(songs).hasSize(2);
        assertThat(songs.get(0)).isEqualTo(yesterday);
        assertThat(songs.get(1)).isEqualTo(nightBefore);

    }

    @Test(expected = IllegalStateException.class)
    public void should_exception_when_adding_more_than_3_songs_per_second() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();
        UUID nightBeforeId = UUIDGen.getTimeUUID();
        UUID hideYourLoveAwayId = UUIDGen.getTimeUUID();

        Song nightBefore = new Song("The Night Before",
                Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
                Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP),
                155, UUIDGen.getTimeUUID(), "Help!");

        Song hideYourLoveAway = new Song("You've Got to Hide Your Love Away",
                Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
                Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP), 125,
                UUIDGen.getTimeUUID(), "Help!");

        Whitebox.setInternalState(service, "purchaseThreshold", 2);

        yesterday.setId(yesterdayId);
        nightBefore.setId(nightBeforeId);
        hideYourLoveAway.setId(hideYourLoveAwayId);

        service.addSong(userId, yesterday);
        service.addSong(userId, nightBefore);
        service.addSong(userId, hideYourLoveAway);
    }

    @Test
    public void should_allow_adding_more_than_3_songs_per_second_with_pause() throws Exception
    {
        UUID yesterdayId = UUIDGen.getTimeUUID();
        UUID nightBeforeId = UUIDGen.getTimeUUID();
        UUID hideYourLoveAwayId = UUIDGen.getTimeUUID();

        Song nightBefore = new Song("The Night Before",
                Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
                Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP),
                155, UUIDGen.getTimeUUID(), "Help!");

        Song hideYourLoveAway = new Song("You've Got to Hide Your Love Away",
                Arrays.asList(RandomUtils.nextLong(), RandomUtils.nextLong()),
                Arrays.asList("John LENNON", "Paul McCARTNEY"), 1965, Sets.newHashSet(POP), 125,
                UUIDGen.getTimeUUID(), "Help!");

        Whitebox.setInternalState(service, "purchaseThreshold", 2);

        yesterday.setId(yesterdayId);
        nightBefore.setId(nightBeforeId);
        hideYourLoveAway.setId(hideYourLoveAwayId);

        service.addSong(userId, yesterday);
        service.addSong(userId, nightBefore);

        Thread.sleep(1000);

        service.addSong(userId, hideYourLoveAway);
    }
}
