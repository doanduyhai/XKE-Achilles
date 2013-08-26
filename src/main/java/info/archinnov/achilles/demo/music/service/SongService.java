package info.archinnov.achilles.demo.music.service;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import info.archinnov.achilles.demo.music.entity.song.Album;
import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import com.google.common.base.Preconditions;

public class SongService {

    private CQLEntityManager em;

    public Album createAlbum(Album album) {

        Preconditions.checkNotNull(album, "Album should not be null");
        Preconditions.checkNotNull(album.getTitle(), "Album title should not be null");
        Preconditions.checkArgument(isNotEmpty(album.getArtistIds()), "Album should have at least one artist id");
        Preconditions.checkArgument(isNotEmpty(album.getArtistNames()), "Album should have at least one artist name");

        UUID albumId = UUIDGen.getTimeUUID();
        album.setId(albumId);

        for (Song song : album.getSongs())
        {
            song.setAlbumId(albumId);
            song.setId(UUIDGen.getTimeUUID());
        }

        em.persist(album);

        return album;
    }

    public UUID createSong(Song song)
    {
        Preconditions.checkNotNull(song, "Song should not be null");
        Preconditions.checkNotNull(song.getTitle(), "Song title should not be null");
        Preconditions.checkNotNull(song.getDurationInSecs(), "Song duration should not be null");
        Preconditions.checkArgument(song.getDurationInSecs() > 0, "Song duration should be positive");
        Preconditions.checkArgument(isNotEmpty(song.getAuthorIds()), "Song should have at least one author id");
        Preconditions.checkArgument(isNotEmpty(song.getAuthorNames()), "Song should have at least one author name");

        UUID songId = UUIDGen.getTimeUUID();

        song.setId(songId);
        em.persist(song);

        return songId;
    }

}
