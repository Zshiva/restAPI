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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Retrieve user ID from the database
        UserDAO userDAO = new UserDAO();
        int userId = userDAO.getUserIdByUsername(username);

        // Check if the user exists and verify the password
        if (userId != -1) {
            User user = new User();
            user.setId(userId);

            // Fetch the user's details from the database
            user = userDAO.getUserById(user.getId());

            if (PasswordUtils.verifyPassword(password, user.getPassword())) {
                // Authentication successful
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println("Login successful");
            } else {
                // Authentication failed
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().println("Invalid username or password");
            }
        } else {
            // User not found
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Invalid username or password");
        }
    }
}
