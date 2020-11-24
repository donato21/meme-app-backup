package com.cs3326.projectmeme.app.makepost;

import android.content.Context;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MakePostViewModel extends ViewModel {
    FirebaseStorage firebaseStorage;
    private MutableLiveData<Boolean> imageUploadedSuccessfully;
    private WeakReference<Context> appContext;
    private Uri dUri;
    FirebaseFirestore db;

    public void init(Context context) {
        appContext = new WeakReference<>(context);
        firebaseStorage = FirebaseStorage.getInstance();
        imageUploadedSuccessfully = new MutableLiveData<>(false);
    }

    public void uploadPost(Post post) {

        if (post.getImageBytes() != null) {
            String fileName = UUID.randomUUID().toString();
            final StorageReference ref = firebaseStorage.getReference().child("images/" + fileName);
            UploadTask uploadTask = ref.putBytes(post.getImageBytes());

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(appContext.get().getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(appContext.get().getApplicationContext(), "Uploaded image successfully!", Toast.LENGTH_SHORT).show();
                    imageUploadedSuccessfully.setValue(true);
                }
            });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // URI!!! POG
                        Uri downloadUri = task.getResult();
                        setUri(downloadUri);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }

        post.setImage(dUri.toString());
        savePostToFirestore(post);
    }

    private void savePostToFirestore(Post post) {
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");
        db.collection("posts").add(city).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {

                Log.d("", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("", "Error writing document", e);
            }
        });
    }

    private void setUri(Uri uri) {
        dUri = uri;
    }

    public MutableLiveData<Boolean> getImageUploadedSuccessfully() {
        return imageUploadedSuccessfully;
    }

    public void setImageUploadedSuccessfully(MutableLiveData<Boolean> imageUploadedSuccessfully) {
        this.imageUploadedSuccessfully = imageUploadedSuccessfully;
    }
}