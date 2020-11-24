package com.cs3326.projectmeme.app.timeline;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cs3326.projectmeme.AppActivity;
import com.cs3326.projectmeme.R;
import com.cs3326.projectmeme.model.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Joiner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimelineFragment extends Fragment {// TODO: Implement adapter
    private Query mQuery;
    private FirestoreRecyclerOptions<Post> mOptions;
    private FirestoreRecyclerAdapter<Post, TimelineFragment.ProductViewHolder> mAdapter;
    private RecyclerView mPostsRecyclerView;
    private LinearLayoutManager mLayoutManager;

    FirebaseUser user;


    private TimelineViewModel mViewModel;
    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        // Query init for Adapter
        mQuery = FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("postedBy")
                .limit(50);

        // Options init for Adapter
        mOptions = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(mQuery, Post.class)
                .build();

        // Init adapter, sets up mapping
        mAdapter = new FirestoreRecyclerAdapter<Post, TimelineFragment.ProductViewHolder>(mOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TimelineFragment.ProductViewHolder holder, int position, @NonNull Post post) {
                holder.bind(post);
            }

            @NonNull
            @Override
            public TimelineFragment.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new TimelineFragment.ProductViewHolder(view);
            }
        };

        mLayoutManager = new LinearLayoutManager(((AppActivity)getActivity()));

        return inflater.inflate(R.layout.timeline_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TimelineViewModel.class);
        // TODO: Use the ViewModel
    }

    // Listeners for the Adapter
    @Override
    public void onStart() {
        super.onStart();
        // Update UI
        ((AppActivity)getActivity()).updateTimelineUI();

        // Update view on fragment load
        mPostsRecyclerView = getView().findViewById(R.id.recycler_view);
        mPostsRecyclerView.setLayoutManager(mLayoutManager);
        mPostsRecyclerView.setAdapter(mAdapter);

        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }



    // Post mapping Boi
    private class ProductViewHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView titleView;
        ImageView imageView;
        TextView likedbyView;
        TextView textView;
        TextView postedbyView;
        ImageButton likeButton;


        ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.post_item_image);
            titleView = itemView.findViewById(R.id.post_item_title);
            likedbyView = itemView.findViewById(R.id.post_item_likedby);
            textView = itemView.findViewById(R.id.post_item_text);
            postedbyView = itemView.findViewById(R.id.post_item_postedby);
            likeButton = itemView.findViewById(R.id.imageButtonLike);
        }

        void bind(final Post post) {

            postedbyView.setText(post.getPostedBy());
            textView.setText(post.getText());
            Glide.with(imageView.getContext())
                    .load(post.getImage())
                    .into(imageView);

<<<<<<< HEAD
=======
            if (post.getLikedBy() == null){
                likedbyView.setText("0 likes");

            } else {
                likedbyView.setText(post.getLikedBy().size() + " likes");

                if(post.getLikedBy().contains(user.getUid())){
                    likeButton.setImageResource(R.drawable.heart_liked);
                    likeButton.setColorFilter(Color.argb(255, 200, 0, 0));

                } else {
                    likeButton.setColorFilter(Color.argb(255, 100, 100, 100));
                    likeButton.setImageResource(R.drawable.heart_unliked);
                }
            }

            likeButton.setOnClickListener(new ImageButton.OnClickListener(){
                @Override
                public void onClick(View view) {

                    // get ref to the document
                    DocumentReference doc = FirebaseFirestore.getInstance().collection("posts").document(post.getDocumentId());
                    Map<String, Object> updates = new HashMap<>();

                    if(post.getLikedBy() != null && post.getLikedBy().contains(user.getUid())){
                        // Update UI & remove UID from array
                        updates.put("likedBy", FieldValue.arrayRemove(user.getUid()));
                    } else {
                        // Update UI & add UID from array
                        updates.put("likedBy", FieldValue.arrayUnion(user.getUid()));
                    }

                    // Push Updates
                    doc.update(updates);
                }
            });
>>>>>>> 0de764db2c5c39829e85b7da2d0e586a7316119e
        }
    }
}