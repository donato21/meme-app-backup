package com.cs3326.projectmeme.app.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs3326.projectmeme.AppActivity;
import com.cs3326.projectmeme.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    public TextView emailTextView, displayNameTextView;
    public ImageView profileImageView;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
    ProfileImage iViewModel;
    public static final int SELECT_IMAGE_CODE = 11;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.profile_fragment, container, false);

        profileImageView = (ImageView) RootView.findViewById(R.id.image_view_profile);
        displayNameTextView = (TextView) RootView.findViewById(R.id.textViewDisplayName);
        emailTextView = (TextView) RootView.findViewById(R.id.textViewEmail);

        return RootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AppActivity)getActivity()).updateProfileUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        // Upload Image Logic
        iViewModel = new ProfileImage();
        iViewModel.init(getContext());
        startImageUploadListener();
    }

    public void processSelectedImage(Uri imgUri) {
        ImageView iv = new ImageView(getContext());
        iv.setImageURI(imgUri);

        Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        iViewModel.uploadImage(byteArrayOutputStream.toByteArray());
    }

    class ProfileImage extends ViewModel {
        FirebaseStorage firebaseStorage;
        private MutableLiveData<Boolean> imageUploadedSuccessfully;
        private WeakReference<Context> appContext;

        public void init(Context context) {
            appContext = new WeakReference<>(context);
            firebaseStorage = FirebaseStorage.getInstance();
            imageUploadedSuccessfully = new MutableLiveData<>(false);
        }

        public void uploadImage(byte[] imageBytes) {
            String fileName = UUID.randomUUID().toString();
            final StorageReference ref = firebaseStorage.getReference().child("images/"+ fileName);
            UploadTask uploadTask = ref.putBytes(imageBytes);

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
                        // Set user photo URI and build update
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();
                        final String TAG = "";

                        // Push User Update
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });
                    } else {
                        // Handle failures
                        // ...
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
    }

    public void startImageUploadListener() {
        iViewModel.getImageUploadedSuccessfully().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean imageUploadedSuccessfully) {
                if (imageUploadedSuccessfully) {
                    getActivity().onBackPressed();
                }
            }
        });
    }
}