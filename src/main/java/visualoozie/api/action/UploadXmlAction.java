package visualoozie.api.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import visualoozie.page.action.PathConstants;
import visualoozie.util.XmlLoader;
import visualoozie.xsd.ACTION;
import visualoozie.xsd.KILL;
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
        WORKFLOWAPP xmldoc;
        try {
            xmldoc = loader.loadString(xml);
        }catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

        return SUCCESS;

    }

    public UploadXmlResult getResult() { return result; }
    public void setResult(UploadXmlResult result) { this.result = result; }

    public File getXmlfile() { return xmlfile; }
    public void setXmlfile(File xmlfile) { this.xmlfile = xmlfile; }

    public class UploadXmlResult{
        private List<WorkflowNode> nodes;
        private String xml;

        public List<WorkflowNode> getNodes() { return nodes; }
        public void setNodes(List<WorkflowNode> nodes) { this.nodes = nodes; }
        
        public String getXml() { return xml; }
        public void setXml(String xml) { this.xml = xml; }

    }
}
