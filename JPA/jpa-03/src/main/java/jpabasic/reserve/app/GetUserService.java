package jpabasic.reserve.app;

import jakarta.persistence.EntityManager;
import jpabasic.reserve.domain.User;
import jpabasic.reserve.jpa.EMF;

public class GetUserService {
    public User getUser(String email) {
        EntityManager em = EMF.createEntityManager();
        try {
            User user = em.find(User.class, email); // 데이터를 찾기 위해 사용, find(엔티티 타입, ID 타입) 전달
            if (user == null) {
                throw new NoUserException();
            }
            return user;
        } finally {
            em.close();
        }
    }
}
