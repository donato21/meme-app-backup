package com.cs3326.projectmeme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cs3326.projectmeme.app.makepost.MakePostFragment;
import com.cs3326.projectmeme.app.profile.ProfileFragment;
import com.cs3326.projectmeme.app.timeline.TimelineFragment;
import com.cs3326.projectmeme.model.Post;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class AppActivity extends AppCompatActivity {

    // TODO: Implement adapter
    private RecyclerView mPostsRecyclerView;
    private Query mQuery;
    private FirestoreRecyclerAdapter<Post, ProductViewHolder> mAdapter;
//    private PostAdapter mAdapter;
    private ViewGroup mEmptyView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseApp fbApp;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Query init for Adapter
        mQuery = FirebaseFirestore.getInstance()
                .collection("posts")
                .orderBy("postedBy")
                .limit(50);

        // Options init for Adapter
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(mQuery, Post.class)
                .build();

        // Init adapter, sets up mapping
        mAdapter = new FirestoreRecyclerAdapter<Post, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Post post) {
                holder.bind(post);
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new ProductViewHolder(view);
            }
        };

        // Check user logged in
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.actionbar, menu);
        // On init change to timeline frag
        FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
        getSupportFragmentManager().beginTransaction()
                .replace(contentView.getId(), new TimelineFragment())
                .commit();
        return true;
    }

    //Timeline Functions
        private void changeToTimelineFragment() {
            changeToFragment(new TimelineFragment());
        }

        public void onToTimelineClicked(View view) {
            changeToTimelineFragment();
        }

        public void updateTimelineUI() {
            // Enable profile icon
            menu.findItem(R.id.miProfile).setVisible(true);

            // Update view on fragment load
            mPostsRecyclerView = findViewById(R.id.recycler_view);
            mPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mPostsRecyclerView.setAdapter(mAdapter);
        }

    //Profile Functions
        private void changeToProfileFragment() {
            changeToFragment(new ProfileFragment());
        }

        public void onToProfileClicked(MenuItem mi) {
            changeToProfileFragment();
        }

        public void updateProfileUI() {
            menu.findItem(R.id.miProfile).setVisible(false);
            ProfileFragment pf = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.app_fragment_container);
            pf.displayNameTextView.setText(currentUser.getDisplayName());
            pf.emailTextView.setText(currentUser.getEmail());
        }

        public void onSignOutClick(View view){
            mAuth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    //Fragment Functions
        private void changeToFragment(Fragment fragment){
            FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);
            getSupportFragmentManager().beginTransaction()
                        .replace(contentView.getId(), fragment)
                        .addToBackStack(null)
                        .commit();
        }

        // Post mapping Boi
        private class ProductViewHolder extends RecyclerView.ViewHolder {
            private View view;
            TextView titleView;
            ImageView imageView;
            TextView likedbyView;
            TextView textView;
            TextView postedbyView;

            ProductViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                imageView = itemView.findViewById(R.id.post_item_image);
                titleView = itemView.findViewById(R.id.post_item_title);
                likedbyView = itemView.findViewById(R.id.post_item_likedby);
                textView = itemView.findViewById(R.id.post_item_text);
                postedbyView = itemView.findViewById(R.id.post_item_postedby);
            }

            void bind(Post post) {
                postedbyView.setText(post.getPostedBy());
                textView.setText(post.getText());
                Glide.with(imageView.getContext())
                        .load(post.getImage())
                        .into(imageView);
            }
        }

    // Listeners for the Adapter
    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }


    public void onFabCreatePostClick(View view) {
        FrameLayout contentView = (FrameLayout) findViewById(R.id.app_fragment_container);

        /*getSupportFragmentManager()
                .beginTransaction()
                .add(contentView.getId(), new MakePostFragment(), "MakePostFragment")
                .commit();*/

        changeToFragment(new MakePostFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MakePostFragment.SELECT_IMAGE_CODE) {
            /*MakePostFragment makePostFragment = (MakePostFragment) getSupportFragmentManager().findFragmentByTag("MakePostFragment");

            if(makePostFragment != null && data != null) {
                makePostFragment.processSelectedImage(data.getData());
            }
            else {
                Log.e("MakePostFragment", "makePostFragment or Intent data is null");
            }*/

            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

            for(Fragment fragment : fragmentList) {
                if(fragment instanceof MakePostFragment) {
                    MakePostFragment makePostFragment = (MakePostFragment) fragment;
                    if(data != null) {
                        Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show();
                        makePostFragment.processSelectedImage(data.getData());
                    }
                    else {
                        Log.e("MakePostFragment", "Intent data is null");
                    }
                    break;
                }
            }
        }
    }
}