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

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user registration data
        String Username = request.getParameter("Username");
        String Password = PasswordUtils.hashPassword(request.getParameter("Password"));
        String Role = request.getParameter("Role");
        String Permissions = request.getParameter("Permissions");

        try {
            UserDAO userDAO= new UserDAO();
            // Add the user to the database using the UserDAO
            userDAO.addUser(Username, Password, Role, Permissions );

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("User registered successfully.");
        } catch (CustomException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to register user. Error: " + e.getMessage());
        }
    }
}
