package org.example.restApi.Servlet;

import org.example.restApi.DAO.BlogPostDAO;
import org.example.restApi.Entity.BlogPost;
import org.example.restApi.Exception.CustomException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@WebServlet("/blogPosts")
@MultipartConfig
public class BlogPostServlet extends HttpServlet {

    private BlogPostDAO blogPostDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        blogPostDAO = new BlogPostDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<BlogPost> blogPosts = blogPostDAO.getAllBlogPosts();
            StringBuilder response = new StringBuilder();
            for (BlogPost blogPost : blogPosts) {
                response.append("Title: ").append(blogPost.getTitle()).append("\n");
                response.append("Content: ").append(blogPost.getContent()).append("\n");
                response.append("Thumbnail: ").append(blogPost.getThumbnailImage()).append("\n\n");
            }
            resp.setContentType("text/plain");
            resp.getWriter().write(response.toString());
        } catch (CustomException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("An error occurred while retrieving blog posts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        Part thumbnailPart = req.getPart("thumbnail");

        // Save the thumbnail image to a directory
        String thumbnailFileName = saveThumbnailImage(thumbnailPart);

        BlogPost blogPost = new BlogPost();
        blogPost.setTitle(title);
        blogPost.setContent(content);
        blogPost.setThumbnailImage(thumbnailFileName);

        try {
            blogPostDAO.addBlogPost(blogPost);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Blog post added successfully");
        } catch (CustomException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("An error occurred while adding the blog post");
        }
    }

    private String saveThumbnailImage(Part thumbnailPart) throws IOException {
        // Generate a unique file name for the thumbnail
        String thumbnailFileName = generateUniqueFileName();

        // Specify the directory to save the thumbnail image
        String thumbnailDirectory = ""; // Replace with your desired directory

        try (InputStream inputStream = thumbnailPart.getInputStream()) {
            // Save the thumbnail image to the directory
            Files.copy(inputStream, Paths.get(thumbnailDirectory, thumbnailFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return thumbnailFileName;
    }

    private String generateUniqueFileName() {
        long timestamp = System.currentTimeMillis();
        return "thumbnail_" + timestamp + ".jpg";
    }
}
