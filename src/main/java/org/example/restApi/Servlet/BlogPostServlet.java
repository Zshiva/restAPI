package org.example.restApi.Servlet;

import org.example.restApi.DAO.BlogPostDAO;
import org.example.restApi.DAO.CommentDAO;
import org.example.restApi.Entity.BlogPost;
import org.example.restApi.Entity.Comments;
import org.example.restApi.Exception.CustomException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/blog")
public class BlogPostServlet extends HttpServlet {
    private BlogPostDAO blogPostDAO;
    private CommentDAO commentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize BlogPostDAO and CommentDAO
        blogPostDAO = new BlogPostDAO();
        commentDAO = new CommentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve blog post data
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String thumbnailImage = request.getParameter("thumbnailImage");

        try {
            // Create a new BlogPost object
            BlogPost blogPost = new BlogPost();
            blogPost.setTitle(title);
            blogPost.setContent(content);
            blogPost.setThumbnailImage(thumbnailImage);

            // Add the blog post to the database using the BlogPostDAO
            blogPostDAO.addBlogPost(blogPost);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Blog post added successfully.");
        } catch (CustomException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to add blog post. Error: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve blog post ID parameter
        String postIdParam = request.getParameter("postId");

        if (postIdParam != null) {
            // Retrieve blog post details by ID
            int postId = Integer.parseInt(postIdParam);

            try {
                // Retrieve blog post from the database using the BlogPostDAO
                BlogPost blogPost = blogPostDAO.getBlogPostById(postId);

                if (blogPost != null) {
                    // Retrieve comments for the blog post using the CommentDAO
                    List<Comments> comments = commentDAO.getCommentsByPostId(blogPost.getId());

                    // Add the comments to the blog post object
                    blogPost.setComments(comments);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(blogPost);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().println("Blog post not found.");
                }
            } catch (CustomException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Failed to retrieve blog post. Error: " + e.getMessage());
            }
        } else {
            try {
                // Retrieve all blog posts from the database using the BlogPostDAO
                List<BlogPost> blogPosts = blogPostDAO.getAllBlogPosts();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(blogPosts);
            } catch (CustomException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().println("Failed to retrieve blog posts. Error: " + e.getMessage());
            }
        }
    }
}
