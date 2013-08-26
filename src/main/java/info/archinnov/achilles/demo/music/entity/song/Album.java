package info.archinnov.achilles.demo.music.entity.song;

import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.google.common.base.Objects;

@Entity
@JsonSerialize(include = Inclusion.NON_NULL)
public class Album {

    @Id
    private UUID id;

    @Column
    private String title;

    @Column
    private Integer releaseYear;

    @Column
    private List<Long> artistIds;

    @Column
    private List<String> artistNames;

    @Column
    private String band;

    @Column
    private Integer trackCount;

    @Column
    private Integer totalDurationInSecs;

    @JoinColumn
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Song> songs;

    public Album() {
    }

    public Album(String title,
            Integer releaseYear,
            List<Long> artistIds,
            List<String> artistNames,
            String band,
            List<Song> songs) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.artistIds = artistIds;
        this.artistNames = artistNames;
        this.band = band;
        this.trackCount = songs.size();
        this.totalDurationInSecs = inferTotalDuration(songs);
        this.songs = songs;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public List<Long> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(List<Long> artistIds) {
        this.artistIds = artistIds;
    }

    public List<String> getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public Integer getTotalDurationInSecs() {
        return totalDurationInSecs;
    }

    public void setTotalDurationInSecs(Integer totalDurationInSecs) {
        this.totalDurationInSecs = totalDurationInSecs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    private Integer inferTotalDuration(List<Song> songs)
    {
        int totalDuration = 0;
        for (Song song : songs) {
            totalDuration += song.getDurationInSecs();
        }
        return totalDuration;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Album other = (Album) obj;

        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(id)
                .addValue(title)
                .addValue(releaseYear)
                .addValue(artistNames)
                .addValue(trackCount)
                .addValue(totalDurationInSecs)
                .toString();
    }
}
