package org.example.restApi.Servlet;

import org.example.restApi.DAO.UserDAO;
import org.example.restApi.Entity.User;
import org.example.restApi.Exception.CustomException;
import org.example.restApi.Security.PasswordUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String role = req.getParameter("role");
        String[] permissions = req.getParameterValues("permissions");

        // Hash the password
        String hashedPassword = PasswordUtils.hashPassword(password);

        // Convert arrays to lists
        List<String> permissionsList;
        if (permissions != null) {
            permissionsList = Arrays.asList(permissions);
        } else {
            permissionsList = new ArrayList<>();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        if (role != null && role.equalsIgnoreCase("admin")) {
            user.setRole("admin");
        } else {
            user.setRole("user");
        }
        user.setPermissions(permissionsList);

        // Register the user
        UserDAO userDAO = new UserDAO();
        try {
            userDAO.addUser(user);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("User registered successfully");
        } catch (CustomException e) {
            // Handle the exception here
            e.printStackTrace(); // Example: Print the stack trace
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("An error occurred during user registration");
        }
    }
}
