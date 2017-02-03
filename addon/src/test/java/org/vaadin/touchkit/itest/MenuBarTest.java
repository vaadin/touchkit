package org.vaadin.touchkit.itest;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import org.vaadin.touchkit.AbstractTouchKitIntegrationTest;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class MenuBarTest extends AbstractTouchKitIntegrationTest implements
        ClickListener {

    public MenuBarTest() {
        setDescription("Probably quite little used component, but supporting its theme still somehow.");

     MenuBar menubar = new MenuBar();
        // Save reference to individual items so we can add sub-menu items to
        // them
        final MenuBar.MenuItem file = menubar .addItem("File", null);
        final MenuBar.MenuItem newItem = file.addItem("New", null);
        file.addItem("Open file...", menuCommand);
        file.addSeparator();

        newItem.addItem("File", menuCommand);
        newItem.addItem("Folder", menuCommand);
        newItem.addItem("Project...", menuCommand);

        file.addItem("Close", menuCommand);
        file.addItem("Close All", menuCommand);
        file.addSeparator();

        file.addItem("Save", menuCommand);
        file.addItem("Save As...", menuCommand);
        file.addItem("Save All", menuCommand);

        final MenuBar.MenuItem edit = menubar.addItem("Edit", null);
        edit.addItem("Undo", menuCommand);
        edit.addItem("Redo", menuCommand).setEnabled(false);
        edit.addSeparator();

        edit.addItem("Cut", menuCommand);
        edit.addItem("Copy", menuCommand);
        edit.addItem("Paste", menuCommand);
        edit.addSeparator();

        final MenuBar.MenuItem find = edit.addItem("Find/Replace", menuCommand);

        // Actions can be added inline as well, of course
        find.addItem("Google Search", new Command() {
            public void menuSelected(MenuItem selectedItem) {
            }
        });
        find.addSeparator();
        find.addItem("Find/Replace...", menuCommand);
        find.addItem("Find Next", menuCommand);
        find.addItem("Find Previous", menuCommand);

        final MenuBar.MenuItem view = menubar.addItem("View", null);
        MenuItem i = view.addItem("Check me", menuCommand);
        i.setCheckable(true);
        view.addItem("Customize Toolbar...", menuCommand);
        view.addSeparator();

        view.addItem("Actual Size", menuCommand);
        view.addItem("Zoom In", menuCommand);
        view.addItem("Zoom Out", menuCommand);

        addComponent(menubar);
    }

    private Command menuCommand = new Command() {
        public void menuSelected(MenuItem selectedItem) {
        }
    };

    @Override
    public void buttonClick(ClickEvent event) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

}
