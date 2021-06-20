package com.wheatek.smartdevice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wheatek.smartdevice.myuriconnect.PointMallCase;

public class OrderResultMCF extends MChildFragment {
    private Button mSubmit;

    public OrderResultMCF(PointMallCase pointMallCase, ChildFgmControll childFgmControll, LoadingView loadingView) {
        super(pointMallCase, childFgmControll, loadingView);
    }

    View view;

    public OrderResultMCF() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.order_result_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSubmit = view.findViewById(R.id.submit);
        mSubmit.setOnClickListener(v -> childFgmControll.toIndexMCF());
    }
}
