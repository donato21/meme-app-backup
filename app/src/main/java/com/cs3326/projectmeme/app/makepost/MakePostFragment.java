package com.cs3326.projectmeme.app.makepost;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs3326.projectmeme.R;


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
                if(TextUtils.isEmpty(editTextPostTitle.getText())){
                    Toast.makeText(getActivity(), "You did not enter a title", Toast.LENGTH_SHORT).show();
                }
                else if(createPostImageView.getDrawable()  == null){
                    Toast.makeText(getActivity(), "You did not select an image", Toast.LENGTH_SHORT).show();
                }
                else{
                    mViewModel.saveAndUploadPost();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MakePostViewModel.class);
        mViewModel.init(this);
        initObservableMethods();
    }

    public void processSelectedImage(Uri imageUri) {
        createPostImageView.setImageURI(imageUri);
    }

    private void initObservableMethods() {
        mViewModel.getImageUploadedSuccessfully().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean imageUploadedSuccessfully) {
                if(imageUploadedSuccessfully) {
                    getActivity().onBackPressed();
                }
            }
        });
    }

    public TextView getEditTextPostTitle() {
        return editTextPostTitle;
    }

    public ImageView getCreatePostImageView() {
        return createPostImageView;
    }
}