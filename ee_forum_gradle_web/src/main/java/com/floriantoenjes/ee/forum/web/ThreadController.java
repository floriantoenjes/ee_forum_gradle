package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.BoardBean;
import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.model.Board;
import com.floriantoenjes.ee.forum.ejb.model.Post;
import com.floriantoenjes.ee.forum.ejb.model.Thread;
import com.floriantoenjes.ee.forum.ejb.model.User;

import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Named
@ViewScoped
public class ThreadController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;

    private Long threadId;

    private Long boardId;

    private Board board;

    private List<Thread> threads;

    private int currentPage;
    private List<Integer> pages = new ArrayList<>();

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
        threads = threadBean.findByBoardId(boardId, currentPage, PAGE_SIZE);

        if (!createPagination()) {
            return "pretty:not-found";
        }

        return null;
    }

    public String createThread(User user) {

        Board board = boardBean.findWithTreads(boardId);
        board.addThread(thread);

        thread.setAuthor(user);
        thread.setCreated(new Date());

        post.setAuthor(user);
        post.setCreated(new Date());

        board.clearLastThread();

        thread.addPost(post);

        boardBean.editBoard(board);

        return "pretty:viewBoard";
    }

    public String editThread() {
        threadBean.editThread(thread);

        return "pretty:viewBoard";
    }

    public String deleteThread() {
        Board board = boardBean.findWithTreads(boardId);
        board.removeThread(thread);

        boardBean.editBoard(board);

        return "pretty:viewBoard";
    }

    private boolean createPagination() {
        long totalThreads = threadBean.getTotalThreadsByBoardId(boardId);

        if (currentPage * PAGE_SIZE > totalThreads) {
            return false;
        }

        pages.clear();
        IntStream.range(0, (int) Math.ceil(totalThreads / (double) PAGE_SIZE)).forEach(pages::add);

        return true;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }
}
