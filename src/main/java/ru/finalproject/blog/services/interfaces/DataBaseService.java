package ru.finalproject.blog.services.interfaces;

import ru.finalproject.blog.database.tables.Post;
import ru.finalproject.blog.database.tables.User;

import java.util.List;

public interface DataBaseService {
    enum Errors {OK, DUPLICATE, DBERROR}

    User loginInBlog(String login, String pass);
    List getAllBlogPosts();
    User getUserById(int user_id);
    Post getPostById(int post_id);

    Errors createNew(String login, String pass);
    Errors addNewPost(User userId, String title, String body);
    Errors addNewComent(User userId, Post postId, String body);
    Errors editOnePost(Post post);

    void delCurrentPost(Post post);

}
