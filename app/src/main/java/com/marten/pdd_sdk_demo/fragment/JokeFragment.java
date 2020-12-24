package com.marten.pdd_sdk_demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marten.pdd_sdk_demo.databinding.FragmentJokeBinding;

public class JokeFragment extends Fragment {

    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentJokeBinding binding = FragmentJokeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

}
