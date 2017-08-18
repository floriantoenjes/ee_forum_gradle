package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Thread;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class ThreadBean {

    @PersistenceContext
    private EntityManager em;

    public ThreadBean() {
    }

    public Thread find(Long id) {
        return em.find(Thread.class, id);
    }

    public Thread findWithPosts(Long id) {
        TypedQuery<Thread> query = em.createQuery("SELECT t FROM Thread t LEFT JOIN FETCH t.posts WHERE t.id= :id", Thread.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<Thread> findByBoardId(Long boardId, int currentPage, int pageSize) {
        TypedQuery<Thread> query = em.createQuery("SELECT t FROM Thread t WHERE t.board.id = :boardId " +
                "ORDER BY t.updated DESC", Thread.class);
        query.setParameter("boardId", boardId);
        query.setFirstResult(currentPage * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public Thread createThread(Thread thread) {
        em.persist(thread);
        return thread;
    }

    public void editThread(Thread thread) {
        em.merge(thread);
    }

    public void deleteThread(Thread thread) {
        em.remove(em.merge(thread));
    }

    public long getTotalThreadsByBoardId(Long boardId) {
        Query queryTotal = em.createQuery("SELECT COUNT(t.id) FROM Thread t WHERE t.board.id = :boardId ");
        queryTotal.setParameter("boardId", boardId);
        return (long) queryTotal.getSingleResult();
    }
}
