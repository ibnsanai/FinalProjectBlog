package ru.finalproject.blog.beans;

import org.jboss.weld.environment.se.Weld;
import ru.finalproject.blog.database.tables.Post;
import ru.finalproject.blog.database.tables.User;
import ru.finalproject.blog.services.interfaces.DataBaseService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@ManagedBean(name="mainpage")
@SessionScoped
public class MainPageBean implements Serializable {
    @Inject
    private DataBaseService dataBaseService;

    //блок переменных для пользователя
    private boolean loginedUser = false;
    private boolean getLogin = false;
    private String msgLogin = "Вы находитесь под Гостем" ;
    private String login ;
    private String password ;
    private User loginUser = null;

    //блок переменных для постов
    private List<Post> postList ;

    @PostConstruct
    private void init(){
        this.postList = getAllPosts();
    }

    public MainPageBean() {

    }

    public boolean isLoginedUser() {
        return loginedUser;
    }
    public boolean isGetLogin() {
        return getLogin;
    }
    public String setGetLogin(boolean getLogin) {
        this.getLogin = getLogin;
        return "mainpage";
    }

    public String getMsgLogin() {
        return msgLogin;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    User getLoginUser() {
        return loginUser;
    }
    private void setLoginUser(User loginUser) {
        this.loginUser = loginUser;
    }

    public List<Post> getPostList() {
        return postList;
    }
    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public String moveToRegistration(){
        msgLogin = "Теперь можете войти со своим логином и паролем" ;
        return "registration?faces-redirect=true";
    }

    private List<Post> getAllPosts(){
        Weld weld = new Weld();
        List<Post> list = weld.initialize().instance().select(DataBaseService.class).get().getAllBlogPosts();
        weld.shutdown();
        if (list != null && list.size()>0) {
            Comparator<Post> dateCompare = (o2, o1) -> o1.getPublDate().compareTo(o2.getPublDate());
            Collections.sort(list, dateCompare);
        }
        return list;
    }

    public String moveToQuit(){
        setLogin("");
        setPassword("");
        msgLogin = "Вы находитесь под Гостем" ;
        loginedUser = false;
        setLoginUser(null);
        return "mainpage";
    }

    String updAllPosts(){
        if (postList != null && postList.size()>0) {
            postList.clear();
        }
        this.postList = getAllPosts();
        return "mainpage";
    }

    public int getLoginUserId(){
        if (getLoginUser() != null){
            return getLoginUser().getId();
        } else return 0;
    }

    public String moveToLogin(String login, String pass){
        Weld weld = new Weld();
        dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
        setLoginUser(dataBaseService.loginInBlog(login,pass));
        if (loginUser != null){
            msgLogin = "Вы вошли под пользователем " + loginUser.getLogin();
            loginedUser = true ;
        } else {
            msgLogin = "Пользователь с данным данным сочетанием имени и пароля не найден. " ;
        }
        weld.shutdown();
        getLogin = false ;
        setLogin("");
        setPassword("");
        return "mainpage";
    }

    public String moveToAddNewPost(){
        return "addnewpost?faces-redirect=true";
    }

    public String moveToPost(){
        return "viewpostpage";
    }
}
