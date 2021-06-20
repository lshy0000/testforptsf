package com.wheatek.smartdevice.uiv2;

import android.annotation.Nullable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.wheatek.smartdevice.myuriconnect.PointMallCase;

public abstract class FragmentC_V2 extends Fragment {
    public abstract void refresh();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointMallCase = PointMallCase.getInstance(getContext());

    }

    PointMallCase pointMallCase;
}
