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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize UserDAO
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve user login credentials
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Check if the user exists in the database
            User user = userDAO.getUserByUsername(username);

            if (user != null && PasswordUtils.verifyPassword(password, user.getPassword())) {
                // User login successful
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Login successful. Welcome, " + user.getUsername() + "!");
            } else {
                // User login failed
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().println("Invalid username or password.");
            }
        } catch (CustomException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to perform login. Error: " + e.getMessage());
        }
    }
}