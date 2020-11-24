package com.cs3326.projectmeme.model;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Post {

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_TEXT = "text";

    private String title;
    private String image;
    private byte[] imageBytes;
    private List<String> likedBy;
    private String text;
    private static String postedBy;

    @DocumentId
    private String documentId;

    public Post(){}

    public Post(String title, byte[] imageBytes, List<String> likedBy, String text, String postedBy, String documentId) {
        this.title = title;
        this.image = image;
        this.imageBytes = imageBytes;
        this.likedBy = likedBy;
        this.text = text;
        this.postedBy = postedBy;
        this.documentId = documentId;
    }

    //Getter's and Setter's
    public String getDocumentId() {return documentId; }

    public void setDocumentId(String documentId){ this.documentId = documentId; }


    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getLikedBy() { return likedBy; }

    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getPostedBy() { return postedBy; }

    public void setPostedBy(String postedBy) { Post.postedBy = postedBy; }

}

