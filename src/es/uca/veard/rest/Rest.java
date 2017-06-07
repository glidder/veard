package es.uca.veard.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uca.veard.dao.Pdao;

@Path("/dao")
public class Rest {
	private static int hum=0;
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
    return "Hello Jersey "+Editor.hum;
  }

} 
