/*
 * Rest.java
 * v0.8
 * 28/07/2017
 * Copyright (c) Luis J. Quintana B.
 */
package es.uca.veard.rest;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.annotation.PostConstruct;
import javax.annotation.*;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import es.uca.veard.dao.Pdao;

/**
 * Class that implements all REST methods.
 * It uses the DAO {@link Pdao} class as an interface with the filesystem.
 *
 * @author Luis J. Quintana B.
 */
@Path("/dao")
public class Rest extends HttpServlet {
    
    //TODO: eliminate from Web.xml
    private static final String MOD_PATH = "models/";
    private static final String IMG_PATH = "images/";
    private static final String PRO_PATH = "projects/";
    private static final String LOG_NAME = "log";
    
    /*
     * GET methods
     *****************************************************/
    /*
     * Tests methods
     */
        /**
         * Simple hello test.
         * @returns plain text message
         */
        @GET
        @Path("/hello")
        @Produces(MediaType.TEXT_PLAIN)
        public String sayPlainTextHello() {
            //Register the operation in the log file
            Pdao.postLog("User requested a salutation",LOG_NAME);
            //Answer the petition
            return "Hello Jersey at "+System.getProperty("user.home");
        }
        /**
         * Consults the application log file
         * @returns string witht he contents of the log file
         */
        @GET
        @Path("/log")
        @Produces(MediaType.TEXT_PLAIN)
        public String showLog() {
            //Register the operation in the log file
            Pdao.postLog("User requested the log",LOG_NAME);
            //Answer the petition
            return "Application Log:"+Pdao.getLog(LOG_NAME);
        }
    
    /**
     * Lists all available models from the server
     */
    @GET
	@Path("/models")
	@Produces(MediaType.TEXT_HTML)
	public String listModels() {
		List<String> projects = Pdao.listAll(MOD_PATH);
		String result ="<ul  class='thumbnails'>";
        String list = "";
		if(!projects.isEmpty()){
			for (String project:projects){
				list+=project+"; ";
				result +=//"<li class='col-md-3'><div>"+name+"</div></li>";
				"<li class='col-md-3'><div class='thumbnail'>"+
	                "<img src='http://placehold.it/320x200' alt='ALT NAME'><div class='caption'>"+
	                  "<h3>"+Pdao.getName(project)+"</h3>"+
	                  "<p>"+Pdao.getDescription(project) +"</p>"+
	                  "<p align='center'><a href='"+"viewer.html?proc="+project+"' class='btn btn-primary btn-block'>Open</a></p></div></div></li>";
			}
		}
        Pdao.postLog("Model list request:\n\t\t\t"+list);
		return result+"</ul>";
	}
    /**
     * Downloads a model from the server
     */
    @GET
    @Path("/models/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String downloadPlainModel(@PathParam("name") String name) {
        return Pdao.loadPlainText(MOD_PATH+name);
    }
    /**
     * Lists all available images from the server
     */
    @GET
	@Path("/images")
	@Produces(MediaType.TEXT_HTML)
	public String listImages() {
		List<String> projects = Pdao.listAll(IMG_PATH);
		String result ="<ul  class='thumbnails'>";
        String list = "";
		if(!projects.isEmpty()){
			for (String project:projects){
				list+=project+"; ";
				result +=//"<li class='col-md-3'><div>"+name+"</div></li>";
				"<li class='col-md-3'><div class='thumbnail'>"+
	                "<img src='http://placehold.it/320x200' alt='ALT NAME'><div class='caption'>"+
	                  "<h3>"+Pdao.getName(project)+"</h3>"+
	                  "<p>"+Pdao.getDescription(project) +"</p>"+
	                  "<p align='center'><a href='"+"viewer.html?proc="+project+"' class='btn btn-primary btn-block'>Open</a></p></div></div></li>";
			}
		}
        Pdao.postLog("Image list request:\n\t\t\t"+list);
		return result+"</ul>";
	}
    /**
     * Downloads an image from the server
     */
    @GET
    @Path("/images/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String downloadPlainImage(@PathParam("name") String name) {
        return Pdao.loadPlainText(IMG_PATH+name);
    }
    /**
     * Allows the download of the raw specified file
     */
    @GET
    @Path("/download/{name}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("name") String name){
        File file = Pdao.loadFile(name);
        return Response.ok(file,MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename=\""+file.getName()+"\"").build();
    }
    /**
     * Allows the download of the raw specified file
     */
    @GET
    @Path("/download/model/{name}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadModel(@PathParam("name") String name){
        File file = Pdao.loadFile(MOD_PATH+name);
        return Response.ok(file,MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename=\""+file.getName()+"\"").build();
    }
    /**
     * Allows the download of the raw specified file
     */
    @GET
    @Path("/download/image/{name}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadImage(@PathParam("name") String name){
        File file = Pdao.loadFile(IMG_PATH+name);
        return Response.ok(file,MediaType.APPLICATION_OCTET_STREAM).header("Content-Disposition","attachment; filename=\""+file.getName()+"\"").build();
    }
    
