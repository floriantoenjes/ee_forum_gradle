package com.floriantoenjes.ee.forum.ejb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "FORUM", name = "BOARD")
@NamedQuery(
        name = "Board.findAll",
        query = "SELECT b FROM Board b"
)
public class Board {

    @Id
    @SequenceGenerator(
            name = "BOARD_ID_GENERATOR",
            sequenceName = "SEQ_BOARD",
            schema = "FORUM",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BOARD_ID_GENERATOR"
    )
    private Long id;

    @NotNull
    @Size(max = 20)
    private String name;

    @Size(max = 120, message = "has to be less than 120 characters")
    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Thread> threads;

    @OneToOne(mappedBy = "boardOneToOne")
    private Thread lastThread;

    // ToDo: Add a way for ordering

    public Board() {}

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public boolean addThread(Thread thread) {
        if (threads == null) {
            threads = new ArrayList<>();
        }
        thread.setBoard(this);
        return this.threads.add(thread);
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thread getLastThread() {
        return lastThread;
    }

    public void setLastThread(Thread lastThread) {
        this.lastThread = lastThread;
    }
}
