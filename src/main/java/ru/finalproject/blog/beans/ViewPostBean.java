package ru.finalproject.blog.beans;


import org.jboss.weld.environment.se.Weld;
import ru.finalproject.blog.database.tables.Post;
import ru.finalproject.blog.services.interfaces.DataBaseService;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

@ManagedBean(name="viewpostpage")
@ViewScoped
public class ViewPostBean implements Serializable{
    @Inject
    private DataBaseService dataBaseService;

    @ManagedProperty(value = "#{mainpage}")
    private MainPageBean mainPageBean ;
    public void setMainPageBean(MainPageBean mainPageBean) {
        this.mainPageBean = mainPageBean;
    }

    private int postId;
    private int userId;
    private int loginUserId;
    private boolean mayEdit;
    private boolean editMode;
    private Post onePost;
    private String postTitle;
    private String postBody;
    private String comentBody;

    @PostConstruct
    public void init() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,String> params = ctx.getExternalContext().getRequestParameterMap();

        if (params.containsKey("mode")){
            if (params.get("mode").equals("edit")) {
                setEditMode(true);
            } else setEditMode(false);
        } else setEditMode(false);

        if (params.containsKey("postId") && params.containsKey("userId") && params.containsKey("loginUserId")) {

            setPostId(Integer.parseInt(params.get("postId")));
            setUserId(Integer.parseInt(params.get("userId")));
            setLoginUserId(Integer.parseInt(params.get("loginUserId")));

            Weld weld = new Weld();
            dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
            setOnePost(dataBaseService.getPostById(postId));
            weld.shutdown();

            setMayEdit(userId - loginUserId == 0);

            setPostTitle(onePost.getTitle());
            setPostBody(onePost.getBody());
        }
    }

    public ViewPostBean() {
    }

    public boolean isMayEdit() {
        return mayEdit;
    }
    public void setMayEdit(boolean mayEdit) {
        this.mayEdit = mayEdit;
    }

    public boolean isEditMode() {
        return editMode;
    }
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    private void setPostId(int postId) {
        this.postId = postId;
    }
    private void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLoginUserId() {
        return loginUserId;
    }
    public void setLoginUserId(int loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Post getOnePost() {
        return onePost;
    }
    public void setOnePost(Post onePost) {
        this.onePost = onePost;
    }

    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }
    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public String getComentBody() {
        return comentBody;
    }
    public void setComentBody(String comentBody) {
        this.comentBody = comentBody;
    }

    public String addNewComent(String body){
        if (!body.equals("")){
            Weld weld = new Weld();
            dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
            dataBaseService.addNewComent(mainPageBean.getLoginUser(), onePost, body);
            weld.shutdown();
        }
        return "viewpostpage?faces-redirect=true" +
                             "&postId=" + onePost.getId() +
                             "&userId=" + onePost.getUserId().getId() +
                             "&loginUserId=" + loginUserId ;
    }

    public String delCurrentPost(){
        Weld weld = new Weld();
        dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
        dataBaseService.delCurrentPost(onePost);
        weld.shutdown();
        mainPageBean.updAllPosts();
        return "mainpage?faces-redirect=true";
    }

    public String getEditModePage(){
        return "viewpostpage?faces-redirect=true" +
                "&postId=" + onePost.getId() +
                "&userId=" + onePost.getUserId().getId() +
                "&loginUserId=" + loginUserId +
                "&mode=edit";
    }

    public String editPost(String title, String body){
        if (!title.equals("") && !body.equals("")) {
            Weld weld = new Weld();
            dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
            onePost.setTitle(title);
            onePost.setBody(body);
            DataBaseService.Errors result = dataBaseService.editOnePost(onePost);
            weld.shutdown();
            if (result == DataBaseService.Errors.OK) {
                mainPageBean.updAllPosts();
            }
        }
        return "viewpostpage?faces-redirect=true" +
                "&postId=" + onePost.getId() +
                "&userId=" + onePost.getUserId().getId() +
                "&loginUserId=" + loginUserId ;
    }

}