    /*
     * POST methods
     ****************************************************/
    /**
     * Uploads a file to the server via InputStream
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadFile (@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail){
        if(uploadedInputStream == null || fileDetail == null)
            Pdao.postLog("Rest.uploadFile(): Invalid form data");
        if (Pdao.saveInputStream(uploadedInputStream,fileDetail.getFileName()))
            Pdao.postLog("Rest.uploadFile(): File saved: " + fileDetail.getFileName());
        else
            Pdao.postLog("Rest.uploadFile(): Can not save file: "+ fileDetail.getFileName());
    }
    /**
     * Uploads a model to the server via InputStream
     */
    @POST
    @Path("/upload/model")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadModel (@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail){
        if(uploadedInputStream == null || fileDetail == null)
            Pdao.postLog("Rest.uploadFile(): Invalid form data");
        if (Pdao.saveInputStream(uploadedInputStream,MOD_PATH+fileDetail.getFileName()))
            Pdao.postLog("Rest.uploadFile(): File saved: " + fileDetail.getFileName());
        else
            Pdao.postLog("Rest.uploadFile(): Can not save file: "+ fileDetail.getFileName());
    }
    /**
     * Uploads an image to the server via InputStream
     */
    @POST
    @Path("/upload/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void uploadImage (@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail){
        if(uploadedInputStream == null || fileDetail == null)
            Pdao.postLog("Rest.uploadFile(): Invalid form data");
        if (Pdao.saveInputStream(uploadedInputStream,IMG_PATH+fileDetail.getFileName()))
            Pdao.postLog("Rest.uploadFile(): File saved: " + fileDetail.getFileName());
        else
            Pdao.postLog("Rest.uploadFile(): Can not save file: "+ fileDetail.getFileName());
    }
    
    
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveProject(@FormParam("name") String name, @FormParam("desc") String desc, @FormParam("ecode") String ecode, @FormParam("jcode") String jcode )
	{	
        Pdao.postLog("\nECODE:\n"+ecode,LOG_NAME);
		Pdao.save(name, desc, ecode, jcode);//Add a check function to the form!!!!!!
	}
    
    
    
    @GET
	@Path("/projects")
	@Produces(MediaType.TEXT_HTML)
	public String listProjects() {
		List<String> projects = Pdao.listAll(PRO_PATH);
		String result ="<ul  class='thumbnails'>";
        String list = "";
		if(!projects.isEmpty()){
			for (String project:projects){
				list+=project+"; ";
				result +=//"<li class='col-md-3'><div>"+name+"</div></li>";
				"<li class='col-md-3'><div class='thumbnail'>"+
	                "<img src='http://placehold.it/320x200' alt='ALT NAME'><div class='caption'>"+
	                  "<h3>"+Pdao.getName(project)+"</h3>"+
	                  "<p>"+Pdao.getDescription(project) +"</p>"+
	                  "<p align='center'><a href='"+"viewer.html?proc="+project+"' class='btn btn-primary btn-block'>Open</a></p></div></div></li>";
			}
		}
        Pdao.postLog("Project list request:\n\t\t\t"+list);
		return result+"</ul>";
	}
	
	@GET
	@Path("/project/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCode(@PathParam ("name") String name) {
		return Pdao.load(name);
	}
    
    /*
    @GET
	@Path("/create/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createFile (@PathParam ("name") String name) {
        return "Created "+Pdao.saveTest(path,name);
	}*/
    
    
    
    
} 
