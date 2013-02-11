package net.junaoki.fflo.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

public class ShowStartPageAction extends ActionSupport {

	private static final long serialVersionUID = 7129864569328965285L;

	@Override
	@Action(value="ShowStartPage"
		, results={
			@Result(name=SUCCESS, location="/jsp/StartPage.jsp")
	})
	public String execute() throws Exception {
        return SUCCESS;
	}

}
