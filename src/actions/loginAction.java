package actions;

import com.opensymphony.xwork2.ActionSupport;

public class loginAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -350079095938246571L;
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


	public String execute() throws Exception
	{
		String usernameArr = "";
		String passwordArr = "";
		if(usernameArr.contains(getUsername())
				&& passwordArr.contains(getPassword()))
		{
			return SUCCESS;
		}	
		else
		{
			return ERROR;
		}
		
	}

}
