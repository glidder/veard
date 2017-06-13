package es.uca.veard.listener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Listener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		Pdao.postLog("\nServlet destruction\n"+
                     "--------------------------------------\n");
	}

        //Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Pdao.postLog("\nServlet initialisation\n"+
                     "--------------------------------------\n");
	}
}

/*
 * Class that implements all REST methods
 */
@Path("/dao")
public class Rest extends HttpServlet {
    
    //TODO: eliminate from Web.xml
    private static String modelPath = "models/";
    private static String imagePath = "images/";
    //TODO: eliminate this variable
    private static String logName = "log";
    
    /* 
     * Getting the propperties configured in Web.xml
     */
    /*@PostConstruct
    public void init() throws ServletException {
        //ServletContext sc = getServletContext(); 
        modelPath = config.getInitParameter ("modelstorage");
        imagePath = config.getInitParameter ("imagestorage");
        logName = config.getInitParameter ("logfilename");
        
        //Register the operation in the log file
        Pdao.postLog(logName,"\nServlet initialisation\n"+
                     "--------------------------------------\n");
    }*/
    /*public void init(ServletConfig config) throws ServletException {
        super.init(config);
    //TODO:delete    path = config.getInitParameter("filestorage");
        modelPath = config.getInitParameter ("modelstorage");
        imagePath = config.getInitParameter ("imagestorage");
        logName = config.getInitParameter ("logfilename");
        
        //Register the operation in the log file
        Pdao.postLog(logName,"\nServlet initialisation\n"+
                     "--------------------------------------\n");
    }*/
    
    /*
     * GET methods
     *****************************************************/
    
    /*
     * Tests methods
     */
        /*
         * Simple hello test
         */
        @GET
        @Path("/hello")
        @Produces(MediaType.TEXT_PLAIN)
        public String sayPlainTextHello() {
            //Register the operation in the log file
            Pdao.postLog(logName,"User requested a salutation");
            //Answer the petition
            return "Hello Jersey at "+System.getProperty("user.home");
        }
        /*
         * Method to consult the application log file
         */
        @GET
        @Path("/log")
        @Produces(MediaType.TEXT_PLAIN)
        public String showLog() {
            //Register the operation in the log file
            Pdao.postLog(logName,"User requested the log");
            return "Application Log:"+Pdao.getLog(logName);
        }
    
    
	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void saveProject(@FormParam("name") String name, @FormParam("desc") String desc, @FormParam("ecode") String ecode, @FormParam("jcode") String jcode )
	{	//Editor.hum=Editor.hum+1;
		//System.out.print("\nCACACACACACACACACACA\n"+ecode);
        Pdao.postLog(logName,"\nECODE:\n"+ecode);
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
