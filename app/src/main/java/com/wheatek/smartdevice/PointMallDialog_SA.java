package com.wheatek.smartdevice;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PointMallDialog_SA extends AlertDialog {

    private int[] currentindex;

    public int[] getCurrentindex() {
        return currentindex;
    }

    public void setCurrentindex(int[] currentindex) {
        this.currentindex = currentindex;
    }

    public PointMallDialog_SA(Context context) {
        super(context);
        init();
    }

    public PointMallDialog_SA(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public WheelProxyView viewHolder;

    View.OnClickListener surelistener;

    public View.OnClickListener getSurelistener() {
        return surelistener;
    }

    public void setSurelistener(View.OnClickListener surelistener) {
        this.surelistener = surelistener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_area_select_pmsf, ((ViewGroup) getWindow().getDecorView()), false);
        setContentView(view);
        viewHolder = new WheelProxyView(view, surelistener, this, getContext(), currentindex);
    }

}
