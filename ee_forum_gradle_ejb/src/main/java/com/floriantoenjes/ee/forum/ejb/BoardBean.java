package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Board;
import com.floriantoenjes.ee.forum.ejb.model.Thread;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
@LocalBean
public class BoardBean {

    @PersistenceContext
    private EntityManager em;

    public Board find(Long id) {
        return em.find(Board.class, id);
    }

    public Board findWithTreads(Long id) {
        TypedQuery<Board> query = em.createQuery("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.threads " +
                "WHERE b.id = :id", Board.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Board> findAll() {
        TypedQuery<Board> query = em.createQuery("SELECT b FROM Board b", Board.class);
        return query.getResultList();
    }

    public Board createBoard(Board board) {
        em.persist(board);
        return board;
    }

    public void editBoard(Board board) {
        em.merge(board);
    }
}
