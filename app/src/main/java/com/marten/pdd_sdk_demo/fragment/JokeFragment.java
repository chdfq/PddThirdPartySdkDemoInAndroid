package com.marten.pdd_sdk_demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marten.pdd_sdk_demo.R;
import com.marten.pdd_sdk_demo.databinding.FragmentJokeBinding;

public class JokeFragment extends Fragment {

    private Context context;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null){
            ViewGroup parten = (ViewGroup) view.getParent();
            if (parten != null){
                parten.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_joke, container, false);
        return view;
    }

}
