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