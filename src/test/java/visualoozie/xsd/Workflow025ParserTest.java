package visualoozie.xsd;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import visualoozie.api.action.WorkflowNode;

public class Workflow025ParserTest {
	
	@Test
	public void test() throws IOException, JAXBException{
		String workflowXml = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("oozie/demo/workflow025.xml"));
		Workflow025Parser parser = new Workflow025Parser();
		List<WorkflowNode> nodes = parser.parse(workflowXml);
		Assert.assertEquals(nodes.get(0).getTo()[0], "cleanup-node");
		Assert.assertEquals(nodes.get(1).getTo()[0], "fork-node");
		Assert.assertEquals(nodes.get(1).getTo()[1], "fail");
		
	}

}
