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
import java.util.List;

@WebServlet("/comments")
public class CommentServlet extends HttpServlet {

    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        commentDAO = new CommentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String postId = req.getParameter("postId");

        try {
            List<Comments> comments = commentDAO.getCommentsByPostId(Integer.parseInt(postId));
            StringBuilder response = new StringBuilder();
            for (Comments comment : comments) {
                response.append("Post ID: ").append(comment.getPostId()).append("\n");
                response.append("Content: ").append(comment.getContent()).append("\n\n");
            }
            resp.setContentType("text/plain");
            resp.getWriter().write(response.toString());
        } catch (CustomException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("An error occurred while retrieving comments");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String postId = req.getParameter("postId");
        String content = req.getParameter("content");

        Comments comment = new Comments();
        comment.setPostId(Integer.parseInt(postId));
        comment.setContent(content);

        try {
            commentDAO.addComment(comment);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Comment added successfully");
        } catch (CustomException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("An error occurred while adding the comment");
        }
    }
}
