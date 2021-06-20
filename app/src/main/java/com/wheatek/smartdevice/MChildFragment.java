package com.wheatek.smartdevice;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wheatek.smartdevice.myuriconnect.PointMallCase;

public class MChildFragment extends Fragment {
    PointMallCase pointMallCase;
    ChildFgmControll childFgmControll;
    LoadingView loadingView;

    public MChildFragment() {
    }

    public MChildFragment(PointMallCase pointMallCase, ChildFgmControll childFgmControll, LoadingView loadingView) {
        this.pointMallCase = pointMallCase;
        this.childFgmControll = childFgmControll;
        this.loadingView = loadingView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        if (getParentFragment() != null && getParentFragment() instanceof PointsMallShareFragment) {
            if (pointMallCase == null)
                pointMallCase = PointMallCase.getInstance(getParentFragment().getContext());
            childFgmControll = ((PointsMallShareFragment) getParentFragment()).childFgmControll;
            loadingView = ((PointsMallShareFragment) getParentFragment());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
