package org.vaadin.touchkit.itest;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;
import org.vaadin.touchkit.ui.NavigationView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.v7.ui.ProgressBar;
import com.vaadin.v7.ui.ProgressIndicator;

public class WaitingAndSpinning extends AbstractTouchKitIntegrationTest implements ClickListener {


    @SuppressWarnings("deprecation")
    public WaitingAndSpinning() {
        setDescription("Test case showing animated ProgressIndicator and pending server visit indigators");
        
        NavigationView navigationView = new NavigationView(".....");
        
        Button button = new Button("This takes long....", this);
        
        CssLayout cssLayout = new CssLayout();
        
        cssLayout.addComponent(button);
        
        navigationView.setContent(cssLayout);
        
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setValue((float) 0.5);
        cssLayout.addComponent(indicator);
        
        ProgressBar progressBar = new ProgressBar();
        progressBar.setValue(0.3f);
        cssLayout.addComponent(progressBar);
        
        indicator = new ProgressIndicator();
        indicator.setIndeterminate(true);
        cssLayout.addComponent(indicator);
        
        progressBar = new ProgressBar();
        progressBar.setIndeterminate(true);
        cssLayout.addComponent(progressBar);
        
        addComponent(navigationView);
    }

    @Override
    public void buttonClick(ClickEvent event) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

}
