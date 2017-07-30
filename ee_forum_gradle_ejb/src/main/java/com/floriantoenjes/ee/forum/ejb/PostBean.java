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
public class PostBean {

    @PersistenceContext
    private EntityManager em;

    public void createPost(Post post) {
        em.persist(post);
    }

    public Post find(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findByThreadId(Long threadId) {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.thread.id = :threadId " +
                "ORDER BY p.created ASC ", Post.class);
        query.setParameter("threadId", threadId);

        return query.getResultList();
    }

    public List<Post> findByText(String text) {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE LOWER(p.text) LIKE :text",
                Post.class);
        query.setParameter("text", "%" + text + "%");

        return query.getResultList();

    }

    public void editPost(Post post) {
        em.merge(post);
    }

    public void deletePost(Post post) {
        em.remove(em.merge(post));
    }
}
