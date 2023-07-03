package org.example.restApi.DAO;

import org.example.restApi.db.SetUpandConnectDb;
import org.example.restApi.Entity.BlogPost;
import org.example.restApi.Exception.CustomException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BlogPostDAO {
    private Connection connection;

    public BlogPostDAO() {
        this.connection = new SetUpandConnectDb().setupAndConnectionDb();
    }

    public void addBlogPost(BlogPost blogPost) throws CustomException {
        String query = "INSERT INTO blog_posts (title, content, thumbnail_image) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, blogPost.getTitle());
            preparedStatement.setString(2, blogPost.getContent());
            preparedStatement.setString(3, blogPost.getThumbnailImage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to add blog post.", e);
        }
    }

    public List<BlogPost> getAllBlogPosts() throws CustomException {
        List<BlogPost> blogPostList = new ArrayList<>();
        String query = "SELECT * FROM blog_posts";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String thumbnailImage = resultSet.getString("thumbnail_image");
                BlogPost blogPost = new BlogPost(id, title, content, thumbnailImage);
                blogPostList.add(blogPost);
            }
        } catch (SQLException e) {
            throw new CustomException("Failed to retrieve blog posts.", e);
        }
        return blogPostList;
    }

    public BlogPost getBlogPostById(int postId) throws CustomException {
        String query = "SELECT * FROM blog_posts WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String thumbnailImage = resultSet.getString("thumbnail_image");
                return new BlogPost(id, title, content, thumbnailImage);
            }
        } catch (SQLException e) {
            throw new CustomException("Failed to retrieve blog post.", e);
        }
        return null; // Blog post not found
    }

    public void updateBlogPost(BlogPost blogPost) throws CustomException {
        String query = "UPDATE blog_posts SET title = ?, content = ?, thumbnail_image = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, blogPost.getTitle());
            preparedStatement.setString(2, blogPost.getContent());
            preparedStatement.setString(3, blogPost.getThumbnailImage());
            preparedStatement.setInt(4, blogPost.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to update blog post.", e);
        }
    }

    public void deleteBlogPost(int postId) throws CustomException {
        String query = "DELETE FROM blog_posts WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, postId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to delete blog post.", e);
        }
    }
}
