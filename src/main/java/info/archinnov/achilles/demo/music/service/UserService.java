package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.user.User;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import org.apache.commons.lang.math.RandomUtils;
import com.google.common.base.Preconditions;

public class UserService {

    private CQLEntityManager em;

    public Long createUser(User user)
    {
        Preconditions.checkNotNull(user, "User should not be null");
        Preconditions.checkNotNull(user.getLogin(), "User login should not be null");
        Preconditions.checkNotNull(user.getPassword(), "User password should not be null");

        Long userId = RandomUtils.nextLong();
        user.setId(userId);

        em.persist(user);

        return userId;
    }

}
