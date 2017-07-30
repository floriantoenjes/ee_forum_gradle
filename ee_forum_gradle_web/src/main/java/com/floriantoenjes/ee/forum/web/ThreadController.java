package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.BoardBean;
import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.model.Board;
import com.floriantoenjes.ee.forum.ejb.model.Post;
import com.floriantoenjes.ee.forum.ejb.model.Thread;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class ThreadController implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long threadId;

    private Long boardId;

    private Board board;

    private List<Thread> threads;

    @Inject
    private Post post;

    @Inject
    private Thread thread;

    @EJB
    private BoardBean boardBean;

    @EJB
    private ThreadBean threadBean;

    @EJB
    private PostBean postBean;

    public String initThread() {
        thread = threadBean.find(threadId);
        if (thread == null) {
            return "pretty:not-found";
        }

        return null;
    }

    public String loadThreads() {
        board = boardBean.find(boardId);
        if (board == null) {
            return "pretty:not-found";
        }
        threads = threadBean.findByBoardId(boardId);

        return null;
    }

    public String createThread(User user) {

        thread.setAuthor(user);
        thread.setBoard(getBoard());
        thread.setCreated(new Date());

        thread = threadBean.createThread(thread);

        post.setAuthor(user);
        post.setThread(thread);
        post.setCreated(new Date());

        postBean.createPost(post);

        return "pretty:viewBoard";
    }

    public String editThread() {
        threadBean.editThread(thread);

        return "pretty:viewBoard";
    }

    public String deleteThread() {
        threadBean.deleteThread(thread);

        return "pretty:viewBoard";
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Board getBoard() {
        return boardBean.find(boardId);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }
}
