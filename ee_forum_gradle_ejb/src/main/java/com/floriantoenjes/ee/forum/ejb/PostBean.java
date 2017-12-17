package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Post;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class PostBean extends EntityBean<Post> {

    @PersistenceContext
    private EntityManager em;

    public void createPost(Post post) {
        em.persist(post);
    }

    public Post find(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findByThreadId(Long threadId, int currentPage, int pageSize) {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.thread.id = :threadId " +
                "ORDER BY p.created ASC ", Post.class);
        query.setParameter("threadId", threadId);
        query.setFirstResult(currentPage * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public long getTotalPostsByThreadId(Long threadId) {
        Query queryTotal = em.createQuery("SELECT COUNT(p.id) FROM Post p WHERE p.thread.id = :threadId ");
        queryTotal.setParameter("threadId", threadId);
        return (long) queryTotal.getSingleResult();
    }

    public List<Post> findByText(String text, int currentPage, int pageSize) {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE LOWER(p.text) LIKE :text",
                Post.class);
        query.setFirstResult(currentPage * pageSize);
        query.setMaxResults(pageSize);
        query.setParameter("text", "%" + text + "%");

        return query.getResultList();
    }

    public long getTotalPostsByText(String text) {
        Query queryTotal = em.createQuery("SELECT COUNT(p.id) FROM Post p WHERE LOWER(p.text) LIKE :text");
        queryTotal.setParameter("text", "%" + text + "%");
        return (long) queryTotal.getSingleResult();
    }

    public void editPost(Post post) {
        em.merge(post);
    }

    public void deletePost(Post post) {
        em.remove(em.merge(post));
    }
}
