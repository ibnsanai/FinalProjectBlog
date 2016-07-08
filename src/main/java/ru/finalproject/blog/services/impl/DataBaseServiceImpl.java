package ru.finalproject.blog.services.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import ru.finalproject.blog.database.HibernateUtils;
import ru.finalproject.blog.database.tables.Coment;
import ru.finalproject.blog.database.tables.Post;
import ru.finalproject.blog.database.tables.User;
import ru.finalproject.blog.services.interfaces.DataBaseService;

import java.util.Date;
import java.util.List;

public class DataBaseServiceImpl implements DataBaseService {
    private Session session = null;

    private void closeSession(){
        if (session != null && session.isOpen()) {
            try {
                session.flush();
                session.clear();
                session.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized Errors createNew(String login, String pass) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();

            session.beginTransaction();

            if (!checkDuplLogin(login,session)) {

                User newUser = new User(login, pass);
                session.save(newUser);

                session.getTransaction().commit();

                return Errors.OK;
            } else {
                return Errors.DUPLICATE;
            }

        }catch (Exception e) {
            e.printStackTrace();
            return Errors.DBERROR;
        }finally {
            closeSession();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized User loginInBlog(String login, String pass) {

        List<User> users ;
        User isLogin = null;

        try {
            session = HibernateUtils.getSessionFactory().openSession();

            Query query = session.createQuery("from User");

            users = (List<User>) query.list();

            for (User user : users){
                if (user.getLogin().toLowerCase().equals(login.toLowerCase()) &&
                    user.getPass().equals(pass)){
                    isLogin = user;
                    break;
                }
            }
            return isLogin;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            closeSession();
        }
    }

    private boolean checkDuplLogin(String login, Session session){
        Query query = session.createQuery("from User where login = :login ");
        query.setParameter("login", login);
        return  query.list().size() > 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Post> getAllBlogPosts() {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            Query query = session.createQuery("from Post");
            if (query.list().size()>0) {
                return query.list();
            }else{
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            closeSession();
        }
    }

    @Override
    public User getUserById(int user_id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            Query query = session.createQuery("from User where id = :id");
            query.setParameter("id", user_id);
            return  (User) query.list().get(0);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            closeSession();
        }
    }

    @Override
    public Post getPostById(int post_id) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            Query query = session.createQuery("from Post where id = :id");
            query.setParameter("id", post_id);
            return  (Post) query.list().get(0);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            closeSession();
        }
    }

    @Override
    public Errors addNewPost(User userId, String title, String body) {
        try {
            Date dt = new Date();
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Post newPost = new Post(userId, title, body, dt);
            session.save(newPost);
            session.getTransaction().commit();
            return Errors.OK;
        }catch (Exception e) {
            e.printStackTrace();
            return Errors.DBERROR;
        }finally {
            closeSession();
        }
    }

    @Override
    public Errors addNewComent(User userId, Post postId, String body) {
        try {
            Date dt = new Date();
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            Coment newComent = new Coment(userId, postId, body, dt);
            session.save(newComent);
            session.getTransaction().commit();
            return Errors.OK;
        }catch (Exception e) {
            e.printStackTrace();
            return Errors.DBERROR;
        }finally {
            closeSession();
        }
    }

    @Override
    public void delCurrentPost(Post post) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(post);
            session.getTransaction().commit();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeSession();
        }
    }

    @Override
    public Errors editOnePost(Post post) {
        try {
            session = HibernateUtils.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(post);
            session.getTransaction().commit();
            return Errors.OK;
        }catch (Exception e) {
            e.printStackTrace();
            return Errors.DBERROR;
        }finally {
            closeSession();
        }
    }
}
