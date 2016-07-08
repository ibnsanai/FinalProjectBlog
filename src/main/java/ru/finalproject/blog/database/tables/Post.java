package ru.finalproject.blog.database.tables;

import org.hibernate.annotations.*;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "POSTS", uniqueConstraints =
                        {@UniqueConstraint(columnNames = "ID")})
public class Post implements Serializable{
    private int id;
    private User userId;
    private String title;
    private String body;
    private Date publDate;
    private boolean comments;
    private List<Coment> comentSet;

    public Post() {
    }

    public Post(User userId, String title, String body, Date publDate) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.publDate = publDate;
        this.comments = false;
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "ID", unique = true, nullable = false)
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

    @Column(name = "TITLE", nullable = false)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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

    @Column(name = "COMENTS", nullable = false)
    public boolean isComments() {
        return comments;
    }
    public void setComments(boolean comments) {
        this.comments = comments;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "postId")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    public List<Coment> getComentSet() {
        if (comentSet != null && comentSet.size()>0) {
            Comparator<Coment> dateCompare = (o2, o1) -> o1.getPublDate().compareTo(o2.getPublDate());
            Collections.sort(comentSet, dateCompare);
        }
        return comentSet;
    }
    public void setComentSet(List<Coment> comentSet) {
        this.comentSet = comentSet;
    }
}
