/**
 * Copyright (c) 2013, Yahoo! Inc.  All rights reserved.
 * Copyrights licensed under the New BSD License. See the accompanying LICENSE file for terms
 */

package visualoozie.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXParseException;

import visualoozie.xsd.Workflow01Parser;
import visualoozie.xsd.Workflow025Parser;
import visualoozie.xsd.Workflow02Parser;
import visualoozie.xsd.Workflow03Parser;
import visualoozie.xsd.Workflow04Parser;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/upload_xml")
public class UploadXmlAction{

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({ MediaType.APPLICATION_JSON })
    public UploadXmlResult postFile(
    		@FormDataParam("xmlfile") InputStream is,
    		@FormDataParam("xmlfile") FormDataContentDisposition fileDetail){

        String rawXml;
        try {
            rawXml = IOUtils.toString(is);
        }catch (IOException e){
		    UploadXmlResult result = new UploadXmlResult();
            e.printStackTrace();
            result.succeeded = false;
            result.errorMessage = e.getMessage();
            return result;
        }

        return generateResult(rawXml);

    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Consumes(MediaType.TEXT_HTML)
    @Produces({ MediaType.APPLICATION_JSON })
    public UploadXmlResult postText( @FormParam("xmltext") String rawXml ){

//        String rawXml;
//        try {
//            rawXml = IOUtils.toString(is);
//        }catch (IOException e){
//		    UploadXmlResult result = new UploadXmlResult();
//            e.printStackTrace();
//            result.succeeded = false;
//            result.errorMessage = e.getMessage();
//            return result;
//        }

        return generateResult(rawXml);

    }

	private UploadXmlResult generateResult(String rawXml) {
		UploadXmlResult result = new UploadXmlResult();

		Scanner scanner = new Scanner(rawXml);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            lines.add(line);
        }
        result.xml = lines.toArray(new String[0]);
        
        // find xmlns to identify a version for the oozie xsd
        Pattern xmlnsPattern = Pattern.compile("workflow-app *xmlns *= *['|\"](.*?)['|\"]"); // TODO this is not good enough. e.g. when xmlns is on a line after.
        Matcher m = xmlnsPattern.matcher(rawXml);
        String xmlns = null;
        while(m.find()){
        	xmlns = m.group(1);
        }

		result.setIdentifiedNamespace(xmlns);
        List<WorkflowNode> nodes;
        try {
        	if("uri:oozie:workflow:0.1".equals(xmlns)){
		        nodes = new Workflow01Parser().parse(rawXml);
        	}else if("uri:oozie:workflow:0.2".equals(xmlns)){
		        nodes = new Workflow02Parser().parse(rawXml);
        	}else if("uri:oozie:workflow:0.2.5".equals(xmlns)){
		        nodes = new Workflow025Parser().parse(rawXml);
        	}else if("uri:oozie:workflow:0.3".equals(xmlns)){
		        nodes = new Workflow03Parser().parse(rawXml);
        	}else if("uri:oozie:workflow:0.4".equals(xmlns)){
		        nodes = new Workflow04Parser().parse(rawXml);
        	}else{
		        nodes = new Workflow04Parser().parse(rawXml);
        	}
        }catch (JAXBException e) {
            // TODO Auto-generated catch block
            result.succeeded = false;
            if(e.getLinkedException() instanceof SAXParseException){
                SAXParseException e2 = (SAXParseException) e.getLinkedException();
                result.lineNumber = e2.getLineNumber();
                result.columnNumber = e2.getColumnNumber();
                result.errorMessage = e2.getMessage();

            }else{
                e.printStackTrace();
                result.errorMessage = e.getMessage();
            }
            return result;
        }catch (Exception e) {
            e.printStackTrace();
            result.succeeded = false;
            result.errorMessage = e.getMessage();
            return result;
        }

        result.setNodes(nodes);
        result.succeeded = true;
        return result;
	}

    @XmlRootElement
    public static class UploadXmlResult{
        private boolean succeeded;
        private String errorMessage;
        private Integer lineNumber;
        private Integer columnNumber;

        private List<WorkflowNode> nodes;
        private String[] xml;
        private String identifiedNamespace;

        public boolean isSucceeded() { return succeeded; }
        public void setSucceeded(boolean succeeded) { this.succeeded = succeeded; }

        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

        public Integer getLineNumber() { return lineNumber; }
        public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

        public Integer getColumnNumber() { return columnNumber; }
        public void setColumnNumber(Integer columnNumber) { this.columnNumber = columnNumber; }

        public List<WorkflowNode> getNodes() { return nodes; }
        public void setNodes(List<WorkflowNode> nodes) { this.nodes = nodes; }

		public String[] getXml() { return xml; }
		public void setXml(String[] xml) { this.xml = xml; }

		public String getIdentifiedNamespace() { return identifiedNamespace; }
		public void setIdentifiedNamespace(String identifiedNamespace) { this.identifiedNamespace = identifiedNamespace; }

    }
}
