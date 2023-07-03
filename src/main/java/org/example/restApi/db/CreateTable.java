package org.example.restApi.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    private Connection connection;
    private String usequery = "USE RESTAPI;";

    public CreateTable(Connection connection) {
        this.connection = connection;
        this.createUserTable();
        this.createRoleTable();
        this.createPermissionTable();
        this.createBlogPostTable();
        this.createCommentTable();
    }

    public static void main(String[] args) {
        CreateTable ct = new CreateTable(new SetUpandConnectDb().setupAndConnectionDb());
    }

    public void createUserTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "Id INT PRIMARY KEY AUTO_INCREMENT," +
                "Username VARCHAR(50) NOT NULL," +
                "Password VARCHAR(50) NOT NULL," +
                "Role VARCHAR(50) NOT NULL," +
                "Permissions VARCHAR(50) NOT NULL" +
                ")";
        try {
            Statement s1 = this.connection.createStatement();
            s1.executeUpdate(query);
        } catch (SQLException var3) {
            var3.printStackTrace();
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

    public void createBlogPostTable() {
        String query = "CREATE TABLE IF NOT EXISTS blog_posts (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "title VARCHAR(100) NOT NULL," +
                "content TEXT NOT NULL," +
                "thumbnail_image VARCHAR(100)" +
                ")";
        try {
            Statement st = this.connection.createStatement();
            st.executeUpdate(this.usequery);
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCommentTable() {
        String query = "CREATE TABLE IF NOT EXISTS comments (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "post_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "content TEXT NOT NULL," +
                "FOREIGN KEY (post_id) REFERENCES blog_posts(id)," +
                "FOREIGN KEY (user_id) REFERENCES users(Id)" +
                ")";
        try {
            Statement st = this.connection.createStatement();
            st.executeUpdate(this.usequery);
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

