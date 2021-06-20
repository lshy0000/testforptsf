package com.wheatek.smartdevice.uiv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.wheatek.smartdevice.AlerDialogUtils;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.MNETUtils;
import com.wheatek.smartdevice.myuriconnect.ShopBean;

import java.util.List;

public class ShopListMCF_V2 extends FragmentC_V2 implements PageIndexProxy.PageIndexChangeListener, LoadingDataI {
    private TextView mHinttext;
    private MFragmentAdapter mAdapter;
    PageIndexProxy pageIndexLay;
    private List<ShopBean> mlist;
    private int pageindex = 0;
    private int pageAll = 10;
    private int pageSize = 6;
    View view;
    private LinearLayout mPageindexlay;
    private ViewPager2 mViewpager;

    private View.OnClickListener mlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!pointMallCase.isNeedPhoneNumber()) {
                ShopBean bean = (ShopBean) v.getTag();
                ((ShopActivity) getActivity()).toDetail(bean);
            } else {
                AlerDialogUtils.createSureNoPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), null);
            }
        }
    };


    private ViewPager2.OnPageChangeCallback mcallb = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position2) {
            super.onPageSelected(position2);
            int position = position2 + 1;
            if (pageindex + 1 > position) {
                pageIndexLay.toPriPage(pageindex + 1 - position, false);
                pageindex = position2;
            } else if (pageindex + 1 < position) {
                pageIndexLay.toNextPage(position - pageindex - 1, false);
                pageindex = position2;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shoplist_fragment_v2, container, false);
        initView(view);
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh();
        }
    }

    public ShopListMCF_V2() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindDataView(String re, boolean success) {
        ((ShopActivity) getActivity()).cancelloadingView();
        if (!success) return;
        if (pointMallCase.getShopBeanList().size() != 0) {
            mHinttext.setText(getResources().getString(R.string.shoplist_hint_text_pmsf));
        }
        mAdapter.total = pointMallCase.getTotalpage();
        pageIndexLay.show(mAdapter.total, pageindex + 1);
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        mHinttext = view.findViewById(R.id.hinttext);
        mPageindexlay = (LinearLayout) view.findViewById(R.id.pageindexlay);
        mViewpager = (ViewPager2) view.findViewById(R.id.viewpager);
        pageIndexLay = new PageIndexProxy(mPageindexlay, getContext(), this);
        mAdapter = new MFragmentAdapter(0, getContext(), pointMallCase, pageSize, mlistener, this);
        mViewpager.setAdapter(mAdapter);
        mViewpager.registerOnPageChangeCallback(mcallb);
    }

    @Override
    public void loadPage(int position, int pagesize, MNETUtils.Callb callb) {
        ((ShopActivity) getActivity()).loadingView();
        pointMallCase.requestShopMap((v1, v2) -> {
            ((ShopActivity) getActivity()).cancelloadingView();
            if (v2) {
                if (callb != null) {
                    callb.response(v1, v2);
                }
            }
        }, position + 1, pageSize);
    }

    //第一次加载数据
    @Override
    public void refresh() {
//        if (pointMallCase.getMap().containsKey(pageindex) && pointMallCase.getMap().get(pageindex) != null) {
//            return;
//        }
        ((ShopActivity) getActivity()).loadingView();
        pointMallCase.requestShopMap((var1, var2) -> {
            bindDataView(var1, var2);
        }, pageindex + 1, pageSize);
    }

    @Override
    public void onPageIndexChange(int page) {
        pageindex = page - 1;
        mViewpager.unregisterOnPageChangeCallback(mcallb);
        mViewpager.setCurrentItem(page - 1);
        mViewpager.registerOnPageChangeCallback(mcallb);
    }

}
