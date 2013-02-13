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

    private UploadXmlResult result;
    
    @Override
    @Action(value="upload_xml", results = {
            @Result(name = SUCCESS , type = "json" , params = { "root", "result" }
            )
        }
    )
    public String execute(){
        result = new UploadXmlResult();
        String xml;
        try {
            xml = FileUtils.readFileToString(xmlfile);
        }catch (IOException e){
            e.printStackTrace();
//            result = null;
            return SUCCESS;
        }
        result.setXml(xml);

        XmlLoader loader = new XmlLoader();
        try {
            result.setXmldoc(loader.loadString(xml));
        }catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return SUCCESS;

    }

    public UploadXmlResult getResult() { return result; }
    public void setResult(UploadXmlResult result) { this.result = result; }

    public File getXmlfile() { return xmlfile; }
    public void setXmlfile(File xmlfile) { this.xmlfile = xmlfile; }

    public class UploadXmlResult{
        private WORKFLOWAPP xmldoc;
        private String xml;

        public WORKFLOWAPP getXmldoc() { return xmldoc; }
        public void setXmldoc(WORKFLOWAPP xmldoc) { this.xmldoc = xmldoc; }
        public String getXml() { return xml; }
        public void setXml(String xml) { this.xml = xml; }
        
    }
}
