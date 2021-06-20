package com.wheatek.smartdevice.uiv2;

import com.wheatek.smartdevice.myuriconnect.MNETUtils;

public interface LoadingDataI {
    void loadPage(int pageindex, int pagesize, MNETUtils.Callb callb);
}