package com.floriantoenjes.ee.forum.ejb;

import com.floriantoenjes.ee.forum.ejb.model.Board;

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

    @SuppressWarnings("unchecked")
    public List<Board> findAll() {
        TypedQuery<Board> query = em.createQuery("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.threads", Board.class);
        return query.getResultList();
    }

    public Board createBoard(Board board) {
        em.persist(board);
        return board;
    }
}
