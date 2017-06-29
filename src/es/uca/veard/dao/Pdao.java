/*
 * Pdao.java
 * v0.8
 * 28/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
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
import java.io.PrintWriter;
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

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

/**
 * Class that implements all filesystem operations.
 * It acts as a Data Access Object (DAO) for the operations performed by the REST {@link Rest} class.
 *
 * @author Luis J. Quintana B.
 */
public class Pdao {
    
    //Base application directory
    private static final String BASE_PATH = System.getProperty("user.home")+"/usercontent/";
    //Default log file name
    private static final String LOG_NAME = "log";
    
    /*
     * LOG methods
     *******************************************************/
    
    /**
     * Returns the contents of the default {@link LOG_NAME} log file
     *
     * @return  a string with the contents of the log file
     */
    static public String getLog (){
        return getLog(LOG_NAME);
    }
    /**
     * Returns the contents of the specified log file
     *
     * @param logName   the name of the log file
     * @return          a string with the contents of the log file
     */
    static public String getLog(String logName){
        return deserializeString(new File(BASE_PATH+logName+".log"));
    }
    /*
     * Registers a message in the default {@link LOG_NAME} log file
     *
     * @param message   The message to register
     */
    static public void postLog(String message){
        postLog(message,LOG_NAME);
    }
    /*
     * Registers a message in the specified log file
     *
     * @param message   The message to register
     * @param logName   The name of the log file
     */
    static public void postLog(String message, String logName){
        
        //Make sure the file exists
        File file = new File(BASE_PATH+logName+".log");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try{
                file.createNewFile();
            } catch (IOException e) {}
        }
        //Declare the file buffers
        BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			
            //Open the buffers
			fw = new FileWriter(file.getAbsoluteFile(), true);// true = append file
			bw = new BufferedWriter(fw);
            
            //Register the message in the log file
			bw.append(new SimpleDateFormat("\n[yyyy/MM/dd HH:mm:ss] ").format(new Date()) + message);
            
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            //Close the buffers
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
    /**
     * Clears the contents of the specified log file
     * @param logName   specified log file
     */
    static public void clearLog(String logName){
        try{
            new PrintWriter(BASE_PATH+logName+".log").close();
        }catch(Exception e){
            postLog("clearLog: failed on "+BASE_PATH+logName+".log\n"+e+"\n");
        }
    }
    
