package com.example.doanltttbdd.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.utils.Utils;

public class UserFragment extends Fragment {

    TextView userNameTextView;
    boolean isSessionExists = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Utils.deleteSession(requireContext());
        isSessionExists = Utils.checkSession(requireContext());

        if (isSessionExists) {
            Utils.replaceFragment(new UserIsLoginFragment(), getParentFragmentManager());
        } else {
            Utils.replaceFragment(new UserNotLoggedFragment(), getParentFragmentManager());
        }

    }


}
