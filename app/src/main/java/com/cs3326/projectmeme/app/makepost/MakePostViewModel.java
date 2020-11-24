package com.cs3326.projectmeme.app.makepost;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cs3326.projectmeme.model.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

public class MakePostViewModel extends ViewModel {
    FirebaseStorage firebaseStorage;
    private MutableLiveData<Boolean> imageUploadedSuccessfully;
    private WeakReference<MakePostFragment> appContext;

    public void init(MakePostFragment makePostFragment) {
        appContext = new WeakReference<>(makePostFragment);
        firebaseStorage = FirebaseStorage.getInstance();
        imageUploadedSuccessfully = new MutableLiveData<>(false);
    }

    public void uploadImage(final String postId) {
        StorageReference storageReference = firebaseStorage.getReference();
        MakePostFragment makePostFragment = appContext.get();
        Bitmap bitmap = ((BitmapDrawable) makePostFragment.getCreatePostImageView().getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        final StorageReference ref = storageReference.child("images/"+postId);
        UploadTask uploadTask = ref.putBytes(byteArrayOutputStream.toByteArray());

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(appContext.get().getActivity(), "Uploaded image successfully in Storage!", Toast.LENGTH_SHORT).show();
                    String downloadUrl = task.getResult().toString();
                    updatePostItemWithUrl(postId, downloadUrl);
                }
                else {
                    Toast.makeText(appContext.get().getActivity(), "Failed to upload image or get image download URL", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public MutableLiveData<Boolean> getImageUploadedSuccessfully() {
        return imageUploadedSuccessfully;
    }

    public void setImageUploadedSuccessfully(MutableLiveData<Boolean> imageUploadedSuccessfully) {
        this.imageUploadedSuccessfully = imageUploadedSuccessfully;
    }

    public Post buildPostObj(){
        MakePostFragment makePostFragment = appContext.get();
        Post post = new Post();
        post.setPostedBy(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        post.setProfileImage(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        post.setText(makePostFragment.getEditTextPostTitle().getText().toString());
        post.setCreated(Timestamp.now());
        post.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        return post;
    }

    private void savePostToFirestore(Post post) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
        .add(post)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(appContext.get().getActivity(), "Post successfully saved without image url!", Toast.LENGTH_SHORT).show();
                Log.d("MakePostViewModel", "Post added with ID: " + documentReference.getId());
                uploadImage(documentReference.getId());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(appContext.get().getActivity(), "Post failed to save", Toast.LENGTH_SHORT).show();
                Log.d("MakePostViewModel", "Error adding post", e);
            }
        });
    }

    public void saveAndUploadPost() {
        Post post = buildPostObj();
        savePostToFirestore(post);
    }

    private void updatePostItemWithUrl(final String postId, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
        .document(postId)
        .update("image", imageUrl)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(appContext.get().getActivity(), "Updated post with the image url successfully!", Toast.LENGTH_SHORT).show();
                imageUploadedSuccessfully.setValue(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(appContext.get().getActivity(), "Update post with the image url failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}