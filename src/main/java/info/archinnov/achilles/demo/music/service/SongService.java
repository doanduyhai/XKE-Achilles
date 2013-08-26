package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.song.Album;
import info.archinnov.achilles.demo.music.entity.song.Song;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;

public class SongService {

    private CQLEntityManager em;

    public Album createAlbum(Album album) {
        UUID albumId = UUIDGen.getTimeUUID();

        /*
         * TODO
         * 
         * Assigner un nouveau albumId à l'entité Album
         * 
         * POur chaque entité Song dans l'album, leur assigner un songId généré
         * 
         * Sauvegarder l'album dans Cassandra
         * 
         * Retourner un album avec les songs et leurs ids
         */

        return album;
    }

    public UUID createSong(Song song)
    {
        UUID songId = UUIDGen.getTimeUUID();

        /*
         * TODO
         * 
         * Assigner un nouveau songId à l'entité Song
         * 
         * Sauvegarder la chanson dans Cassandra
         * 
         * Retourner le songId
         */

        return songId;
    }

}
