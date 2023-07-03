package org.example.restApi.Entity;

import java.util.List;

public class BlogPost {
    private int id;
    private String title;
    private String content;
    private String thumbnailImage;
    private List<Comments> comments;

    public BlogPost() {
    }

    public BlogPost(int id, String title, String content, String thumbnailImage) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnailImage = thumbnailImage;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
