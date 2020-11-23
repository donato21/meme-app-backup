package com.cs3326.projectmeme.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cs3326.projectmeme.R;
import com.cs3326.projectmeme.model.Post;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class PostAdapter extends FirestoreAdapter<PostAdapter.ViewHolder>{
    public interface OnPostSelecterListener {
        void onPostSelected(DocumentSnapshot post);
    }

    private OnPostSelecterListener mListener;

    public PostAdapter(Query query, OnPostSelecterListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    //Class: Maps data to a view dynamically from input of DB
    static class ViewHolder extends RecyclerView.ViewHolder {
        //Different attributes of view based off Post Model
        TextView titleView;
        ImageView imageView;
        TextView likedbyView;
        TextView textView;
        TextView postedbyView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.post_item_image);
            titleView = itemView.findViewById(R.id.post_item_title);
            likedbyView = itemView.findViewById(R.id.post_item_likedby);
            textView = itemView.findViewById(R.id.post_item_text);
            postedbyView = itemView.findViewById(R.id.post_item_postedby);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnPostSelecterListener listener) {
            Post post = snapshot.toObject(Post.class);        //Maps each object to a Post object
            Resources resources = itemView.getResources();//Not sure what this does? I think its the icons

            // Load image
            Glide.with(imageView.getContext())
                    .load(post.getImage())
                    .into(imageView);

            titleView.setText(post.getTitle());
            textView.setText(post.getText());
            // TODO: likebyView.setText()
            postedbyView.setText(post.getPostedBy());
        }

        // TODO: Click Listener(Optional)

    }
}

