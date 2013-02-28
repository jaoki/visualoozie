package visualoozie.xsd;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import visualoozie.api.WorkflowNode;

public class Workflow03ParserTest {
	
	@Test
	public void distcp() throws IOException, JAXBException{
		String workflowXml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("oozie/distcp/workflow.xml"));
		Workflow03Parser parser = new Workflow03Parser();
		List<WorkflowNode> nodes = parser.parse(workflowXml);
		Assert.assertEquals(nodes.get(0).getTo()[0], "distcp-node");
		
	}

}
