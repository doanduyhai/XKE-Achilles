package info.archinnov.achilles.demo.music.entity.song;

import info.archinnov.achilles.demo.music.constants.MusicStyle;
import info.archinnov.achilles.type.Counter;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.google.common.base.Objects;

@Entity
@JsonSerialize(include = Inclusion.NON_NULL)
public class Song {

    @Id
    private UUID id;

    @Column
    private String title;

    @Column
    private List<Long> authorIds;

    @Column
    private List<String> authorNames;

    @Column
    private Integer releaseYear;

    @Column
    private Set<MusicStyle> styles;

    @Column
    private Integer durationInSecs;

    @Column
    private UUID albumId;

    @Column
    private String albumTitle;

    @Column
    private Counter ratingValue;

    public Song() {
    }

    public Song(String title,
            List<Long> authorIds,
            List<String> authorNames,
            Integer releaseYear,
            Set<MusicStyle> styles,
            Integer durationInSecs,
            UUID albumId,
            String albumTitle) {
        this.title = title;
        this.authorIds = authorIds;
        this.authorNames = authorNames;
        this.releaseYear = releaseYear;
        this.styles = styles;
        this.durationInSecs = durationInSecs;
        this.albumId = albumId;
        this.albumTitle = albumTitle;
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

    public List<Long> getAuthorIds() {
        return authorIds;
    }

    public void setAuthorIds(List<Long> authorIds) {
        this.authorIds = authorIds;
    }

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(List<String> authorNames) {
        this.authorNames = authorNames;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Set<MusicStyle> getStyles() {
        return styles;
    }

    public void setStyles(Set<MusicStyle> styles) {
        this.styles = styles;
    }

    public Integer getDurationInSecs() {
        return durationInSecs;
    }

    public void setDurationInSecs(Integer durationInSecs) {
        this.durationInSecs = durationInSecs;
    }

    public UUID getAlbumId() {
        return albumId;
    }

    public void setAlbumId(UUID albumId) {
        this.albumId = albumId;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public Counter getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Counter ratingValue) {
        this.ratingValue = ratingValue;
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
        final Song other = (Song) obj;

        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(id)
                .addValue(title)
                .addValue(authorNames)
                .addValue(releaseYear)
                .addValue(styles)
                .addValue(durationInSecs)
                .addValue(albumTitle)
                .toString();
    }
}
