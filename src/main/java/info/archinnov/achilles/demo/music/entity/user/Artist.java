package info.archinnov.achilles.demo.music.entity.user;

import info.archinnov.achilles.demo.music.constants.MusicStyle;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import org.apache.commons.lang.RandomStringUtils;
import com.google.common.base.Objects;

@Entity
public class Artist extends User {

    @Column
    private Set<MusicStyle> styles;

    @Column
    private Set<String> bands;

    @Column
    private Integer activeSince;

    @Column
    private String website;

    public Artist() {
    }

    public Artist(String firstname,
            String lastname,
            String description,
            Set<MusicStyle> styles,
            Set<String> bands,
            Integer activeSince,
            String website) {
        super(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), firstname,
                lastname, description);
        this.styles = styles;
        this.bands = bands;
        this.activeSince = activeSince;
        this.website = website;

    }

    public Set<MusicStyle> getStyles() {
        return styles;
    }

    public void setStyles(Set<MusicStyle> styles) {
        this.styles = styles;
    }

    public Set<String> getBands() {
        return bands;
    }

    public void setBands(Set<String> bands) {
        this.bands = bands;
    }

    public Integer getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(Integer activeSince) {
        this.activeSince = activeSince;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(getId())
                .addValue(getLogin())
                .addValue(getPassword())
                .addValue(getFirstname())
                .addValue(getLastname())
                .addValue(getDescription())
                .addValue(styles)
                .addValue(bands)
                .addValue(activeSince)
                .addValue(website)
                .toString();
    }
}
