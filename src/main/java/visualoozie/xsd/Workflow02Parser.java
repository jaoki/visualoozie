package visualoozie.xsd;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import visualoozie.api.action.WorkflowNode;
import visualoozie.util.XmlLoader;
import visualoozie.xsd.workflow02.ACTION;
import visualoozie.xsd.workflow02.CASE;
import visualoozie.xsd.workflow02.DECISION;
import visualoozie.xsd.workflow02.KILL;
import visualoozie.xsd.workflow02.WORKFLOWAPP;

public class Workflow02Parser {
	public List<WorkflowNode> parse(String rawXml) throws JAXBException {
        XmlLoader loader = new XmlLoader();
        WORKFLOWAPP xmldoc = loader.loadWorkflow02(rawXml);
		
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
                node.setTo(new String[]{
                		action.getOk().getTo()
                		, action.getError().getTo()
        		});
                nodes.add(node);
            } else if(nodeXml instanceof KILL){
                KILL kill = (KILL)nodeXml;
                node = new WorkflowNode();
                node.setName(kill.getName());
                node.setType(WorkflowNode.NodeType.KILL);
                node.setTo(new String[]{});
                nodes.add(node);
            } else if(nodeXml instanceof DECISION){
                DECISION decision = (DECISION)nodeXml;
                node = new WorkflowNode();
                node.setName(decision.getName());
                node.setType(WorkflowNode.NodeType.DECISION);
                List<String> tos = new ArrayList<>();
                tos.add(decision.getSwitch().getDefault().getTo());
                for (CASE acase : decision.getSwitch().getCase()) {
                	tos.add(acase.getTo());
				}
                node.setTo(tos.toArray(new String[0]));
                nodes.add(node);
            }else{
            	throw new UnsupportedOperationException("Unspported node instance(" + nodeXml + ")");
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
