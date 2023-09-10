package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabasic.reserve.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 영속 컨텍스트
 * - 일종의 메모리 공간
 * - 실제 데이터가 변경되었음을 감지하고 커밋하는 시점에 데이터를 실제 db에 반영함.
 */
public class UserSaveMain {
    private static Logger logger = LoggerFactory.getLogger(UserSaveMain.class);

    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("jpabegin");

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = new User("user@user.com", "user", LocalDateTime.now());
            entityManager.persist(user);
            logger.info("EntityManager.persist 호출함");
            transaction.commit(); // 쿼리가 실행되는 시점에 실제 데이터가 변경됨 ->  영속컨텍스트의 가장 큰 특징
            logger.info("EntityTransaction.commit 호출함");
        } catch (Exception ex) {
            logger.error("에러 발생: " + ex.getMessage(), ex);
            transaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
