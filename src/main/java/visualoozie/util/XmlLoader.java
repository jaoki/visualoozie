package visualoozie.util;

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

import visualoozie.xsd.shell03.ACTION;

public class XmlLoader {
    private Unmarshaller unmarshaller = null;

    public XmlLoader(){
        try {
            JAXBContext context = JAXBContext.newInstance(
        		visualoozie.xsd.workflow02.WORKFLOWAPP.class
        		, visualoozie.xsd.workflow03.WORKFLOWAPP.class
        		, visualoozie.xsd.workflow04.WORKFLOWAPP.class
        		, ACTION.class
    		);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        ClassLoader classLoader = XmlLoader.class.getClassLoader();
        List<StreamSource> sources = new ArrayList<StreamSource>();

        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/oozie-workflow-0.2.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/oozie-workflow-0.3.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/oozie-workflow-0.4.xsd")));
        sources.add(new StreamSource(classLoader.getResourceAsStream("oozie/shell-action-0.3.xsd")));

        try {
        	Schema schema = sf.newSchema(sources.toArray(new StreamSource[0]));
            unmarshaller.setSchema(schema);
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    public visualoozie.xsd.workflow02.WORKFLOWAPP loadWorkflow02(String xml) throws JAXBException {
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());

        @SuppressWarnings("unchecked")
        JAXBElement<visualoozie.xsd.workflow02.WORKFLOWAPP> element = (JAXBElement<visualoozie.xsd.workflow02.WORKFLOWAPP>)unmarshaller.unmarshal(input);
        return element.getValue();
    }

    public visualoozie.xsd.workflow03.WORKFLOWAPP loadWorkflow03(String xml) throws JAXBException {
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());

        @SuppressWarnings("unchecked")
        JAXBElement<visualoozie.xsd.workflow03.WORKFLOWAPP> element = (JAXBElement<visualoozie.xsd.workflow03.WORKFLOWAPP>)unmarshaller.unmarshal(input);
        return element.getValue();
    }

    public visualoozie.xsd.workflow04.WORKFLOWAPP loadWorkflow04(String xml) throws JAXBException {
        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes());

        @SuppressWarnings("unchecked")
        JAXBElement<visualoozie.xsd.workflow04.WORKFLOWAPP> element = (JAXBElement<visualoozie.xsd.workflow04.WORKFLOWAPP>)unmarshaller.unmarshal(input);
        return element.getValue();
    }

//    public visualoozie.xsd.workflow04.WORKFLOWAPP loadStringArray(String[] xml) throws JAXBException {
//        StringBuffer sb = new StringBuffer();
//        for(String part : xml){
//            sb.append(part);
//        }
//
//        return loadString(sb.toString());
//    }

}

