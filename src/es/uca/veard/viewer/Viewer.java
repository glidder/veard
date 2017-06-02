package es.uca.veard.viewer;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import es.uca.veard.dao.Pdao;

@Path("/viewer")
public class Viewer {
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
}
