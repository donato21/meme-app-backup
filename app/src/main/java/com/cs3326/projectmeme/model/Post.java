package com.cs3326.projectmeme.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import java.util.List;

public class Post {

    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_TEXT = "text";

    private String image;
    private byte[] imageBytes;
    private String profileImage;
    private List<String> likedBy;
    private String text;
    private static String postedBy;
    private static Timestamp created;
    private static String userId;

    @DocumentId
    private String documentId;

    public Post(){}

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

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) {  this.profileImage = profileImage; }

    public List<String> getLikedBy() { return likedBy; }

    public void setLikedBy(List<String> likedBy) { this.likedBy = likedBy; }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public String getPostedBy() { return postedBy; }

    public void setPostedBy(String postedBy) { Post.postedBy = postedBy; }

    public Timestamp getCreated() { return created; }

    public void setCreated(Timestamp date) { created = date; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

}

