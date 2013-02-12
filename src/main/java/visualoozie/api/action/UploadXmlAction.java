package visualoozie.api.action;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import visualoozie.page.action.PathConstants;
import visualoozie.util.XmlLoader;
import visualoozie.xsd.WORKFLOWAPP;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
//import org.hibernate.exception.ConstraintViolationException;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage(PathConstants.PARENT_PACKAGE_JSON_DEFAULT)
@Namespace(PathConstants.NAMESPACE_API)
public class UploadXmlAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private File xmlfile;

    private WORKFLOWAPP result;
    
    @Override
    @Action(value="upload_xml", results = {
            @Result(name = SUCCESS , type = "json" , params = { "root", "result" }
            )
        }
    )
    public String execute(){
        String xml;
        try {
            xml = FileUtils.readFileToString(xmlfile);
        }catch (IOException e){
            e.printStackTrace();
            result = null;
            return SUCCESS;
        }

        XmlLoader loader = new XmlLoader();
        try {
            result = loader.loadString(xml);
            System.out.println(result.getEnd().getName());
        }
        catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return SUCCESS;

    }

    public WORKFLOWAPP getResult() { return result; }
    public void setResult(WORKFLOWAPP result) { this.result = result; }

    public File getXmlfile() { return xmlfile; }
    public void setXmlfile(File xmlfile) { this.xmlfile = xmlfile; }

}
