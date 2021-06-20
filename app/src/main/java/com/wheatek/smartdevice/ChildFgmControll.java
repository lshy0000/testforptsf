package com.wheatek.smartdevice;

import android.annotation.NonNull;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wheatek.smartdevice.myuriconnect.PointMallCase;

import java.util.ArrayList;
import java.util.List;

public class ChildFgmControll {

    Context context;
    private FragmentManager fgm;
    Fragment pointsMallShareFragment;
    private int currentchildfgmIndex = -1;
    private PageChangeListener pageChangeListener;

    public void setPageChangeListener(PageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    List<MChildFragment> mChildFragments;

    public int getCurrentchildfgmIndex() {
        return currentchildfgmIndex;
    }

    public void setCurrentchildfgmIndex(int currentchildfgmIndex) {
        this.currentchildfgmIndex = currentchildfgmIndex;
    }

    public List<MChildFragment> getmChildFragments() {
        return mChildFragments;
    }

    public void setmChildFragments(@NonNull List<MChildFragment> mChildFragments) {
        this.mChildFragments = mChildFragments;
        initf();
        toIndexMCF();
    }

    public ChildFgmControll(Context context, Fragment pointsMallShareFragment) {
        this.context = context;
        this.pointsMallShareFragment = pointsMallShareFragment;
        fgm = this.pointsMallShareFragment.getChildFragmentManager();
    }

    public static List<MChildFragment> getDefaultChildFragments(PointMallCase pointMallCase, ChildFgmControll childFgmControll, LoadingView loadingView) {
        List<MChildFragment> re = new ArrayList<>();
        re.add(new ShopListMCF(pointMallCase, childFgmControll, loadingView));  // ---0
//        re.add(new ShoppDetailMCF(pointMallCase));
        re.add(new OrderMCF(pointMallCase, childFgmControll, loadingView));   // ---1
        re.add(new OrderResultMCF(pointMallCase, childFgmControll, loadingView)); // ---2
        re.add(new OrderHistoryMCF(pointMallCase, childFgmControll, loadingView)); // ---3
        return re;
    }

    public void toOrderMCF(int position) {
        int oldindex = currentchildfgmIndex;
        if (currentchildfgmIndex != 1) {
            currentchildfgmIndex = 1;
            ((OrderMCF) mChildFragments.get(currentchildfgmIndex)).setShopBean(position);
//            fgm.beginTransaction().replace(R.id.child_fm_lay, mChildFragments.get(currentchildfgmIndex)).commit();
//            fgm.beginTransaction().hide(mChildFragments.get(oldindex)).show(mChildFragments.get(currentchildfgmIndex)).commit();
            toHideAllAndShow(oldindex, currentchildfgmIndex);
            pageChanged(null);
        }
    }

    public void toIndexMCF() {
        int oldindex = currentchildfgmIndex;
        if (currentchildfgmIndex != 0) {
            currentchildfgmIndex = 0;
//            if (oldindex == -1) {
//                fgm.beginTransaction().show(mChildFragments.get(currentchildfgmIndex)).commit();
//            } else {
//                fgm.beginTransaction().hide(mChildFragments.get(oldindex)).show(mChildFragments.get(currentchildfgmIndex)).commit();
//            }
            toHideAllAndShow(oldindex, currentchildfgmIndex);
            pageChanged(null);
        }
    }

    private void initf() {
        FragmentTransaction tra = fgm.beginTransaction();
        for (MChildFragment fragment : mChildFragments) {
            tra.add(R.id.child_fm_lay, fragment);
            tra.hide(fragment);
        }
        tra.commit();
    }

    public void toHistoryMCF() {
        int oldindex = currentchildfgmIndex;
        if (currentchildfgmIndex != 3) {
            currentchildfgmIndex = 3;
            toHideAllAndShow(oldindex, currentchildfgmIndex);
            pageChanged(null);
        }
    }

    public void toOrderResultMCF(String var1, boolean success) {
        int oldindex = currentchildfgmIndex;
        if (currentchildfgmIndex != 2) {
            currentchildfgmIndex = 2;
            toHideAllAndShow(oldindex, currentchildfgmIndex);
            pageChanged(success);
        }

    }

    private void pageChanged(Object obj) {
        if (pageChangeListener != null) {
            pageChangeListener.onChildFmChanged(currentchildfgmIndex, obj);
        }
    }


    public void toHideAllAndShow(int oldindex, int newindex) {
        FragmentTransaction tra = fgm.beginTransaction();
        for (Fragment fragment : fgm.getFragments()) {
            if (fragment instanceof MChildFragment) {
                tra.hide(fragment);
            }
        }
        tra.show(mChildFragments.get(newindex));
        tra.commit();
    }


    public interface PageChangeListener {
        void onChildFmChanged(int currentchildindex, Object obj);
    }

}
