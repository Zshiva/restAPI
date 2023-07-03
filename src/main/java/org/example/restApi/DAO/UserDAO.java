package org.example.restApi.DAO;

import org.example.restApi.Entity.User;
import org.example.restApi.Exception.CustomException;
import org.example.restApi.db.SetUpandConnectDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends User{

    private Connection connection = new SetUpandConnectDb().setupAndConnectionDb();
    private String usequery = "USE restapi;";

//    public static void main(String[] args) throws CustomException {
//        UserDAO userDAO = new UserDAO();
//        userDAO.addUser("John", "abc", "admin", "write posts");
//    }

    public void addUser(String Username, String Password, String Role, String Permissions) throws CustomException {
        String query = "INSERT INTO users(Username, Password, Role, Permissions) VALUES (?, ?, ?, ?)";
            try {
                Statement st1 = this.connection.createStatement();
                st1.executeUpdate(usequery);
                PreparedStatement st = this.connection.prepareStatement(query);
                st.setString(1, Username);
                st.setString(2, Password);
                st.setString(3, Role);
                st.setString(4, Permissions);
                st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to add user.", e);
        }
    }

    public List<User> getAllUsers() throws CustomException {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setPermissions(resultSet.getString("permissions"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to retrieve users.", e);
        }
        return userList;
    }

    public User getUserByUsername(String username) throws CustomException {
        String query = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setPermissions(resultSet.getString("permissions"));
                return user;
            } else {
                throw new CustomException("User not found for username: " + username);
            }
        } catch (SQLException e) {
            throw new CustomException("Failed to retrieve user.", e);
        }
    }
}
