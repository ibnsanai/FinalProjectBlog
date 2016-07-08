package ru.finalproject.blog.beans;

import org.jboss.weld.environment.se.Weld;
import ru.finalproject.blog.services.interfaces.DataBaseService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

@ManagedBean(name="addnewpost")
@ViewScoped
public class AddNewPostBean implements Serializable {

    @ManagedProperty(value = "#{mainpage}")
    private MainPageBean mainPageBean ;
    public void setMainPageBean(MainPageBean mainPageBean) {
        this.mainPageBean = mainPageBean;
    }

    @Inject
    private DataBaseService dataBaseService;

    private String msgAddPost;
    private String title;
    private String body;
    private String param = "";

    @PostConstruct
    public void init() {
        //Вариант считывать параметры с URL
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,String> params = ctx.getExternalContext().getRequestParameterMap();
        //Вариант считать параметры с Flesh-a
        param = (String)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("err");
        if (params.containsKey("err")) {
            switch (params.get("err")) {
                case "dberr": {
                    setMsgAddPost("Во время работы произошла какая-то ошибка.");
                    break;
                }
                case "emptyerr": {
                    setMsgAddPost("Оба поля обязательны для заполнения.");
                    break;
                }
                default: {
                    setMsgAddPost("Введите тему и текст поста.");
                    break;
                }
            }
        }else setMsgAddPost("Введите тему и текст поста.");
    }

    public AddNewPostBean() {

    }

    public String getMsgAddPost() {
        return msgAddPost;
    }
    public void setMsgAddPost(String msgAddPost) {
        this.msgAddPost = msgAddPost;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String addNewPost(String title, String body){
        if (!title.equals("") && !body.equals("")) {

            Weld weld = new Weld();
            dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
            DataBaseService.Errors result = dataBaseService.addNewPost(mainPageBean.getLoginUser(),title, body);
            weld.shutdown();

            if (result == DataBaseService.Errors.OK) {

                this.param = "noerr";
                //добавить параметр во Flash
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("err", "noerr");
                //После добавления новой записи обновить список всех, на главной странице
                mainPageBean.updAllPosts();
                //добавить параметры в URL, обязателен редирект
                return "mainpage?faces-redirect=true&err=" + this.param;
            } else {
                this.param = "dberr";
                FacesContext.getCurrentInstance().getExternalContext().getFlash().put("err", "dberr");
                return "addnewpost?faces-redirect=true&err=" + this.param;
            }

        }else {
            this.param = "emptyerr";
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("err", "emptyerr");
            return "addnewpost?faces-redirect=true&err=" + this.param;
        }
    }

}
