package com.cs3326.projectmeme.app.viewpost;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cs3326.projectmeme.R;
import com.cs3326.projectmeme.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ViewPostFragment extends Fragment {

    private ViewPostViewModel mViewModel;
    private static Post post;
    FirebaseFirestore db;
    FirebaseUser user;
    ImageView profileView;
    TextView postedbyView;
    TextView textView;
    ImageView imageView;
    private ImageView likeButton, sparkles;

    public static ViewPostFragment newInstance() {
        return new ViewPostFragment();
    }

    // Feed post data to local variable
    public static void init(Post ip){
        post = ip;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_post_fragment, container, false);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        profileView = view.findViewById(R.id.image_view_profile);
        postedbyView = view.findViewById(R.id.post_item_postedby);
        textView = view.findViewById(R.id.post_item_text);
        imageView = view.findViewById(R.id.post_item_image);
        likeButton = view.findViewById(R.id.like_image_view);
        sparkles = view.findViewById(R.id.sparkles_image_view);
        sparkles.setVisibility(ImageView.INVISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewPostViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();

        // Push post data to views
        Glide.with(profileView.getContext())
                .load(post.getProfileImage())
                .into(profileView);

        postedbyView.setText(post.getPostedBy());

        textView.setText(post.getText());

        Glide.with(imageView.getContext())
                .load(post.getImage())
                .into(imageView);

        if (post.getLikedBy() != null) {
            if (post.getLikedBy().contains(user.getUid())) {
                setHeart(true);
            } else {
                setHeart(false);
            }
        } else {
            setHeart(false);
        }

        // Update like button on click
        likeButton.setOnClickListener(new ImageButton.OnClickListener() {
            boolean likeClicked = false;

            @Override
            public void onClick(View view) {

                // get ref to the document
                DocumentReference doc = FirebaseFirestore.getInstance().collection("posts").document(post.getDocumentId());
                Map<String, Object> updates = new HashMap<>();

                if (post.getLikedBy() != null && post.getLikedBy().contains(user.getUid()) && !likeClicked) {
                    // Update UI & remove UID from array
                    updates.put("likedBy", FieldValue.arrayRemove(user.getUid()));

                    setHeart(false);
                    likeClicked = true;
                } else if (post.getLikedBy() != null && post.getLikedBy().contains(user.getUid()) && likeClicked) {
                    // Update UI & add UID from array
                    updates.put("likedBy", FieldValue.arrayUnion(user.getUid()));

                    setHeart(true);
                    likeClicked = false;
                } else if (post.getLikedBy() == null || !post.getLikedBy().contains(user.getUid()) && !likeClicked) {
                    // Update UI & add UID from array
                    updates.put("likedBy", FieldValue.arrayUnion(user.getUid()));

                    setHeart(true);
                    likeClicked = true;
                } else if (post.getLikedBy() == null || !post.getLikedBy().contains(user.getUid()) && likeClicked) {
                    // Update UI & remove UID from array
                    updates.put("likedBy", FieldValue.arrayRemove(user.getUid()));

                    setHeart(false);
                    likeClicked = false;
                }
                // Push Updates
                doc.update(updates);
            }
        });
    }

    private void setHeart(Boolean liked) {
        if (liked) {
            likeButton.setImageResource(R.drawable.heart_liked);
            likeButton.setColorFilter(Color.argb(255, 200, 0, 0));
            sparkles.setVisibility(ImageView.VISIBLE);
        }
        else {
            likeButton.setColorFilter(Color.argb(255, 100, 100, 100));
            likeButton.setImageResource(R.drawable.heart_liked);
            sparkles.setVisibility(ImageView.INVISIBLE);
        }
    }
}