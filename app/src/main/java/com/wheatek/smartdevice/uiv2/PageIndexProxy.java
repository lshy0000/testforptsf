package com.wheatek.smartdevice.uiv2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wheatek.smartdevice.R;

//分页控件组合布局
public class PageIndexProxy {

    ViewGroup parent;
    Context context;
    private TextView mPripagePmsf;
    private TextView mPriimgPmsf;
    private TextView mNPmsf;
    private TextView mMPmsfPmsf;
    private TextView mQPmsfPmsf;
    private TextView mNextimgPmsf;
    private TextView mNextpagePmsf;
    PageIndexChangeListener listener;

    public PageIndexProxy(ViewGroup parent, Context context, PageIndexChangeListener listener) {
        this.parent = parent;
        this.context = context;
        this.listener = listener;
        initView(parent);
    }

    int pageall, page;
    int currentind;

    public void show(int pageall, int page) {
        this.page = page;
        this.pageall = pageall;
        refresh(false);
    }

    public void refresh(boolean isnotify) {
        mQPmsfPmsf.setVisibility(pageall < 3 ? View.GONE : View.VISIBLE);
        mMPmsfPmsf.setVisibility(pageall < 2 ? View.GONE : View.VISIBLE);
        changeDrawableIscheck(mQPmsfPmsf, currentind == 2);
        changeDrawableIscheck(mNPmsf, currentind == 0);
        changeDrawableIscheck(mMPmsfPmsf, currentind == 1);
        mPripagePmsf.setEnabled(page != 1);
        mPriimgPmsf.setEnabled(page != 1);
        mNextimgPmsf.setEnabled(page != (pageall));
        mNextpagePmsf.setEnabled(page != (pageall));
        mNPmsf.setText(String.valueOf(page - currentind));
        mMPmsfPmsf.setText(String.valueOf(page - currentind + 1));
        mQPmsfPmsf.setText(String.valueOf(page - currentind + 2));
        if (isnotify) {
            if (listener != null) {
                listener.onPageIndexChange(page);
            }
        }
    }

    private void changeDrawableIscheck(TextView textView, boolean b) {
        if (b) {
            textView.setTextColor(context.getResources().getColor(R.color.white_pmsf, null));
            textView.setBackground(context.getResources().getDrawable(R.drawable.yuan_text_pmsf2, null));
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.text_primary_pmsf, null));
            textView.setBackground(context.getResources().getDrawable(R.drawable.yuan_text_pmsf, null));
        }
    }

    private void initView(ViewGroup view) {
        mPripagePmsf = (TextView) view.findViewById(R.id.pripage_pmsf);
        mPriimgPmsf = (TextView) view.findViewById(R.id.priimg_pmsf);
        mNPmsf = (TextView) view.findViewById(R.id.n_pmsf);
        mMPmsfPmsf = (TextView) view.findViewById(R.id.m_pmsf_pmsf);
        mQPmsfPmsf = (TextView) view.findViewById(R.id.q_pmsf_pmsf);
        mNextimgPmsf = (TextView) view.findViewById(R.id.nextimg_pmsf);
        mNextpagePmsf = (TextView) view.findViewById(R.id.nextpage_pmsf);
        mPriimgPmsf.setOnClickListener(v -> {
            toPriPage(1, true);
        });
        mPripagePmsf.setOnClickListener(v -> toPriPage(1, true));
        mNPmsf.setOnClickListener(v -> {
            if (currentind != 0) {
                page = page - currentind;
                currentind = 0;
                refresh(true);
            }

        });
        mQPmsfPmsf.setOnClickListener(v -> {
            if (currentind != 2) {
                page = page - currentind + 2;
                currentind = 2;
                refresh(true);
            }

        });
        mMPmsfPmsf.setOnClickListener(v -> {
            if (currentind != 1) {
                page = page - currentind + 1;
                currentind = 1;
                refresh(true);
            }
        });
        mNextimgPmsf.setOnClickListener(v -> toNextPage(1, true));
        mNextpagePmsf.setOnClickListener(v -> toNextPage(1, true));
    }

    public void toPriPage(int i, boolean b) {
        boolean refr = false;
        for (int j = 0; j < i; j++) {
            if (page > 1) {
                page--;
                if (currentind > 0)
                    currentind--;
                refr = true;
            }
        }
        if (refr) {
            refresh(b);
        }

    }

    public void toNextPage(int i, boolean b) {
        boolean refr = false;
        for (int j = 0; j < i; j++) {
            if (page < pageall) {
                page++;
                if (currentind < 2)
                    currentind++;
                refr = true;
            }
        }
        if (refr) {
            refresh(b);
        }
    }

    public static interface PageIndexChangeListener {
        void onPageIndexChange(int page);
    }
}
