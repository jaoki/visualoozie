package visualoozie.xsd;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import visualoozie.api.action.WorkflowNode;

public class Workflow04ParserTest {
	
	@Test
	public void shell03() throws IOException, JAXBException{
		String workflowXml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("oozie/shell/workflow.xml"));
		Workflow04Parser parser = new Workflow04Parser();
		List<WorkflowNode> nodes = parser.parse(workflowXml);
		Assert.assertEquals(nodes.get(0).getTo()[0], "shell-node");
		
	}

}
