package visualoozie.page.action;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.opensymphony.xwork2.ActionSupport;

public class VisualizeXmlAction extends ActionSupport{

    private static final long serialVersionUID = 1L;

    @Override
	@Action(value="visualize_xml" , results={ @Result( name=SUCCESS, location="/jsp/VisualizeXml.jsp" ) })
	public String execute() throws JsonMappingException, JsonParseException, IOException{
        return SUCCESS;
	}

}
