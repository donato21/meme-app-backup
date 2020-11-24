package com.cs3326.projectmeme.app.makepost;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs3326.projectmeme.R;
import com.cs3326.projectmeme.model.Post;

import java.io.ByteArrayOutputStream;

public class MakePostFragment extends Fragment {
    private ImageView createPostImageView;
    private Button selectImageButton;
    private Button addPostButton;
    private TextView editTextPostTitle;
    private MakePostViewModel mViewModel;

    public static final int SELECT_IMAGE_CODE = 10;

    public static MakePostFragment newInstance() {
        return new MakePostFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.make_post_fragment, container, false);
        selectImageButton = (Button) view.findViewById(R.id.buttonSelectImage);
        addPostButton = (Button) view.findViewById(R.id.buttonCreatePost);
        editTextPostTitle = (TextView) view.findViewById(R.id.editTextPostTitle);
        createPostImageView = (ImageView) view.findViewById(R.id.create_post_image_view);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE_CODE);
            }
        });

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post post = buildPostObj();
                mViewModel.uploadPost(post);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MakePostViewModel.class);
        mViewModel.init(getContext());
        initObservableMethods();
    }

    public void processSelectedImage(Uri imageUri) {
        createPostImageView.setImageURI(imageUri);
    }

    public Post buildPostObj() {
        Post post = new Post();
        if (createPostImageView.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) createPostImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            post.setImageBytes(byteArrayOutputStream.toByteArray());
        }
        post.setTitle(editTextPostTitle.getText().toString());
        return post;
    }

    private void initObservableMethods() {
        mViewModel.getImageUploadedSuccessfully().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean imageUploadedSuccessfully) {
                if(imageUploadedSuccessfully) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

}