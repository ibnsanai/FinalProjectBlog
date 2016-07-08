package ru.finalproject.blog.beans;

import org.jboss.weld.environment.se.Weld;
import ru.finalproject.blog.services.interfaces.DataBaseService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Map;

@ManagedBean(name="registration")
@ViewScoped
public class RegistrationBean implements Serializable {

    private String msgReg = "Придумайте логин и пароль";
    private String login = "";
    private String password = "";

    @Inject
    private DataBaseService dataBaseService;

    @PostConstruct
    public void init() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,String> params = ctx.getExternalContext().getRequestParameterMap();
        if (params.containsKey("err")) {
            switch (params.get("err")) {
                case "dublerr": {
                    setMsgReg("Пользователь с таким логином уже зарегистрирован в системе, придумайте новый логин.");
                    break;
                }
                case "dberr": {
                    setMsgReg("Во время работы произошла какая-то ошибка.");
                    break;
                }
                case "emptyerr": {
                    setMsgReg("Оба поля обязательны для заполнения.");
                    break;
                }
                default: {
                    setMsgReg("Придумайте логин и пароль.");
                    break;
                }
            }
        }else setMsgReg("Придумайте логин и пароль.");
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMsgReg() {
        return msgReg;
    }
    public void setMsgReg(String msgReg) {
        this.msgReg = msgReg;
    }

    public String saveRegistrationData(String login, String password){
        if (!login.equals("") && !password.equals("")) {

            Weld weld = new Weld();
            dataBaseService = weld.initialize().instance().select(DataBaseService.class).get();
            DataBaseService.Errors result = dataBaseService.createNew(login, password);

            setLogin("");
            setPassword("");
            weld.shutdown();

            if (result == DataBaseService.Errors.OK) {
                return "mainpage?faces-redirect=true&err=noerr";
            } else {
                if (result == DataBaseService.Errors.DUPLICATE) {
                    return "registration?faces-redirect=true&err=dublerr";
                } else {
                    return "registration?faces-redirect=true&err=dberr";
                }
            }
        } else {
            return "registration?faces-redirect=true&err=emptyerr";
        }
    }
}
