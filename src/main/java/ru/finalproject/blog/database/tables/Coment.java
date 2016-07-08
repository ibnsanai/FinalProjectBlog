package ru.finalproject.blog.database.tables;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "COMENTS", uniqueConstraints =
                          {@UniqueConstraint(columnNames = "ID")})
public class Coment implements Serializable {
    private int id;
    private User userId;
    private Post postId;
    private String body;
    private Date publDate;

    public Coment() {
    }

    public Coment(User userId, Post postId, String body, Date publDate) {
        this.userId = userId;
        this.postId = postId;
        this.body = body;
        this.publDate = publDate;
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    public User getUserId() {
        return userId;
    }
    public void setUserId(User userId) {
        this.userId = userId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "POST_ID", nullable = false)
    public Post getPostId() {
        return postId;
    }
    public void setPostId(Post postId) {
        this.postId = postId;
    }

    @Column(name = "BODY", nullable = false)
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PUBLDATE", nullable = false)
    public Date getPublDate() {
        return publDate;
    }
    public void setPublDate(Date publDate) {
        this.publDate = publDate;
    }
}
