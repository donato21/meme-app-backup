package com.cs3326.projectmeme.model;

import android.net.Uri;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Post {

    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_TEXT = "text";

    private String image;
    private byte[] imageBytes;
    private List<String> likedBy;
    private String text;
    private static String postedBy;

    @DocumentId
    private String documentId;

    public Post(){}

    public Post (byte[] imageBytes, String image, List<String> likedBy, String text, String postedBy, String documentId) {
        this.image = image;
        this.likedBy = likedBy;
        this.text = text;
        this.postedBy = postedBy;
        this.documentId = documentId;
        this.imageBytes = imageBytes;
    }

    //Getter's and Setter's
    public String getDocumentId() {return documentId; }

    public void setDocumentId(String documentId){ this.documentId = documentId; }

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

