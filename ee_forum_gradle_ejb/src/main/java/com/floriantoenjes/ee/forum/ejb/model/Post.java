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

    @NotNull
    @ManyToOne
    private Thread thread;

    @NotNull
    @ManyToOne
    private User author;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

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
}
