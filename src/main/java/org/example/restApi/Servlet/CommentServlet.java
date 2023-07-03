package org.example.restApi.Servlet;

import org.example.restApi.DAO.CommentDAO;
import org.example.restApi.Entity.Comments;
import org.example.restApi.Exception.CustomException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize CommentDAO
        commentDAO = new CommentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the comment data from the request parameters
        int postId = Integer.parseInt(request.getParameter("postId"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        String content = request.getParameter("content");

        try {
            // Create a new Comments object
            Comments comment = new Comments();
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setContent(content);

            // Add the comment to the database using the CommentDAO
            commentDAO.addComment(comment);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Comment added successfully.");
        } catch (CustomException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to add comment. Error: " + e.getMessage());
        }
    }
}

