package visualoozie.xsd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import visualoozie.api.action.WorkflowNode;
import visualoozie.util.XmlLoader;
import visualoozie.xsd.workflow04.ACTION;
import visualoozie.xsd.workflow04.KILL;
import visualoozie.xsd.workflow04.WORKFLOWAPP;

public class Workflow04Parser {
	public List<WorkflowNode> parse(String rawXml) throws JAXBException {
        XmlLoader loader = new XmlLoader();
        WORKFLOWAPP xmldoc = loader.loadWorkflow04(rawXml);
		
		List<WorkflowNode> nodes = new ArrayList<>();
        WorkflowNode node = new WorkflowNode();
        node.setName("start");
        node.setType(WorkflowNode.NodeType.START);
        node.setTo(new String[]{xmldoc.getStart().getTo()});
        nodes.add(node);

        for(Object nodeXml : xmldoc.getDecisionOrForkOrJoin()){
        	// TODO add more handling here.
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
		return nodes;
	}

}
