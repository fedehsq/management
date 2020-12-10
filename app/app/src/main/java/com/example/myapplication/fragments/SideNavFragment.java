package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.activities.ShowCompanyActivity;

/**
 * create an instance of this fragment.
 */
public class SideNavFragment extends Fragment implements View.OnClickListener {

    private LinearLayout trucksLinearLayout;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trucksLinearLayout = view.findViewById(R.id.trucks_linear_layout);
        trucksLinearLayout.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.side_nav_fragment, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v == trucksLinearLayout) {
            startActivity(new Intent(getContext(), ShowCompanyActivity.class));
        }
    }
}