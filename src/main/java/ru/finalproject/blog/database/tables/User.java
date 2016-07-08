package ru.finalproject.blog.database.tables;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "USERS", uniqueConstraints =
                       {@UniqueConstraint(columnNames = "ID"),
                        @UniqueConstraint(columnNames = "LOGIN")})
public class User implements Serializable{

    private int id;
    private String login;
    private String pass;
    private List<Post> postSet;
    private List<Coment> comentSet;

    public User() {
    }

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
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

    @Column(name = "LOGIN", unique = true, nullable = false, length = 128)
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "PASS", nullable = false, length = 20)
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userId")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    public List<Post> getPostSet() {
        return postSet;
    }
    public void setPostSet(List<Post> postSet) {
        this.postSet = postSet;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userId")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    public List<Coment> getComentSet() {
        return comentSet;
    }
    public void setComentSet(List<Coment> comentSet) {
        this.comentSet = comentSet;
    }
}
