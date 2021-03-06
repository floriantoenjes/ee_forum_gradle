package com.floriantoenjes.ee.forum.web;

import com.floriantoenjes.ee.forum.ejb.BoardBean;
import com.floriantoenjes.ee.forum.ejb.PostBean;
import com.floriantoenjes.ee.forum.ejb.ThreadBean;
import com.floriantoenjes.ee.forum.ejb.UserBean;
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
public class PostController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;

    private Long boardId;
    private Long threadId;
    private Long postId;

    private Board board;
    private Thread thread;
    private List<Post> posts;

    private int currentPage;
    private List<Integer> pages = new ArrayList<>();

    @Inject
    private Post post;

    @EJB
    private PostBean postBean;

    @EJB
    private ThreadBean threadBean;

    @EJB
    private BoardBean boardBean;

    @EJB
    private UserBean userBean;

    public String initPost() {
        post = postBean.find(postId);
        if (post == null) {
            return "pretty:not-found";
        }
        return null;
    }

    public String loadPosts() {
        thread = threadBean.find(threadId);
        if (thread == null) {
            return "pretty:not-found";
        }

        board = thread.getBoard();
        posts = postBean.findByThreadId(threadId, currentPage, PAGE_SIZE);

        if (!createPagination()) {
            return "pretty:not-found";
        }

        return null;
    }

    public String createPost(User author) {
        Thread thread = threadBean.findWithPosts(threadId);
        Board board = thread.getBoard();
        board.clearLastThread().ifPresent(threadBean::editThread);

        thread.clearLastPost();
        thread.addPost(post);

        post.setAuthor(author);
        post.setCreated(new Date());
        author = userBean.findWithPosts(author.getId());
        author.addPost(post);

        threadBean.editThread(thread);
        boardBean.editBoard(board);
        userBean.merge(author);

        currentPage =  calculateCurrentPage(thread.getPosts().size());

        return "pretty:viewThreadPages";
    }

    public String editPost() {
        postBean.editPost(post);

        currentPage = calculateCurrentPage(post.getPostNumber());

        return "pretty:viewThreadPages";
    }

    public String deletePost() {

        post.setDeleted(true);
        post.setText("This post has been deleted.");
        postBean.editPost(post);

        currentPage = calculateCurrentPage(post.getPostNumber());

        return "pretty:viewThreadPages";
    }

    private boolean createPagination() {
        long totalPosts = postBean.getTotalPostsByThreadId(threadId);

        if (currentPage * PAGE_SIZE > totalPosts) {
            return false;
        }

        pages.clear();
        IntStream.range(0, (int) Math.ceil(totalPosts / (double) PAGE_SIZE)).forEach(pages::add);

        return true;
    }

    private int calculateCurrentPage(long n) {
        return (int) Math.ceil(n / (double) PAGE_SIZE) - 1;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Thread getThread() {
        return threadBean.find(threadId);
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}