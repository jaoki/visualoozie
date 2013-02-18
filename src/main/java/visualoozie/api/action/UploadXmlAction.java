package visualoozie.api.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.xml.sax.SAXParseException;

import visualoozie.page.action.PathConstants;
import visualoozie.util.XmlLoader;
import visualoozie.xsd.Workflow02Parser;
import visualoozie.xsd.Workflow04Parser;
import visualoozie.xsd.workflow04.ACTION;
import visualoozie.xsd.workflow04.KILL;

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

        String rawXml;
        try {
            rawXml = FileUtils.readFileToString(xmlfile);
        }catch (IOException e){
            e.printStackTrace();
            result.succeeded = false;
            result.errorMessage = e.getMessage();
            return SUCCESS;
        }

        Scanner scanner = new Scanner(rawXml);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            lines.add(line);
        }
        result.xml = lines.toArray(new String[0]);
        
        // find xmlns to identify a version for the oozie xsd
        Pattern xmlnsPattern = Pattern.compile("workflow-app *xmlns *= *\"(.*?)\"");
        Matcher m = xmlnsPattern.matcher(rawXml);
        String xmlns = null;
        while(m.find()){
        	xmlns = m.group(1);
        }

		result.setIdentifiedNamespace(xmlns);
        List<WorkflowNode> nodes;
        try {
        	if("uri:oozie:workflow:0.2".equals(xmlns)){
		        nodes = new Workflow02Parser().parse(rawXml);
        	}else{
		        nodes = new Workflow04Parser().parse(rawXml);
        	}
        }catch (JAXBException e) {
            // TODO Auto-generated catch block
            result.succeeded = false;
            if(e.getLinkedException() instanceof SAXParseException){
                SAXParseException e2 = (SAXParseException) e.getLinkedException();
                result.lineNumber = e2.getLineNumber();
                result.columnNumber = e2.getColumnNumber();
                result.errorMessage = e2.getMessage();

            }else{
                e.printStackTrace();
                result.errorMessage = e.getMessage();
            }
            return SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            result.succeeded = false;
            result.errorMessage = e.getMessage();
            return SUCCESS;
        }

        result.setNodes(nodes);
        result.succeeded = true;

        return SUCCESS;

    }


    public UploadXmlResult getResult() { return result; }
    public void setResult(UploadXmlResult result) { this.result = result; }

    public File getXmlfile() { return xmlfile; }
    public void setXmlfile(File xmlfile) { this.xmlfile = xmlfile; }

    public class UploadXmlResult{
        private boolean succeeded;
        private String errorMessage;
        private Integer lineNumber;
        private Integer columnNumber;

        private List<WorkflowNode> nodes;
        private String[] xml;
        private String identifiedNamespace;

        public boolean isSucceeded() { return succeeded; }
        public void setSucceeded(boolean succeeded) { this.succeeded = succeeded; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

        public Integer getColumnNumber() { return columnNumber; }
        public void setColumnNumber(Integer columnNumber) { this.columnNumber = columnNumber; }

        public List<WorkflowNode> getNodes() { return nodes; }
        public void setNodes(List<WorkflowNode> nodes) { this.nodes = nodes; }

		public String[] getXml() { return xml; }
		public void setXml(String[] xml) { this.xml = xml; }

		public String getIdentifiedNamespace() { return identifiedNamespace; }
		public void setIdentifiedNamespace(String identifiedNamespace) { this.identifiedNamespace = identifiedNamespace; }

    }
}
