package es.uca.veard.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.nio.file.Files;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Class that implements all filesystem operations
 */
public class Pdao {
    
    //TODO: change all references to System... for this variable;
    //Base application directory
    private static String basePath = System.getProperty("user.home")+"/usercontent/";
    
    /*
     * Function to register a message in a log file
     */
    static public void postLog(String logName, String message){
        
        //Make sure the file exists
        File file = createFile(basePath+logName+".log");
        
        BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			
			fw = new FileWriter(file.getAbsoluteFile(), true);// true = append file
			bw = new BufferedWriter(fw);
            
            //Register the message in the log file
			bw.append("\n[" +
                      new SimpleDateFormat("yyyy.MM.dd HH/mm/ss").format(new Date()) +
                      "] " + message);
            
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
                
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    /*
     * Function that return the contents of the requested log file
     */
    static public String getLog(String logName){
        return deserializeString(new File(basePath+logName+".log"));
    }
    
    static public String saveTest(String path,String name){
        String fullPath = System.getProperty("user.home")+path+name+".xml";
        File myXMLFile = createFile(fullPath);

        return fullPath;
    }
    /*
    static public boolean uploadTest(File file, String path){
        File savedFile = createFile(System.getProperty("user.home")+path);
        //FileChannel source = null;
        //FileChannel destination = null;
        try{
            Files.copy( file.toPath(), savedFile.toPath() );
            //source = new FileInputStream(file).getChannel();
            //destination = new FileOutputStream(savedFile).getChannel();
            //destination.transferFrom(source, 0, source.size());
        } catch (IOException e){
            return false;
        }
        finally{
            /*if(source!=null){
                source.close();
            }
            if(destination!=null){
                destination.close();
            }*//*
            return true;
        }
    }*/
    
    static public boolean uploadTest(InputStream uploadedInputStream, String path){
        OutputStream out = null;
        try{
            int read = 0;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(new File(System.getProperty("user.home")+path));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }  
        } catch (IOException e) {
			return false;
		} finally {
            if(uploadedInputStream != null){
                try{
                    uploadedInputStream.close();
                } catch (IOException e){
                }
            }
            if (out!=null){
                try{
                    out.flush();
                    out.close();
                } catch (IOException e){
                }
            }
        }
        return true;
        
    }
    
	static public boolean save(String name, String description, String ecode, String jcode){
		//int fname = new File(System.getProperty("user.home")+"/usercontent/").list().length; // Temporal naming function
		File myXMLFile = new File(System.getProperty("user.home")+"/userdata/", /*f*/name+".xml");  //or "user.home" 
		File myJSFile = new File(System.getProperty("user.home")+"/userdata/", /*f*/name+".js");
		//System.out.print("Yep! "+System.getProperty("user.home")+" -->"+code);
		try {
			myXMLFile.createNewFile();
			FileWriter fw = new FileWriter(myXMLFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(ecode.substring(0, ecode.length()-6)+"<pname>"+name+"</pname><pdesc>"+description+"</pdesc></xml>");
			bw.close();
			
			myJSFile.createNewFile();
			fw = new FileWriter(myJSFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			bw.write(jcode);
			bw.close();
		} catch (IOException e) {}
		return true;
	}
	
	public static String deserializeString(File file){
		
	      int len;
	      char[] chr = new char[4096];
	      final StringBuffer buffer = new StringBuffer();
	      try {
	      final FileReader reader = new FileReader(file);
	      try {
	          while ((len = reader.read(chr)) > 0) {
	              buffer.append(chr, 0, len);
	          }
	      } finally {
	          reader.close();
	      }}catch(Exception e){}
	      return buffer.toString();
		
	  }
	
	static public String load(String name){
		return deserializeString(new File(System.getProperty("user.home")+"/userdata/", name+".js"));
		
	}
	
	static public List<String> listAll(){
		File dir = new File(System.getProperty("user.home")+"/userdata/");
		File[] directoryListing = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".xml");
		    }
		});
		
		List<String> names = new ArrayList<String>();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String name=child.getName();
				names.add(name.substring(0,name.length()-4));
				/*
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				try{
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(child);
					Element rootElement = document.getDocumentElement();
					
					NodeList list = rootElement.getElementsByTagName("pname");
			        if (list != null && list.getLength() > 0) {
			        	//System.out.print("FOUND IT!!!!!");
			            NodeList subList = list.item(0).getChildNodes();

			            if (subList != null && subList.getLength() > 0) {
			            	//System.out.print("DA FUCK?>>>>>>>>>>>>>>>"+(String) subList.item(0).getNodeValue()+"<<<<<<<<<<<<<<\n");
			                names.add( (String) subList.item(0).getNodeValue());
			            }
			        }
					
					//XPath xPath = XPathFactory.newInstance().newXPath();

					//Node node = (Node) xPath.evaluate("//pname", document, XPathConstants.NODE);
					//names.add((String) xPath.evaluate("/pname", document, XPathConstants.STRING));//node.getNodeValue());
					//System.out.print(node.getNodeValue()+"\n <------VALUE");
			    } catch (Exception e) {
			    	e.printStackTrace();
			    }*/
		    }
		}
		return names;
	}
	static public String getName(String project){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		String name = "Null";
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(System.getProperty("user.home")+"/userdata/"+project+".xml"));
			Element rootElement = document.getDocumentElement();
			
			NodeList list = rootElement.getElementsByTagName("pname");
	        if (list != null && list.getLength() > 0) {
	        	//System.out.print("FOUND IT!!!!!");
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	            	//System.out.print("DA FUCK?>>>>>>>>>>>>>>>"+(String) subList.item(0).getNodeValue()+"<<<<<<<<<<<<<<\n");
	                 name = (String) subList.item(0).getNodeValue();
	            }
	        }
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
		return name;
	}
	static public String getDescription(String project){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		String desc = "Null";
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(System.getProperty("user.home")+"/userdata/"+project+".xml"));
			Element rootElement = document.getDocumentElement();
			
			NodeList list = rootElement.getElementsByTagName("pdesc");
	        if (list != null && list.getLength() > 0) {
	        	//System.out.print("FOUND IT!!!!!");
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	            	//System.out.print("DA FUCK?>>>>>>>>>>>>>>>"+(String) subList.item(0).getNodeValue()+"<<<<<<<<<<<<<<\n");
	                 desc = (String) subList.item(0).getNodeValue();
	            }
	        }
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
		return desc;
	}
    
    static public File createFile(String path){
        File newFile = new File(path);
        if(!newFile.exists()){
            newFile.getParentFile().mkdirs();
            try{
                newFile.createNewFile();
            } catch (IOException e) {}
        }
        return newFile;
    }
}