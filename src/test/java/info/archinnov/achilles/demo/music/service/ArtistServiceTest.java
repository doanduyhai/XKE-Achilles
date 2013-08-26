package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.constants.MusicStyle.*;
import static org.fest.assertions.api.Assertions.*;
import info.archinnov.achilles.demo.music.entity.user.Artist;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.junit.AchillesCQLResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class ArtistServiceTest {

    @Rule
    public AchillesCQLResource resource = new AchillesCQLResource("info.archinnov.achilles.demo.music.entity",
            "Artist");

    @InjectMocks
    private ArtistService service;

    private CQLEntityManager em = resource.getEm();

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(service, CQLEntityManager.class, em);
    }

    @Test
    public void should_create_artist() throws Exception
    {
        Artist artist = new Artist("John", "LENNON", "John Lennon from the Beattles",
                Sets.newHashSet(POP), Sets.newHashSet("The Beattles"), 1960, "http://www.john-lennon.com");

        Long artistId = service.createArtist(artist);

        assertThat(artistId).isNotNull();

        Artist foundArtist = em.rawTypedQuery(Artist.class, "SELECT * FROM Artist where id=" + artistId).getFirst();

        assertThat(foundArtist).isNotNull();

        // auto generated login
        assertThat(foundArtist.getLogin()).isNotNull();
        assertThat(foundArtist.getLogin().length()).isEqualTo(10);

        // auto generated password with SHA 512
        assertThat(foundArtist.getPassword()).isNotNull();
        assertThat(foundArtist.getPassword().length()).isEqualTo(128);

        assertThat(foundArtist.getFirstname()).isEqualTo("John");
        assertThat(foundArtist.getLastname()).isEqualTo("LENNON");
        assertThat(foundArtist.getDescription()).isEqualTo("John Lennon from the Beattles");

        assertThat(foundArtist.getStyles()).containsExactly(POP);
        assertThat(foundArtist.getBands()).containsExactly("The Beattles");
        assertThat(foundArtist.getActiveSince()).isEqualTo(1960);
        assertThat(foundArtist.getWebsite()).isEqualTo("http://www.john-lennon.com");
    }
}
