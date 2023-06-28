package org.example.restApi.DAO;

import org.example.db.SetUpandConnectDb;
import org.example.restApi.Entity.Comments;
import org.example.restApi.Exception.CustomException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    private Connection connection;

    public CommentDAO() {
        this.connection = new SetUpandConnectDb().setupAndConnectionDb();
    }

    public void addComment(Comments comment) throws CustomException {
        String query = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, comment.getPostId());
            preparedStatement.setInt(2, comment.getUserId());
            preparedStatement.setString(3, comment.getContent());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to add comment.", e);
        }
    }

    public List<Comments> getCommentsByPostId(int postId) throws CustomException {
        List<Comments> commentList = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE post_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int userId = resultSet.getInt("user_id");
                String content = resultSet.getString("content");
                Comments comment = new Comments(id, postId, userId, content);
                commentList.add(comment);
            }
        } catch (SQLException e) {
            throw new CustomException("Failed to retrieve comments.", e);
        }
        return commentList;
    }

    public void updateComment(Comments comment) throws CustomException {
        String query = "UPDATE comments SET content = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, comment.getContent());
            preparedStatement.setInt(2, comment.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to update comment.", e);
        }
    }

    public void deleteComment(int commentId) throws CustomException {
        String query = "DELETE FROM comments WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, commentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CustomException("Failed to delete comment.", e);
        }
    }
}
