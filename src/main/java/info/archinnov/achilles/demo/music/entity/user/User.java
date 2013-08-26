package info.archinnov.achilles.demo.music.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.codec.digest.DigestUtils;
import com.google.common.base.Objects;

@Entity
public class User {

    @Id
    private Long id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String description;

    public User() {
    }

    public User(String login,
            String password,
            String firstname,
            String lastname,
            String description) {
        this.login = login;
        this.password = DigestUtils.sha512Hex(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        final User other = (User) obj;

        return Objects.equal(this.id, other.id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(id)
                .addValue(login)
                .addValue(password)
                .addValue(firstname)
                .addValue(lastname)
                .addValue(description)
                .toString();
    }

}
