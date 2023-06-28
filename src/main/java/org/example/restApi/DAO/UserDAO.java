package org.example.restApi.DAO;

import org.example.db.SetUpandConnectDb;
import org.example.restApi.Entity.User;
import org.example.restApi.Exception.CustomException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        this.connection = new SetUpandConnectDb().setupAndConnectionDb();
    }

    public void addUser(User user) throws CustomException {
        String query = "INSERT INTO users (username, password, role_id, permission_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            int roleId = getRoleIdByName(user.getRole()); // Retrieve the role ID
            preparedStatement.setInt(3, roleId);
            int permissionId = getPermissionIdByName(user.getPermissions().toString()); // Retrieve the permission ID
            preparedStatement.setInt(4, permissionId);
            preparedStatement.executeUpdate();

            int userId = getUserIdByUsername(user.getUsername());
            insertUserPermissions(userId, user.getPermissions());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to add user.", e);
        }
    }


    public int getUserIdByUsername(String username) {
        String query = "SELECT id FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user ID.", e);
        }
        return -1; // User not found
    }

    private void insertUserPermissions(int userId, List<String> permissions) throws CustomException {
        String query = "INSERT INTO user_permissions (user_id, permission) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (String permission : permissions) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, permission);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to insert user permissions.", e);
        }
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
                user.setPermissions(getUserPermissions(userId));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user.", e);
        }
        return null; // User not found
    }

    private List<String> getUserPermissions(int userId) {
        List<String> permissions = new ArrayList<>();
        String query = "SELECT permission FROM user_permissions WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                permissions.add(resultSet.getString("permission"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user permissions.", e);
        }
        return permissions;
    }

    public void updateUser(User user) throws CustomException {
        String query = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

            updateUserPermissions(user.getId(), user.getPermissions());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to update user.", e);
        }
    }

    public void deleteUser(int userId) throws CustomException {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

            deleteAllUserPermissions(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to delete user.", e);
        }
    }

    public List<User> getAllUsers() {
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
                user.setPermissions(getUserPermissions(user.getId()));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve users.", e);
        }
        return userList;
    }

    private void updateUserPermissions(int userId, List<String> permissions) {
        String query = "INSERT INTO user_permissions (user_id, permission) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (String permission : permissions) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, permission);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to insert user permissions.", e);
        }
    }

    private void deleteAllUserPermissions(int userId) {
        String query = "DELETE FROM user_permissions WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user permissions.", e);
        }
    }

    public int getRoleIdByName(String roleName) throws CustomException {
        String query = "SELECT id FROM roles WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, roleName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new Exception("Role not found: ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to retrieve role ID.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getPermissionIdByName(String permissionName) throws CustomException {
        String query = "SELECT id FROM permissions WHERE name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, permissionName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new Exception("Permission not found: ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new CustomException("Failed to retrieve permission ID.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
