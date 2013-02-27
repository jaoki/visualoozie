package visualoozie.xsd;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import visualoozie.api.action.WorkflowNode;
import visualoozie.xsd.workflow02.ACTION;
import visualoozie.xsd.workflow02.CASE;
import visualoozie.xsd.workflow02.DECISION;
import visualoozie.xsd.workflow02.FORK;
import visualoozie.xsd.workflow02.FORKTRANSITION;
import visualoozie.xsd.workflow02.JOIN;
import visualoozie.xsd.workflow02.KILL;
import visualoozie.xsd.workflow02.WORKFLOWAPP;

public class Workflow02Parser {
    private Unmarshaller unmarshaller = null;

    // TODO ThreadLocal
	public Workflow02Parser(){
		
        try {
            JAXBContext context = JAXBContext.newInstance(WORKFLOWAPP.class);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ClassLoader classLoader = Workflow025Parser.class.getClassLoader();
        List<StreamSource> sources = new ArrayList<StreamSource>();

        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/distcp-action-0.1.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/distcp-action-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/email-action-0.1.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/gms-oozie-sla-0.1.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/hive-action-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/hive-action-0.3.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/hive-action-0.4.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/oozie-sla-0.1.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/oozie-workflow-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/shell-action-0.1.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/shell-action-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/shell-action-0.3.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/sqoop-action-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/sqoop-action-0.3.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/sqoop-action-0.4.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/ssh-action-0.1.xsd")));

        try {
        	Schema schema = sf.newSchema(sources.toArray(new StreamSource[0]));
            unmarshaller.setSchema(schema);
        } catch (SAXException e) {
            e.printStackTrace();
        }

	}
	
	public List<WorkflowNode> parse(String rawXml) throws JAXBException {
	
        ByteArrayInputStream input = new ByteArrayInputStream(rawXml.getBytes());
        @SuppressWarnings("unchecked")
        WORKFLOWAPP xmldoc = ((JAXBElement<WORKFLOWAPP>)unmarshaller.unmarshal(input)).getValue();

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
            } else if(nodeXml instanceof FORK){
                FORK fork = (FORK)nodeXml;
                node = new WorkflowNode();
                node.setName(fork.getName());
                node.setType(WorkflowNode.NodeType.FORK);
                List<String> tos = new ArrayList<>();
                for(FORKTRANSITION fork1 : fork.getPath()){
                	tos.add(fork1.getStart());
                }
                node.setTo(tos.toArray(new String[0]));
                nodes.add(node);
            } else if(nodeXml instanceof JOIN){
                JOIN join = (JOIN)nodeXml;
                node = new WorkflowNode();
                node.setName(join.getName());
                node.setType(WorkflowNode.NodeType.JOIN);
                node.setTo(new String[]{join.getTo()});
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
