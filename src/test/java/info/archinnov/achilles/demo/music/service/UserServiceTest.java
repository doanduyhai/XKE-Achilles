package info.archinnov.achilles.demo.music.service;

import static org.fest.assertions.api.Assertions.*;
import info.archinnov.achilles.demo.music.entity.user.User;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import info.archinnov.achilles.junit.AchillesCQLResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Rule
    public AchillesCQLResource resource = new AchillesCQLResource("info.archinnov.achilles.demo.music.entity", "User");

    @InjectMocks
    private UserService service;

    private CQLEntityManager em = resource.getEm();

    @Before
    public void setUp()
    {
        Whitebox.setInternalState(service, CQLEntityManager.class, em);
    }

    @Test
    public void should_create_user() throws Exception
    {
        User user = new User("login", "password", "fn", "ln", "description");

        Long userId = service.createUser(user);

        assertThat(userId).isNotNull();

        User foundUser = em.rawTypedQuery(User.class, "SELECT * FROM User where id=" + userId).getFirst();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getLogin()).isEqualTo("login");

        // auto generated password with SHA 512
        assertThat(foundUser.getPassword()).isNotNull();
        assertThat(foundUser.getPassword().length()).isEqualTo(128);

        assertThat(foundUser.getFirstname()).isEqualTo("fn");
        assertThat(foundUser.getLastname()).isEqualTo("ln");
        assertThat(foundUser.getDescription()).isEqualTo("description");
    }
}
