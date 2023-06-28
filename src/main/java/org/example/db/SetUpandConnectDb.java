package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetUpandConnectDb {

    private Connection connection;
    private String url="jdbc:mysql://localhost:3306/restapi";
    private String username="root";
    private String password="";


    public static void main(String[] args) {
        SetUpandConnectDb st=new SetUpandConnectDb();
        st.setupAndConnectionDb();
    }
    public  Connection setupAndConnectionDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection= DriverManager.getConnection(this.url, this.username, this.password);
            String query = "CREATE DATABASE IF NOT EXISTS RESTAPI";
            Statement st = this.connection.createStatement();
            st.executeUpdate(query);

        }catch (ClassNotFoundException var1) {
            System.out.println(var1);
        }catch(SQLException var2){
            System.out.println(var2);
        }
        return this.connection;
    }
}

