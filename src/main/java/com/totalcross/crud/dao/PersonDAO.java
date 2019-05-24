package com.totalcross.crud.dao;

import java.sql.SQLException;

import com.totalcross.crud.model.Person;
import totalcross.sql.Connection;
import totalcross.sql.DriverManager;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sys.Convert;
import totalcross.sys.Settings;

public class PersonDAO {

    private Connection con;


    public PersonDAO() throws SQLException {
        con = DriverManager.getConnection("jdbc:sqlite:" + Convert.appendPath(Settings.appPath, "crud.db"));

        PreparedStatement pSmt = con.prepareStatement("create table if not exists person(id int primary key, name varchar, born varchar, number varchar)");
        pSmt.executeUpdate();
    }

    public void insert(Person p) throws SQLException {
        PreparedStatement pSmtInsert = con.prepareStatement("insert into person values(?, ?, ?, ?)");
        pSmtInsert.clearParameters();
        p.setId(nextId());
        pSmtInsert.setInt(1, p.getId());
        pSmtInsert.setString(2, p.getName());
        pSmtInsert.setString(3, p.getBorn());
        pSmtInsert.setString(4, p.getNumber());
        pSmtInsert.executeUpdate();
    }

    public void update(Person p) throws SQLException {
        PreparedStatement pSmtUpdate = con.prepareStatement("UPDATE person SET name = ?, number = ?, born = ? WHERE id = ?");
        pSmtUpdate.clearParameters();
        pSmtUpdate.setString(1, p.getName());
        pSmtUpdate.setString(2, p.getNumber());
        pSmtUpdate.setString(3, p.getBorn());
        pSmtUpdate.setInt(4, p.getId());
        pSmtUpdate.executeUpdate();

    }

    public void delete(Person p) throws SQLException {
        PreparedStatement pSmtDelete = con.prepareStatement("DELETE FROM person WHERE id = ?");
        pSmtDelete.clearParameters();
        pSmtDelete.setInt(1, p.getId());

        pSmtDelete.executeUpdate();
    }

    public String[][] all() throws SQLException {

        PreparedStatement pSmtSelect = con.prepareStatement("SELECT * FROM person");
        ResultSet rs	    =  pSmtSelect.executeQuery();

        int amount = 0;
        while(rs.next()){
            amount += 1;
        }

        String[][] retorno  = new String[amount][4];

        rs	=  pSmtSelect.executeQuery();
        for (int i = 0; i < amount; i++) {
            rs.next();
            for (int j = 0; j < 4; j++) {
                retorno[i][j] = rs.getString(j+1);
            }

        }
        rs.close();
        return retorno;
    }

    private int nextId() throws SQLException{
        int retorno = 1;

        PreparedStatement pSmtId = con.prepareStatement("SELECT max(id) as  vId from person");
        ResultSet rs = pSmtId.executeQuery();
        while(rs.next()){
            retorno = rs.getInt("vId") + 1;
        }
        rs.close();
        return retorno;
    }

}
