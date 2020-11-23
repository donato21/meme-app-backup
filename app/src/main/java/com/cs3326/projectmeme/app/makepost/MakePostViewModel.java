package com.cs3326.projectmeme.app.makepost;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cs3326.projectmeme.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.type.DateTime;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class MakePostViewModel extends ViewModel {
    FirebaseStorage firebaseStorage;
    private MutableLiveData<Boolean> imageUploadedSuccessfully;
    private WeakReference<Context> appContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");

    public void init(Context context) {
        appContext = new WeakReference<>(context);
        firebaseStorage = FirebaseStorage.getInstance();
        imageUploadedSuccessfully = new MutableLiveData<>(false);
    }

    public void uploadPost(Post post) {
        String postId = savePostToDB(post);
        StorageReference storageReference = firebaseStorage.getReference();

        String fileName = dateFormat.format(new Date());
        UploadTask uploadTask = storageReference.child("images/"+ fileName).putBytes(post.getImageBytes());

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
    }

    private String savePostToDB(Post post) {

        return "id";
    }

    public MutableLiveData<Boolean> getImageUploadedSuccessfully() {
        return imageUploadedSuccessfully;
    }

    public void setImageUploadedSuccessfully(MutableLiveData<Boolean> imageUploadedSuccessfully) {
        this.imageUploadedSuccessfully = imageUploadedSuccessfully;
    }
}