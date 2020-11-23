package com.cs3326.projectmeme.app.profile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs3326.projectmeme.AppActivity;
import com.cs3326.projectmeme.R;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    public TextView emailTextView, displayNameTextView;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View RootView = inflater.inflate(R.layout.profile_fragment, container, false);

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
        // TODO: Use the ViewModel
    }
}