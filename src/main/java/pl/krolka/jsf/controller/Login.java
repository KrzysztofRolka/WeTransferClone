package pl.krolka.jsf.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void login() {
	        
	        FacesContext context = FacesContext.getCurrentInstance();

	        if(this.username.equals("admin") && this.password.equals("admin")){
	            context.getExternalContext().getSessionMap().put("user", username);
	            try {
					context.getExternalContext().redirect("sec/admin_page.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        else  {
                 //Send an error message on Login Failure 
	            context.addMessage(null, new FacesMessage("Authentication Failed. Check username or password."));

	        } 
	    }

	public void logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();
		try {
			context.getExternalContext().redirect("/WeTransferClone/faces/login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
