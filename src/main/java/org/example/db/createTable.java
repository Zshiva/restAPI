package org.example.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class createTable {

    private Connection connection;
    private String usequery = "USE RESTAPI;";

    public createTable(Connection connection) {
        this.connection = connection;
        this.createUserTable();
        this.createBlogPostTable();
        this.createCommentTable();
        this.createRoleTable();
        this.createPermissionTable();
        this.createUserPermissionsTable();
    }

    public static void main(String[] args) {
        createTable ct= new createTable(new SetUpandConnectDb().setupAndConnectionDb());
    }

    public void createUserTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) NOT NULL," +
                "password VARCHAR(50) NOT NULL," +
                "role VARCHAR(20) NOT NULL" +
                ")";
        try {
            Statement s1 = this.connection.createStatement();
            s1.executeUpdate(this.usequery);
            s1.executeUpdate(query);
        } catch (SQLException var3) {
            var3.printStackTrace();
        }
    }

    public void createBlogPostTable(){
        String query="CREATE TABLE IF NOT EXISTS blog_posts (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "title VARCHAR(100) NOT NULL," +
                "content TEXT NOT NULL," +
                "thumbnail_image VARCHAR(100)" +
                ")";
        try {
            Statement st=this.connection.createStatement();
            st.executeUpdate(this.usequery);
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createCommentTable(){
        String query="CREATE TABLE IF NOT EXISTS comments (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "post_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "content TEXT NOT NULL," +
                "FOREIGN KEY (post_id) REFERENCES blog_posts(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";
        try {
            Statement st=this.connection.createStatement();
            st.executeUpdate(this.usequery);
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void createRoleTable() {
        String query = "CREATE TABLE IF NOT EXISTS roles (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL" +
                ")";
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(this.usequery);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createPermissionTable() {
        String query = "CREATE TABLE IF NOT EXISTS permissions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(50) NOT NULL" +
                ")";
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(this.usequery);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUserPermissionsTable() {
        String query = "CREATE TABLE IF NOT EXISTS user_permissions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "user_id INT NOT NULL," +
                "permission VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")";
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(this.usequery);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
