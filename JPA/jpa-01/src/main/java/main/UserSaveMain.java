package main;

import jakarta.persistence.*;
import jpabasic.reserve.domain.User;

import java.time.LocalDateTime;

public class UserSaveMain {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabegin"); // 최초 한번만 수행

        EntityManager entityManager = emf.createEntityManager(); // 팩토리로 생성 가능한 엔티티 매니저로 db 연동에 대한 모든 것을 사용함.
        EntityTransaction transaction = entityManager.getTransaction(); // 트랜잭션이 필요한 경우 사용
        try {
            transaction.begin();
            User user = new User("user@user.com", "user", LocalDateTime.now());
            entityManager.persist(user);
            transaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
