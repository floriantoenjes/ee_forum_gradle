package com.floriantoenjes.ee.forum.ejb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "FORUM", name = "THREAD")
public class Thread implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 5;


    @Id
    @SequenceGenerator(
            name = "THREAD_ID_GENERATOR",
            sequenceName = "SEQ_THREAD",
            schema = "FORUM",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "THREAD_ID_GENERATOR"
    )
    private Long id;

    @NotNull
    @Size(max = 20)
    private String name;

    @NotNull
    @ManyToOne
    private Board board;

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @OneToOne(mappedBy = "threadOneToOne")
    private Post lastPost;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @OneToOne
    @JoinColumn(name = "BOARD_ONE_TO_ONE_ID")
    private Board boardOneToOne;

    public Thread() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public boolean addPost(Post post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        post.setThread(this);

        lastPost = post;
        updated = new Date();
        post.setThreadOneToOne(this);

        return this.posts.add(post);
    }

    public Post getLastPost() {
        return lastPost;
    }

    public void setLastPost(Post lastPost) {
        this.lastPost = lastPost;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getPages() {
        return (long) Math.ceil((posts.size() - 1) / PAGE_SIZE );
    }

    public Board getBoardOneToOne() {
        return boardOneToOne;
    }

    public void setBoardOneToOne(Board boardOneToOne) {
        this.boardOneToOne = boardOneToOne;
    }
}
