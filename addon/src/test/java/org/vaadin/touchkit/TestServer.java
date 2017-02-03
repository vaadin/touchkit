package org.vaadin.touchkit;

import java.io.File;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Ignore;
import org.vaadin.touchkit.server.TouchKitServlet;

@Ignore
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

        final ServerConnector connector = new ServerConnector(server);

        connector.setPort(7777);
        server.setConnectors(new Connector[] { connector });
        WebAppContext context = new WebAppContext();

        ServletHolder servletHolder = new ServletHolder(TouchKitServlet.class);
        servletHolder.setInitParameter("UI", TouchkitTestUI.class.getName());
        servletHolder.setInitParameter("widgetset",
                "org.vaadin.touchkit.gwt.TouchKitWidgetSet");

        // check for production mode argument
        for (String arg : args) {
            if ("productionMode".equalsIgnoreCase(arg)) {
                servletHolder.setInitParameter("productionMode", "true");
            }
        }

        MimeTypes mimeTypes = context.getMimeTypes();
        mimeTypes.addMimeMapping("appcache", "text/cache-manifest");
        mimeTypes.addMimeMapping("manifest", "text/cache-manifest");
        context.setMimeTypes(mimeTypes);

        // // test fallback app and widgetset
        // servletHolder.setInitParameter("fallbackApplication",
        // FallbackApplication.class.getName());
        // servletHolder.setInitParameter("fallbackWidgetset",
        // Constants.DEFAULT_WIDGETSET);

        File file = new File("./target/classes");
        if (!file.isDirectory() || !new File(file, "VAADIN").isDirectory()) {
            System.err.println(
                    "Oops: " + file + "/VAADIN does not seem to exist!");
            System.err.println("Did you remember to mvn vaadin:compile?");
            return;
        }
        context.setWar(file.getPath());
        context.setContextPath("/");
        context.addServlet(servletHolder, "/*");

        servletHolder = new ServletHolder(RemoteLogger.class);
        context.addServlet(servletHolder, "/remotelog");

        server.setHandler(context);

        server.start();
        server.join();

    }
}
