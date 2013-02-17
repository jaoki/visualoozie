package visualoozie.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

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
            URL url = classLoader.getResource("oozie/oozie-workflow-0.4.xsd");
            Schema schema = sf.newSchema(url);
            unmarshaller.setSchema(schema);
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

    public WORKFLOWAPP loadStringArray(String[] xml) throws JAXBException {
        StringBuffer sb = new StringBuffer();
        for(String part : xml){
            sb.append(part);
        }

        return loadString(sb.toString());
    }

}

