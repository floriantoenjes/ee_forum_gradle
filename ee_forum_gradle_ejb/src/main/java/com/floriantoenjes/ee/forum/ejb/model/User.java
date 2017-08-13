package com.floriantoenjes.ee.forum.ejb.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "FORUM", name = "USR")
@NamedQuery(
        name = "User.findAll",
        query = "SELECT u FROM User u ORDER BY u.username ASC"
)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(
            name = "USR_ID_GENERATOR",
            sequenceName = "SEQ_USR",
            schema = "FORUM",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "USR_ID_GENERATOR"
    )
    private Long id;

    @NotNull(message = "is required")
    @Size(min = 3, max = 40, message = "has to be between 3 and 40 characters")
    @Pattern(regexp = "[a-zA-Z0-9-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+",
            message = "has to be a valid email address")
    private String email;

    @NotNull(message = "is required")
    @Size(min = 6, max = 10, message = "has to be between 6 and 10 characters")
    private String password;

    @NotNull(message = "is required")
    @Size(min = 4, max = 20, message = "has to be between 4 and 20 characters")
    private String username;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] avatar;

    @Size(max = 120, message = "has to be less than 120 characters")
    private String signature;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USR_ROLE",
            joinColumns = @JoinColumn(name = "USR_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "sender")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "receiver")
    private List<Message> messagesReceived;

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public boolean addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        return roles.add(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
