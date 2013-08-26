package info.archinnov.achilles.demo.music.service;

import info.archinnov.achilles.demo.music.entity.user.User;
import info.archinnov.achilles.entity.manager.CQLEntityManager;
import org.apache.commons.lang.math.RandomUtils;

public class UserService {

    private CQLEntityManager em;

    public Long createUser(User user)
    {
        Long userId = RandomUtils.nextLong();

        /*
         * TODO
         * 
         * Assigner un nouveau userId à l'entité User
         * 
         * Sauvegarder l'user dans Cassandra
         * 
         * Retourner l'userId
         */

        return userId;
    }

}