    /*
     * SAVE methods
     *******************************************************/
    /**
     * Saves a file from an InputStream in the specified path
     * @param uploadedInputStream   the InputStream to be saved as a file
     * @param path                  the path to save the file in
     * @return                      a bool set true if there's no error, false otherwise
     */
    static public boolean saveInputStream(InputStream uploadedInputStream, String path){
        OutputStream out = null;
        try{
            int read = 0;
            byte[] bytes = new byte[1024];
            out = new FileOutputStream(createFile(BASE_PATH+path));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }  
        } catch (IOException e) {
            postLog("saveInputStream: failed writting on "+BASE_PATH+path+"\n"+e+"\n");
			return false;
		} finally {
            if(uploadedInputStream != null){
                try{
                    uploadedInputStream.close();
                } catch (IOException e){
                    postLog("saveInputStream: failed closing stream on "+BASE_PATH+path+"\n"+e+"\n");
                }
            }
            if (out!=null){
                try{
                    out.flush();
                    out.close();
                } catch (IOException e){
                    postLog("saveInputStream: failed closing buffer on "+BASE_PATH+path+"\n"+e+"\n");
                }
            }
        }
        return true; 
    }
    /**
     * Saves a file from a string in the specified path
     * @param contents  Content string to be saved
     * @param path      path of the file
     * @return          a bool set to true if there's no error, false otherwise
     */
    static public boolean saveString(String content, String path){
        File file = createFile(BASE_PATH+path);
        //Declare the file buffers
        BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			
            //Open the buffers
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
            
            //Write the content to the file
            //postLog("saveString: Writting: '"+content+"' in path '"+BASE_PATH+path+"'.");
			bw.write(content);
            
		} catch (IOException e) {
            postLog("saveString: failed writting on "+BASE_PATH+path+"\n"+e+"\n");
            return false;
		} finally {
            //Close the buffers
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
                
			} catch (IOException e) {
                postLog("saveInputStream: failed closing buffer on "+BASE_PATH+path+"\n"+e+"\n");
                return false;
			}
		}
        return true;
    }
    
    /*
     * LOAD methods
     *******************************************************/
    /**
     * Returns the contents of an specified file
     * @param path  relative path of the file
     * @return      string with the contents of the file
     */
    static public String loadPlainText(String path){
        //TODO: throw custom exception if the file doesn't exists
        return deserializeString(new File(BASE_PATH+path));
    }
    /**
     * Returns a specified File
     * @param path  relative path of the file
     * @return      the specified File
     */
    static public File loadFile(String path){
        //TODO: Control if the file exists instead of creating it
        return createFile(BASE_PATH+path);
    }
    /**
     * Returns a list of all files within the specified directory
     * @param path  directory path
     * @return      list of all file names in the directory
     */
    static public List<String> listAll(String path){
        return listType(path,"");
    }
    /**
     * Returns a list of all files with the specified ending within the specified directory
     * @param path  directory path
     * @param type  File ending
     * @return      list of all matching file names in the directory
     */
    static public List<String> listType(String path, String type){
        File dir = new File(BASE_PATH+path);
		File[] directoryListing = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(type);
		    }
		});
		
		List<String> names = new ArrayList<String>();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String name=child.getName();
				names.add(name.substring(0,name.length()-type.length()));
		    }
		}
		return names;
    }
    /**
     * Returns the name of a certain project
     * @param path  path of the project file
     * @return      string with the name of the project
     */
    static public String getName(String path){
        
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
        
		String name = "Unnamed";
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(createFile(BASE_PATH+path));
            document.getDocumentElement().normalize();

            NodeList list = document.getElementsByTagName("pname");
	        if (list != null && list.getLength() > 0) {
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	                 name = (String) subList.item(0).getTextContent();
	            }
	        }else{
                postLog("getName: <pname> not found on "+BASE_PATH+path);
            }
		} catch (Exception e) {
            postLog("getName: DocumentBuilder failed on "+BASE_PATH+path+"\n"+e+"\n");
	    }
        postLog("Requested name of the project on path "+path+": "+name);
		return name;
	}
    /**
     * Returns the description of a certain project
     * @param path  path of the project file
     * @return      string with the description of the project
     */
    static public String getDescription(String path){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		String desc = "Null";
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(createFile(BASE_PATH+path));
			document.getDocumentElement().normalize();
			
			NodeList list = document.getElementsByTagName("pdesc");
	        if (list != null && list.getLength() > 0) {
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	                 desc = (String) subList.item(0).getNodeValue();
	            }
	        }else{
                postLog("getDescription: <pdesc> not found on "+BASE_PATH+path);
            }
		} catch (Exception e) {
            postLog("getDescription: DocumentBuilder failed on "+BASE_PATH+path+"\n"+e+"\n");
	    }
        postLog("Requested description of the project on path "+path+": "+desc);
		return desc;
	}
    /**
     * Returns the java code of a certain project
     * @param path  path of the project file
     * @return      string with the java code of the project
     */
    static public String getJavaCode(String path){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		String jcode = "Null";
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(createFile(BASE_PATH+path));
			document.getDocumentElement().normalize();
			
			NodeList list = document.getElementsByTagName("jcode");
	        if (list != null && list.getLength() > 0) {
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	                 jcode = (String) subList.item(0).getNodeValue();
	            }
	        }else{
                postLog("getJavaCode: <jcode> not found on "+BASE_PATH+path);
            }
		} catch (Exception e) {
            postLog("getJavaCode: DocumentBuilder failed on "+BASE_PATH+path+"\n"+e+"\n");
	    }
        postLog("Requested java code of the project on path "+path+": "+jcode);
		return jcode;
	}
    
    /*
     * HELPER methods
     *******************************************************/
    /**
     * Creates a {@link File} object safely.
     * Creates the necesary folder structure if necessary.
     * @param path  The complete file path
     * @return      The created File object
     */
    static public File createFile(String path){
        File newFile = new File(path);
        if(!newFile.exists()){
            postLog("Created new file "+path);
            newFile.getParentFile().mkdirs();
            try{
                newFile.createNewFile();
            } catch (IOException e) {}
        } else {
            postLog("File already exists at "+path);
        }
        return newFile;
    }
    /**
     * Creates a string with the contents of the specified file
     * @param file  a File object to be deserialized
     * @return      a string with the contents of the File object
     */
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
              }
          }catch(Exception e){}
	      return buffer.toString();
	  }
    
}