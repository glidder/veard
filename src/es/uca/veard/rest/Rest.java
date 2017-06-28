/*
 * Rest.java
 * v0.8
 * 28/07/2017
 * Copyright (c) Luis José Quintana Bolaño
 */
package es.uca.veard.rest;

import java.util.List;
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
 * @author Luis José Quintana Bolaño
 */
@Path("/dao")
public class Rest extends HttpServlet {
    
    //TODO: eliminate from Web.xml
    private static final String MODEL_PATH = "models/";
    private static final String IMAGE_PATH = "images/";
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
    
    
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveProject(@FormParam("name") String name, @FormParam("desc") String desc, @FormParam("ecode") String ecode, @FormParam("jcode") String jcode )
	{	//Editor.hum=Editor.hum+1;
		//System.out.print("\nCACACACACACACACACACA\n"+ecode);
        Pdao.postLog("\nECODE:\n"+ecode,LOG_NAME);
		Pdao.save(name, desc, ecode, jcode);//Add a check function to the form!!!!!!
	}
    
   
    
    @GET
	@Path("/list")
	@Produces(MediaType.TEXT_HTML)
	public String listProjects() {
		List<String> projects = Pdao.listAll();
		String result ="<ul  class='thumbnails'>";
		if(!projects.isEmpty()){
			for (String project:projects){
				System.out.print(project);
				result +=//"<li class='col-md-3'><div>"+name+"</div></li>";
				"<li class='col-md-3'><div class='thumbnail'>"+
	                "<img src='http://placehold.it/320x200' alt='ALT NAME'><div class='caption'>"+
	                  "<h3>"+Pdao.getName(project)+"</h3>"+
	                  "<p>"+Pdao.getDescription(project) +"</p>"+
	                  "<p align='center'><a href='"+"viewer.html?proc="+project+"' class='btn btn-primary btn-block'>Open</a></p></div></div></li>";
			}
		}
		return result;
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
    
    /*
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile (@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail){
        if(uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity("Invalid form data").build();
        if (Pdao.uploadTest(uploadedInputStream,path+fileDetail.getFileName()))
            return Response.status(200).entity("File saved to " + path).build();
        else
            return Response.status(500).entity("Can not save file to "+path).build();
    }*/
    
} 
