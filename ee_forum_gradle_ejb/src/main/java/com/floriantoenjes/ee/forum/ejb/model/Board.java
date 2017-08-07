package com.floriantoenjes.ee.forum.ejb.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Thread> threads;

    @OneToOne(mappedBy = "boardOneToOne")
    private Thread lastThread;

    @Column(name = "THREAD_COUNT")
    private Long threadCount;

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
        boolean added = this.threads.add(thread);
        threadCount = (long) threads.size();
        return added;

    }

    public boolean removeThread(Thread thread) {
        if (threads != null) {
            thread.setBoard(null);
            boolean removed = threads.remove(thread);
            threadCount = (long) threads.size();
            return removed;
        }
        return false;
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

    public Optional<Thread> getAndClearLastThread() {
        Thread oldLastThread = lastThread;
        if (oldLastThread != null) {
            lastThread.setBoardOneToOne(null);
            lastThread = null;
            return Optional.of(oldLastThread);
        }
        return Optional.empty();
    }

    public void setLastThread(Thread lastThread) {
        this.lastThread = lastThread;
        lastThread.setBoardOneToOne(this);
    }

    public Long getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Long threadCount) {
        this.threadCount = threadCount;
    }
}
