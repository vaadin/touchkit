package com.vaadin.addons.touchkit;

import java.io.File;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class TestServer {

	/**
	 * 
	 * Test server for the addon.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server();
		
		final Connector connector = new SelectChannelConnector();

		connector.setPort(7777);
		server.setConnectors(new Connector[] { connector });

        WebAppContext context = new WebAppContext();
        context.setDescriptor("./src/main/webapp/WEB-INF/web.xml");
		File file = new File("./target");
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {
			if(file2.isDirectory() && file2.getName().startsWith("vaadin-touchkit-")) {
				context.setWar(file2.getPath());
				break;
			}
		}
        context.setContextPath("/");
 
        server.setHandler(context);
        
        server.start();
		server.join();
	}

}
