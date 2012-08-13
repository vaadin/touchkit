package com.vaadin.addon.touchkit.server;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;

import com.vaadin.terminal.gwt.server.BootstrapListener;
import com.vaadin.terminal.gwt.server.BootstrapResponse;
import com.vaadin.terminal.gwt.server.VaadinContextEvent;
import com.vaadin.terminal.gwt.server.VaadinContextListener;

public class TouchKitVaadinContextListener implements VaadinContextListener {

    @Override
    public void contextCreated(VaadinContextEvent event) {
        event.getVaadinContext().addBootstrapListener(new BootstrapListener() {
            
            @Override
            public void modifyBootstrap(BootstrapResponse response) {
                Document document = response.getDocument();
                

                
                Element html = document.getElementsByTag("html").get(0);
                Element head = document.getElementsByTag("head").get(0);
                
                html.attr("cache","bar");
                DocumentType doctype = (DocumentType) html.previousSibling();
                DocumentType html5doc = new DocumentType("html", "", "", "");
                doctype.replaceWith(html5doc);
                
                Element element = document.createElement("meta");
                element.attr("name", "viewport");
                StringBuilder content = new StringBuilder();
                content.append("width=device-width");
                content.append(",");
                content.append("user-scalable=no");
                content.append(",");
                content.append("initial-scale=1");
                content.append(",");
                content.append("maximum-scale=1");
                content.append(",");
                content.append("minimum-scale=1");
                element.attr("content", content.toString());
                head.appendChild(element);
                
                element = document.createElement("meta");
                element.attr("name", "apple-mobile-web-app-capable");
                element.attr("content", "yes");
                head.appendChild(element);
                
                element = document.createElement("meta");
                element.attr("name", "apple-mobile-web-app-status-bar-style");
                element.attr("content", "black");
                head.appendChild(element);
                
            }
        });
    }

    @Override
    public void contextDestoryed(VaadinContextEvent event) {
    }

}
