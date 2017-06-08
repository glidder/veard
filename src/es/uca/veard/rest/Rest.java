package es.uca.veard.rest;

import java.util.List;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import es.uca.veard.dao.Pdao;

@Path("/dao")
public class Rest extends HttpServlet {
    
    public void init(ServletConfig config){
        path = config.getInitParameter("filestorage");
    }
    
	private static int hum=0;
    private static String path="/userdata/";
    
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@Consumes(MediaType.TEXT_PLAIN)
	public void saveProject(@FormParam("name") String name, @FormParam("desc") String desc, @FormParam("ecode") String ecode, @FormParam("jcode") String jcode )
	{	//Editor.hum=Editor.hum+1;
		System.out.print("\nCACACACACACACACACACA\n"+ecode);
		Pdao.save(name, desc, ecode, jcode);//Add a check function to the form!!!!!!
	}
    
    // This method is called if TEXT_PLAIN is requested
    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello Jersey "+System.getProperty("user.home")+Rest.hum;
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
    
    @GET
	@Path("/create/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String createFile (@PathParam ("name") String name) {
        return "Created "+Pdao.saveTest(path,name);
	}
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile (@FormDataParam("file") File uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail){
        if(uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity("Invalid form data").build();
        if (Pdao.uploadTest(uploadedInputStream,path+fileDetail.getFileName()))
            return Response.status(200).entity("File saved to " + path).build();
        else
            return Response.status(500).entity("Can not save file to "+path).build();
    }
} 
