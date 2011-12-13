package com.vaadin.addon.touchkit.itest;

import java.io.Serializable;

import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.TouchKitWindow;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;

public class MeetingRoomView extends TouchKitWindow {

    public MeetingRoomView() {
        setContent(new RootView());
    }

    public class RootView extends NavigationView implements Component {

        private MeetingRoom myRoom;
        private MeetingRoomForm myForm;

        public RootView() {

            setCaption("Form test");

            VerticalComponentGroup verticalComponentGroup = new VerticalComponentGroup();

            myRoom = new MeetingRoom("Grand A", "Ballroom");

            Label myLabel = new Label();
            myLabel.setValue("Very important info.");
            myLabel.setCaption("Title:");
            verticalComponentGroup.addComponent(myLabel);

            myForm = new MeetingRoomForm();
            myForm.setItemDataSource(new BeanItem<MeetingRoom>(myRoom));
            myForm.setWriteThrough(true);
            myForm.setImmediate(true);
            verticalComponentGroup.addComponent(myForm);

            setContent(verticalComponentGroup);

        }

    }

    public class MeetingRoomForm extends Form {

        NativeSelect roomTypes;

        public MeetingRoomForm() {

            setFormFieldFactory(new DefaultFieldFactory() {

                @Override
                public Field createField(Item item, Object propertyId,
                        Component uiContext) {
                    Field field = super
                            .createField(item, propertyId, uiContext);
                    if (propertyId.equals("roomType")) {

                        roomTypes = new NativeSelect("Room Type");
                        roomTypes.setNewItemsAllowed(false);
                        roomTypes.setNullSelectionAllowed(false);
                        getRoomTypes();
                        field = roomTypes;
                        return field;
                    } else {
                        return null;
                    }
                }
            });

        }

        private void getRoomTypes() {

            roomTypes.removeAllItems();
            roomTypes.addItem("Ballroom");
            roomTypes.addItem("Conference Room");
            roomTypes.addItem("Suite");
            roomTypes.addItem("Meeting Room");

        }


    }

    public class MeetingRoom implements Serializable {
        
        String name;
        String roomType;
        
        public MeetingRoom(String name, String roomType) {
            
            this.name = name;
            this.roomType = roomType;
            
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getRoomType() {
            return roomType;
        }
        
        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }
        
    }
}
