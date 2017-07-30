package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Role;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Optional;

@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext
    private EntityManager em;

    public UserBean() {

    }

    public User register(User user) {
        Query query = em.createNamedQuery("Role.findByName");
        query.setParameter("name", "USER");
        Role userRole = (Role) query.getSingleResult();
        user.addRole(userRole);

        em.persist(user);
        return user;
    }

    public Optional<User> find(String username, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultList().stream().findFirst();
    }

    public Optional<byte[]> getAvatar(Long userId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<byte[]> cq = cb.createQuery(byte[].class);
        Root<User> u = cq.from(User.class);

        Path<Object> id = u.get("id");
        cq.select(u.get("avatar")).where(cb.equal(id, userId));
        TypedQuery<byte[]> typedQuery = em.createQuery(cq);

        return typedQuery.getResultList().stream().findFirst();
    }

    public void merge(User user) {
        em.merge(user);
    }
}
