package info.archinnov.achilles.demo.music.service;

import static info.archinnov.achilles.demo.music.constants.MusicStyle.*;
import static org.fest.assertions.api.Assertions.assertThat;
import info.archinnov.achilles.demo.music.entity.song.Album;
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
public class SongServiceTest {

    @Rule
    public AchillesCQLResource resource = new AchillesCQLResource("info.archinnov.achilles.demo.music.entity",
            "Album", "Song", "SongIndex");

    @InjectMocks
    private SongService service;

    private CQLEntityManager em = resource.getEm();

    private final Long lennonId = RandomUtils.nextLong();
    private final String artistLennon = "John LENNON";

    private final Long mcCartneyId = RandomUtils.nextLong();
    private final String artistMcCartney = "Paul McCARTNEY";

    private final Long billyJoelId = RandomUtils.nextLong();
    private final String artistBillyJoel = "Billy JOEL";

    private final UUID helpAlbumId = UUIDGen.getTimeUUID();
    private final String helpAlbumTitle = "Help!";

    private final UUID haveABallAlbumId = UUIDGen.getTimeUUID();
    private final String haveABallAlbumTitle = "Have a ball";

    private final String beattlesBand = "The Beattles";

    private final Song yesterday = new Song("Yesterday", Arrays.asList(lennonId, mcCartneyId),
            Arrays.asList(artistLennon, artistMcCartney), 1965, Sets.newHashSet(POP),
            125, helpAlbumId, helpAlbumTitle);

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(service, CQLEntityManager.class, em);
    }

    @Test
    public void should_create_song() throws Exception
    {

        UUID songId = service.createSong(yesterday);

        Song foundYesterday = em.rawTypedQuery(Song.class, "SELECT * FROM Song WHERE id=" + songId).getFirst();

        assertThat(foundYesterday).isNotNull();
        assertThat(foundYesterday.getTitle()).isEqualTo(yesterday.getTitle());
        assertThat(foundYesterday.getAuthorIds()).containsExactly(lennonId, mcCartneyId);
        assertThat(foundYesterday.getAuthorNames()).containsExactly(artistLennon, artistMcCartney);
        assertThat(foundYesterday.getReleaseYear()).isEqualTo(yesterday.getReleaseYear());
        assertThat(foundYesterday.getStyles()).containsExactly(POP);
        assertThat(foundYesterday.getDurationInSecs()).isEqualTo(125);
        assertThat(foundYesterday.getAlbumId()).isEqualTo(helpAlbumId);
        assertThat(foundYesterday.getAlbumTitle()).isEqualTo(helpAlbumTitle);
    }

    @Test
    public void should_cascade_create_album_and_songs() throws Exception
    {

        Song nightBefore = new Song("The Night Before", Arrays.asList(lennonId, mcCartneyId),
                Arrays.asList(artistLennon, artistMcCartney), 1965, Sets.newHashSet(POP), 155,
                helpAlbumId, helpAlbumTitle);

        Album album = new Album(helpAlbumTitle, 1965, Arrays.asList(lennonId, mcCartneyId),
                Arrays.asList(artistLennon, artistMcCartney), beattlesBand, Arrays.asList(yesterday, nightBefore));

        Album actual = service.createAlbum(album);

        assertThat(actual).isNotNull();
        UUID albumId = actual.getId();

        assertThat(albumId).isNotNull();

        List<Song> songs = actual.getSongs();
        assertThat(songs).hasSize(2);

        UUID yesterdayId = songs.get(0).getId();
        assertThat(yesterdayId).isNotNull();

        UUID nightBeforeId = songs.get(1).getId();
        assertThat(nightBeforeId).isNotNull();

        Song foundYesterday = em.rawTypedQuery(Song.class, "SELECT * FROM Song WHERE id=" + yesterdayId).getFirst();

        assertThat(foundYesterday).isNotNull();
        assertThat(foundYesterday.getTitle()).isEqualTo(yesterday.getTitle());
        assertThat(foundYesterday.getAlbumId()).isEqualTo(albumId);

        Song foundNightBefore = em
                .rawTypedQuery(Song.class, "SELECT * FROM Song WHERE id=" + nightBeforeId)
                .getFirst();

        assertThat(foundNightBefore).isNotNull();
        assertThat(foundNightBefore.getTitle()).isEqualTo(nightBefore.getTitle());
        assertThat(foundNightBefore.getAlbumId()).isEqualTo(albumId);

        Album foundAlbum = em.typedQuery(Album.class, "SELECT * FROM Album WHERE id=" + actual.getId()).getFirst();

        assertThat(foundAlbum).isNotNull();
        assertThat(foundAlbum.getTitle()).isEqualTo(album.getTitle());
        assertThat(foundAlbum.getArtistIds()).containsExactly(lennonId, mcCartneyId);
        assertThat(foundAlbum.getArtistNames()).containsExactly(artistLennon, artistMcCartney);
        assertThat(foundAlbum.getBand()).isEqualTo(beattlesBand);
        assertThat(foundAlbum.getTrackCount()).isEqualTo(2);
        assertThat(foundAlbum.getTotalDurationInSecs()).isEqualTo(
                yesterday.getDurationInSecs() + nightBefore.getDurationInSecs());

        List<Song> foundSongs = foundAlbum.getSongs();
        assertThat(foundSongs).hasSize(2);
        assertThat(foundSongs).containsExactly(foundYesterday, foundNightBefore);
    }

    @Test
    public void should_find_songs_by_title() throws Exception
    {
        Song song1 = new Song("You've Got to Hide Your Love Away", Arrays.asList(lennonId, mcCartneyId),
                Arrays.asList(artistLennon, artistMcCartney), 1965, Sets.newHashSet(POP), 125, helpAlbumId,
                helpAlbumTitle);

        Song song2 = new Song("You're Going to Lose That Girl", Arrays.asList(lennonId, mcCartneyId),
                Arrays.asList(artistLennon, artistMcCartney), 1965, Sets.newHashSet(POP), 155, helpAlbumId,
                helpAlbumTitle);

        service.createSong(song1);
        service.createSong(song2);

        List<Song> foundSongs = service.findSongsByTitle("you");

        assertThat(foundSongs).hasSize(2);
        assertThat(foundSongs.get(0).getTitle()).isEqualTo(song2.getTitle());
        assertThat(foundSongs.get(1).getTitle()).isEqualTo(song1.getTitle());
    }

    @Test
    public void should_find_songs_by_artist() throws Exception
    {

        Song uptownGirl = new Song("Uptown Girl", Arrays.asList(billyJoelId), Arrays.asList(artistBillyJoel),
                1983, Sets.newHashSet(POP, ROCK), 198, haveABallAlbumId, haveABallAlbumTitle);

        service.createSong(yesterday);
        service.createSong(uptownGirl);

        List<Song> foundSongs = service.findSongsByArtist("Jo");

        assertThat(foundSongs).hasSize(2);
        assertThat(foundSongs.get(0).getTitle()).isEqualTo(uptownGirl.getTitle());
        assertThat(foundSongs.get(1).getTitle()).isEqualTo(yesterday.getTitle());
    }

    @Test
    public void should_find_songs_by_music_style() throws Exception
    {

        Song uptownGirl = new Song("Uptown Girl", Arrays.asList(billyJoelId), Arrays.asList(artistBillyJoel),
                1983, Sets.newHashSet(POP, ROCK), 198, haveABallAlbumId, haveABallAlbumTitle);

        service.createSong(yesterday);
        service.createSong(uptownGirl);

        List<Song> foundSongs = service.findSongsByStyle(ROCK.name());

        assertThat(foundSongs).hasSize(1);
        assertThat(foundSongs.get(0).getTitle()).isEqualTo(uptownGirl.getTitle());
    }

}
