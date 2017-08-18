package com.floriantoenjes.ee.forum.ejb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(schema = "FORUM", name = "POST")
public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "POST_ID_GENERATOR",
            sequenceName = "SEQ_POST",
            schema = "FORUM",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "POST_ID_GENERATOR"
    )
    private Long id;

    @NotNull
    @Size(min = 5, max = 1000)
    private String text;

//    @NotNull
    @ManyToOne
    private Thread thread;

    @OneToOne
    @JoinColumn(name = "THREAD_ONE_TO_ONE_ID")
    private Thread threadOneToOne;

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @NotNull
    @Column(name = "POST_NUMBER")
    private Long postNumber;

    @NotNull
    private Boolean deleted = false;

    public Post() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
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

    public Thread getThreadOneToOne() {
        return threadOneToOne;
    }

    public void setThreadOneToOne(Thread threadOneToOne) {
        this.threadOneToOne = threadOneToOne;
    }

    public Long getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(Long postNumber) {
        this.postNumber = postNumber;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return id != null ? id.equals(post.id) : post.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
