package visualoozie.page.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;

public class VisualizeXmlAction extends ActionSupport{

    private static final long serialVersionUID = 1L;
    private static final String VERSION;
    static{
    	InputStream is = VisualizeXmlAction.class.getClassLoader().getResourceAsStream("visualoozie_version.properties");
    	Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e){}
		VERSION = (String) properties.get("visualoozie.version");
    	
    }

    @Override
	@Action(value="visualize_xml" , results={ @Result( name=SUCCESS, location="/jsp/VisualizeXml.jsp" ) })
	public String execute() throws IOException{
        return SUCCESS;
	}

    public String getVersion() { return VERSION; }

}
