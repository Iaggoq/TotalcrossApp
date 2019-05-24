package com.totalcross.crud.ui;

import com.totalcross.crud.dao.PersonDAO;
import com.totalcross.crud.model.Person;
import totalcross.sys.Convert;
import totalcross.ui.Button;
import totalcross.ui.Control;
import totalcross.ui.Label;
import totalcross.ui.Edit;
import totalcross.ui.Window;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;

import java.sql.SQLException;



public class PersonEditForm extends Window {

    private static final int SALVAR_BTN_ID 		= 100;
    private static final int CANCELAR_BTN_ID 	= 101;
    // buttons ids
    private final PersonDAO dao;
    private Edit nameEdit;
    private Edit numberEdit;
    private Edit dateEdit;
    // instancing Edits

    PersonEditForm(PersonDAO dao, Person person, int editMode){
        this.dao = dao;
        this.appObj = person;
        this.appId = editMode;

    }
    protected void postPopup(){
        super.postPopup();
        initUI();
        // method to call this window
    }

    public void initUI(){
        super.initUI();

        Label lb4 = new Label("Person's Data",CENTER);
        // Window label
        Button saveButton   = new Button("Save");
        Button cancelButton = new Button("Cancel");
        // Button text
        Edit   idEdit 		  = new Edit("9999");
        nameEdit 		      = new Edit();
        numberEdit           = new Edit("(99)9999-99999");
        // Edit mask to make the input his format
        numberEdit.setMode(Edit.NORMAL, true);
        numberEdit.setValidChars(Edit.numbersSet);
        //Edit set to only make the number mask  editable
        numberEdit.setKeyboard(Edit.KBD_NUMERIC);
        // Edit keyboard set to only make input numbers
        dateEdit            = new Edit("99/99/99");
        dateEdit.setMode(Edit.NORMAL, true);
        dateEdit.setValidChars(Edit.numbersSet);
        dateEdit.setKeyboard(Edit.KBD_NUMERIC);
        lb4.setBackForeColors(0x2979ff, 0xEEEEEE);
        // Change label window color
        saveButton.appId = SALVAR_BTN_ID;
        cancelButton.appId = CANCELAR_BTN_ID;
        // Button receiving his ID

        idEdit.setEditable(false);
        // Set idEdit to not receive input
        idEdit.setText(Convert.toString(((Person) appObj).getId()));
        // Edit only works with String so it need to transform
        nameEdit.setText(((Person) appObj).getName());
        numberEdit.setText(((Person) appObj).getNumber());
        dateEdit.setText(((Person) appObj).getBorn());


        add(cancelButton, RIGHT - 2, TOP + 2, PREFERRED + 2, PREFERRED);
        add(saveButton, BEFORE - 2, SAME, SAME, SAME);
        add(new Label("Name:"), LEFT+145, AFTER + 7);
        add(nameEdit, CENTER, AFTER , SCREENSIZE + 85, PREFERRED);
        add(new Label("Number:"), LEFT+145, AFTER + 5);
        add(numberEdit, CENTER, AFTER , SCREENSIZE + 85, PREFERRED);
        add(new Label("Date:"), LEFT+145, AFTER + 5);
        add(dateEdit, CENTER, AFTER , SCREENSIZE + 85, PREFERRED);
        add(lb4,CENTER, TOP,SCREENSIZE + 100, PREFERRED);
        add(cancelButton, CENTER, BOTTOM - 10, PARENTSIZE + 95, PREFERRED);
        add(saveButton, CENTER, BOTTOM - 230, PARENTSIZE + 95, PREFERRED);
        // Setting buttons, Labels and Edits locations size, Weight and etc.
        saveButton.setBackForeColors(Color.getRGB("1878d1"), Color.WHITE);
        cancelButton.setBackForeColors(Color.getRGB("d34419"), Color.WHITE);
        // Settings buttons colors


    }


    public void onEvent(Event event){
        if (event.type == ControlEvent.PRESSED) {
            // event type controller button pressed
            switch (((Control) event.target).appId) {
                case SALVAR_BTN_ID: {
                    // save button id pressed
                    if (NameExist()) {
                        // check if name exist
                        if (NumberCorrect()) {
                            // check if any number is missing
                            if (DateCorrect()) {
                                // check if any number date is missing
                                switch (appId) {
                                    case 1: {
                                        // Edit mode 1 New
                                        try {
                                            dao.insert((Person) appObj);
                                            // save new person
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                    case 2: {
                                        // Edit mode 2 update
                                        try {
                                            dao.update((Person) appObj);
                                            // Update new person
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    }
                                }
                                unpop();
                                // Back to first window
                            }
                        }
                    }
                    break;
                }
                case CANCELAR_BTN_ID: {
                    // Button cancel pressed
                    unpop();
                    // Back to first window
                    break;
                }
            }
        }
    }

    private boolean NameExist(){
        // check if name exist
        if(!(this.nameEdit.getLength() > 0)){
            new MessageBox("Error", "Name invalid").popup();
            // Make a pop up message telling name invalid
            return false;
            // Dont make the program continue and return to input the data
        }

        return true;
    }
    private boolean NumberCorrect(){
        if(!(this.numberEdit.getLength()> 10)){
            // Check if there is any number missing
            new MessageBox("Error", "Number invalid").popup();
            // Number invalid pop up a message telling
            return false;
            // Dont make the program continue and return to input the data
        }
         return true;
    }
    private boolean DateCorrect(){
        if(!(this.dateEdit.getLength()> 5)){
            // Check if there is any number missing
            new MessageBox("Error", "Date invalid").popup();
            return false;
            // Dont make the program continue and return to input the data
        }
        // Input Edit Text in the person to make a new or update.
        ((Person) appObj).setName(this.nameEdit.getText());
        ((Person) appObj).setNumber(this.numberEdit.getText());
        ((Person) appObj).setBorn(this.dateEdit.getText());
        return true;
    }
}
