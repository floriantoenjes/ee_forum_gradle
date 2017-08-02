package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.BoardBean;
import com.floriantoenjes.ee.forum.ejb.model.Board;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class BoardController {
    private List<Board> boards;

    @Inject
    private Board board;

    @EJB
    private BoardBean boardBean;

    public BoardController() {

    }

    public String createBoard() {
        boardBean.createBoard(board);
        return "pretty:home";
    }

    public void loadBoards() {
        boards = boardBean.findAll();
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
