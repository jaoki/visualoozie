package visualoozie.api.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
import visualoozie.xsd.workflow04.ACTION;
import visualoozie.xsd.workflow04.KILL;
import visualoozie.xsd.workflow04.WORKFLOWAPP;

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

        Scanner scanner;
        try {
            scanner = new Scanner(xmlfile);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
            result.succeeded = false;
            result.errorMessage = e.getMessage();
            return SUCCESS;
        }
        
        String rawXml;
        try {
            rawXml = FileUtils.readFileToString(xmlfile);
        }catch (IOException e){
            e.printStackTrace();
            return SUCCESS;
        }
//        result.setEscapedXml(StringEscapeUtils.escapeHtml(rawXml));
        
        List<String> lines = new ArrayList<>();
        StringBuffer xmlSB = new StringBuffer();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
//            lines.add(StringEscapeUtils.escapeHtml(line));
            lines.add(line);
            xmlSB.append(line);
        }
//        result.escapedXml = lines.toArray(new String[0]);
        result.xml = lines.toArray(new String[0]);

        XmlLoader loader = new XmlLoader();
        WORKFLOWAPP xmldoc;
        try {
            xmldoc = loader.loadString(rawXml);
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
        }

        // Parse uri:oozie:workflow:0.4 to WorkflowNode
        List<WorkflowNode> nodes = new ArrayList<>();
        WorkflowNode node = new WorkflowNode();
        node.setName("start");
        node.setType(WorkflowNode.NodeType.START);
        node.setTo(new String[]{xmldoc.getStart().getTo()});
        nodes.add(node);

        for(Object nodeXml : xmldoc.getDecisionOrForkOrJoin()){
            if(nodeXml instanceof ACTION){
                ACTION action = (ACTION)nodeXml;
                node = new WorkflowNode();
                node.setName(action.getName());
                node.setType(WorkflowNode.NodeType.ACTION);
                node.setTo(new String[]{action.getOk().getTo(), action.getError().getTo()});
                nodes.add(node);
            } else if(nodeXml instanceof KILL){
                KILL kill = (KILL)nodeXml;
                node = new WorkflowNode();
                node.setName(kill.getName());
                node.setType(WorkflowNode.NodeType.KILL);
                node.setTo(new String[]{});
                nodes.add(node);
            }

        }
        
        node = new WorkflowNode();
        node.setName(xmldoc.getEnd().getName());
        node.setType(WorkflowNode.NodeType.END);
        node.setTo(new String[]{});
        nodes.add(node);
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
        
    }
}
