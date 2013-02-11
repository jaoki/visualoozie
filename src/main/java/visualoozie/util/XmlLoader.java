package visualoozie.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

//import org.apache.oozie.tools.workflowgenerator.xsd04.WORKFLOWAPP;
import org.xml.sax.SAXException;

import visualoozie.xsd.WORKFLOWAPP;

public class XmlLoader {
    private Unmarshaller unmarshaller = null;

    public XmlLoader(){
        try {
            JAXBContext context = JAXBContext.newInstance("visualoozie.xsd");
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            ClassLoader classLoader = XmlLoader.class.getClassLoader();
            URL url;
            try {
                // TODO this should be from classloader
                url = new File("src/main/xsd/oozie-workflow-0.4.xsd").toURI().toURL();
                Schema schema = sf.newSchema(url);
                unmarshaller.setSchema(schema);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public WORKFLOWAPP loadString(String xml) throws JAXBException {
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());

        @SuppressWarnings("unchecked")
        JAXBElement<WORKFLOWAPP> element = (JAXBElement<WORKFLOWAPP>)unmarshaller.unmarshal(input);
        return element.getValue();
    }

}

