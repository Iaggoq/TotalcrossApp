package com.totalcross.crud.ui;

import totalcross.ui.Container;
import com.totalcross.crud.dao.PersonDAO;
import com.totalcross.crud.model.Person;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.ui.Button;

import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.UnitsConverter;

import java.sql.SQLException;

public class PersonForm extends Container {
    private static final int NEW_BTN_ID		= 100;
    private static final int UPDATE_BTN_ID 	= 101;
    private static final int DELETE_BTN_ID 		= 102;
    private static PersonDAO dao 		    = null;
    private Grid pessoaGrid;

    public PersonForm() throws SQLException {
        dao = new PersonDAO();
    }
    public void initUI(){

        Button newButton   = new Button("New");
        Button updateButton = new Button("Update");
        Button deleteButton   = new Button("Delete");
        // Create new buttons
        newButton.appId	 = NEW_BTN_ID;
        updateButton.appId = UPDATE_BTN_ID;
        deleteButton.appId	 = DELETE_BTN_ID;
        // Give the buttons ID
        add(newButton, CENTER, TOP + 2+ UnitsConverter.toPixels(DP + 8), PARENTSIZE + 95, PREFERRED);
        add(updateButton, CENTER, AFTER + UnitsConverter.toPixels(DP + 8), PARENTSIZE + 95, PREFERRED);
        add(deleteButton, CENTER, AFTER + UnitsConverter.toPixels(DP + 8), PARENTSIZE + 95, PREFERRED);
        // Declare where the buttons will be locate
        newButton.setBackForeColors(Color.getRGB("18d25a"), Color.WHITE);
        updateButton.setBackForeColors(Color.getRGB("1878d1"), Color.WHITE);
        deleteButton.setBackForeColors(Color.getRGB("d34419"), Color.WHITE);
        // Set Buttons colors
        pessoaGrid = new Grid(new String[]{"Id", "Name","Date","Number"}, new int[]{-4, -30,-25,-40}, new int[]
                {CENTER, LEFT,LEFT,LEFT}, false);
        // Create a grid with lacuna names, location, how the information will be centralized, disable check box
        add(pessoaGrid, LEFT, AFTER + 2, FILL - 2, FILL - 2);
        try {
            pessoaGrid.setItems(dao.all());
            // this will fill the grid
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onEvent(Event event){
        // Event method
        switch (event.type) {
            case ControlEvent.PRESSED:
                // what will happen when the button pressed
                switch (((Control) event.target).appId) {
                    // what will happen with which button pressed using his id
                    case NEW_BTN_ID:
                        new PersonEditForm(dao, new Person(), 1).popupNonBlocking();
                        // Edit mode 1 insert a new person
                        break;
                    case UPDATE_BTN_ID:{
                        if(SelectPerson()){
                            new PersonEditForm(dao, getSelectPerson(), 2).popupNonBlocking();
                            // Edit mode 2 update a new person
                        }
                        break;
                    }
                    case DELETE_BTN_ID:{
                        if(SelectPerson()){
                            try {
                                dao.delete(getSelectPerson());
                                // Delete Button case will delete the selected person
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            try {
                                pessoaGrid.setItems(dao.all());
                                // This will refresh the grid to show the updated grid
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            pessoaGrid.setSelectedIndex(-1);
                        }
                        break;
                    }
                }
                break;
            case ControlEvent.WINDOW_CLOSED:{

                if(event.target instanceof PersonEditForm){
                    // Event Window closed this is when the second window is closed
                    try {
                        pessoaGrid.setItems(dao.all());
                        // refresh the grid
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    pessoaGrid.setSelectedIndex(-1);
                }
            }
            break;
        }
    }

    private boolean SelectPerson(){
        boolean SelectPerson = pessoaGrid.getSelectedIndex() >= 0;

        if(!SelectPerson){
            new MessageBox("Atenttion", "Nobody was select").popup();;
            // this will show if pressed update button but no one was select
        }
        return SelectPerson;
    }

    private Person getSelectPerson(){
        Person p = new Person();
        String[] colunas = pessoaGrid.getSelectedItem();
        // this will return the select person from the grid
        try{
            p.setId(Convert.toInt(colunas[0]));
            p.setName(colunas[1]);
            p.setNumber(colunas[3]);
            p.setBorn(colunas[2]);
            // this will set the EDIT with value from the select person
        }catch(InvalidNumberException e){
            e.printStackTrace();
        }
        return p;
    }

}