package visualoozie;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.bind.JAXBException;

import org.testng.Assert;
import org.testng.annotations.Test;

import visualoozie.page.action.VisualizeXmlAction;

public class VersionNumberTest {
	
	@Test
	public void test() throws IOException, JAXBException{
    	InputStream is = VisualizeXmlAction.class.getClassLoader().getResourceAsStream("visualoozie_version.properties");
    	Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e){
			Assert.fail(e.getMessage());
		}
		Assert.assertNotNull(properties.get("visualoozie.version"));

	}

}
