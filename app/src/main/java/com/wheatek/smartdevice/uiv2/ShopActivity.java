package com.wheatek.smartdevice.uiv2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wheatek.smartdevice.AlerDialogUtils;
import com.wheatek.smartdevice.LoadingView;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.MAddressInfo;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ShopBean;
import com.wheatek.smartdevice.myuriconnect.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements LoadingView {


    private static final int SHOPINDEX = 0;
    private static final int ORDERINDEX = 1;
    private static final int ADDRESSINFO = 3;
    private static final int SHOPDETAIL = 2;
    private static final int BACK = -1;
    private TextView mTitleShopindex;
    private TextView mTitleOrder;
    private TextView mTitleScore;
    private TextView mTitleMeasureindex;
    private TextView mMyScore;
    private FrameLayout mFragm;
    private TextView mBack;
    private TextView mToexchange;
    List<Integer> historyFms = new ArrayList();
    private FragmentManager fgm;
    private List<FragmentC_V2> mFragments = new ArrayList<>();
    private FrameLayout mProgress;

    @Override
    public void cancelloadingView() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void loadingView() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity_v2_pmsf);
        fgm = getSupportFragmentManager();
        pointMallCase = PointMallCase.getInstance(this);
        initView();
        //初始化fragments
        initf();
        //选择当前显示页
        if (getIntent() != null) {
            Intent intent = getIntent();
            int shopid = intent.getIntExtra("shopId", -1);
            boolean ishowdetail = intent.getBooleanExtra("detailOpen", false);
            if (shopid != -1 && ishowdetail) {
                showBean.shopBean = new ShopBean();
                showBean.shopBean.setId(shopid);
                // 先跳转到首页，后跳转到详情
                toChangePage(SHOPINDEX);
                getWindow().getDecorView().postDelayed(() -> {
                    toChangePage(SHOPDETAIL);
                }, 500);
            } else {
                toChangePage(SHOPINDEX);
            }
        } else {
            toChangePage(SHOPINDEX);
        }
    }

    private void initf() {
        mFragments.add(new ShopListMCF_V2());
        mFragments.add(new OrderHistoryMCF_V2());
        mFragments.add(new DetailFMCF());
        mFragments.add(new OrderMCF_V2());
        FragmentTransaction tra = fgm.beginTransaction();
        for (Fragment mFragment : mFragments) {
            tra.add(R.id.fragm, mFragment).hide(mFragment);
        }
        tra.commit();
    }

    private void initView() {
        mTitleShopindex = findViewById(R.id.title_shopindex);
        mTitleOrder = findViewById(R.id.title_order);
        mTitleScore = findViewById(R.id.title_score);
        mTitleMeasureindex = findViewById(R.id.title_measureindex);
        mMyScore = findViewById(R.id.my_score);
        mFragm = findViewById(R.id.fragm);
        mBack = findViewById(R.id.back);
        mToexchange = findViewById(R.id.toexchange);
        mTitleShopindex.setOnClickListener(v -> {
            toChangePage(SHOPINDEX);
        });
        mTitleOrder.setOnClickListener(v -> {
            toChangePage(ORDERINDEX);
        });
        mTitleScore.setOnClickListener(v -> toScore());
        mTitleMeasureindex.setOnClickListener(v -> {
            toMeasure();
        });
        mBack.setOnClickListener(v -> {
            toChangePage(BACK);
        });
        mToexchange.setOnClickListener(v -> {
            if (getCurrentPage() == ADDRESSINFO) {
                toBuySomething();
            } else {
                if (showBean.bugcount * showBean.shopBean.getIntegral() > pointMallCase.getScore()) {
                    AlerDialogUtils.createScoreNoPhoneDialog(((ViewGroup) getWindow().getDecorView()), ShopActivity.this, pointMallCase.getScore(), null);
                    return;
                }
                toChangePage(ADDRESSINFO);
            }
        });
        mProgress = (FrameLayout) findViewById(R.id.progress);
        mMyScore.setText(String.valueOf(pointMallCase.getScore()));
    }

    private void toScore() {
        finish();
    }

    private void toMeasure() {
        Intent intent = new Intent();
        intent.putExtra("finishthis", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int getCurrentPage() {
        if (historyFms.size() == 0) {
            return -1;
        }
        return historyFms.get(historyFms.size() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pointMallCase.releaseData();
    }

    /**
     * 当前浏览的数据结构类
     */
    public static class ShowBean {
        public ShopBean shopBean;
        public MAddressInfo addressInfo;
        public int bugcount;
        public String delivery;
    }

    public ShowBean showBean = new ShowBean();

    public ShowBean getShowBean() {
        return showBean;
    }

    public void setShowBean(ShowBean showBean) {
        this.showBean = showBean;
    }


    public void toDetail(ShopBean shopBean) {
        showBean.shopBean = shopBean;
        if (getCurrentPage() != SHOPDETAIL)
            toChangePage(SHOPDETAIL);
        else {
            mFragments.get(SHOPDETAIL).refresh();
        }
    }

    PointMallCase pointMallCase;

    private void toBuySomething() {
        if (getCurrentPage() == ADDRESSINFO && ((OrderMCF_V2) mFragments.get(ADDRESSINFO)).checkupload())
            pointMallCase.requestBuyShop(showBean.shopBean.getId(), showBean.bugcount, showBean.delivery,
                    getCurrentAddressInfo().getRecivername(),
                    getCurrentAddressInfo().getRecivernumber(),
                    getCurrentAddressInfo().getAddress(),
                    getCurrentAddressInfo().getAddressdetail(),
                    getCurrentAreaIndex(), (v1, v2) -> {
                        if (v2) {
                            createDialogToresult(((ViewGroup) getWindow().getDecorView()), ShopActivity.this);
                        } else {
                            ToastUtils.getInstance(getApplicationContext()).showToast(v1, Toast.LENGTH_LONG);
                        }
                    });

    }

    private static class D_R_ViewHolder {
        private Button mTomyorder;
        private Button mToshop;
        private Button mToscore;
        private Button mTomeasure;
        private TextView mCancel;

        private D_R_ViewHolder(View view) {
            mTomyorder = view.findViewById(R.id.tomyorder);
            mToshop = view.findViewById(R.id.toshop);
            mToscore = view.findViewById(R.id.toscore);
            mTomeasure = view.findViewById(R.id.tomeasure);
            mCancel = view.findViewById(R.id.cancel);
        }
    }

    public Dialog createDialogToresult(ViewGroup decorView, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_result_dialog, decorView, false);
        D_R_ViewHolder viewHolder = new D_R_ViewHolder(view);
        final Dialog dialog = new Dialog(context);
        viewHolder.mCancel.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });
        viewHolder.mTomeasure.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
            toMeasure();
        });
        viewHolder.mToshop.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
            toChangePage(SHOPINDEX);
        });
        viewHolder.mToscore.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
            toScore();
        });
        viewHolder.mTomyorder.setOnClickListener(v -> {
            if (dialog != null) {
                dialog.dismiss();
            }
            toChangePage(ORDERINDEX);
        });
        dialog.setContentView(view);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
//        dialog.getWindow().getDecorView().setBackgroundResource(R.drawable.select_dialog_bg_wyg);
        return dialog;
    }


    private MAddressInfo getCurrentAddressInfo() {
        MAddressInfo re = null;
        if (((OrderMCF_V2) mFragments.get(ADDRESSINFO)).getCurrentAddressInfo() != null) {
            re = ((OrderMCF_V2) mFragments.get(ADDRESSINFO)).getCurrentAddressInfo();
        }
        return re;
    }

    private int[] getCurrentAreaIndex() {
        int[] re = new int[3];
        if (((OrderMCF_V2) mFragments.get(ADDRESSINFO)).getCurrentAreaIndex() != null) {
            re = ((OrderMCF_V2) mFragments.get(ADDRESSINFO)).getCurrentAreaIndex();
        }
        return re;
    }

    public void toChangePage(int i) {
        int ol, ne;
        switch (i) {
            case BACK:
                ol = getCurrentPage();
                synchronized (historyFms) {
                    if (historyFms.size() > 1)
                        historyFms.remove(historyFms.size() - 1);
                    else return;
                }

                ne = getCurrentPage();
                doChangePage(ol, ne);
                break;
            case SHOPINDEX:
                ol = getCurrentPage();
                if (SHOPINDEX == ol) {
                    return;
                } else {
                    synchronized (historyFms) {
                        historyFms.clear();
                        historyFms.add(SHOPINDEX);
                    }
                    mTitleShopindex.setTextColor(getResources().getColor(R.color.primary_pmsf,null));
                    mTitleOrder.setTextColor(getResources().getColor(R.color.text_primary_pmsf,null));
                    ne = SHOPINDEX;
                    doChangePage(ol, ne);
                }
                break;
            case ORDERINDEX:
                ol = getCurrentPage();
                if (ORDERINDEX == ol) {
                    return;
                } else {
                    synchronized (historyFms) {
                        historyFms.clear();
                        historyFms.add(ORDERINDEX);
                    }
                    mTitleShopindex.setTextColor(getResources().getColor(R.color.text_primary_pmsf,null));
                    mTitleOrder.setTextColor(getResources().getColor(R.color.primary_pmsf,null));
                    ne = ORDERINDEX;
                    doChangePage(ol, ne);
                }
                break;
            case SHOPDETAIL:
                ol = getCurrentPage();
                if (SHOPDETAIL == ol) {
                    return;
                } else {
                    historyFms.add(SHOPDETAIL);
                    mTitleShopindex.setTextColor(getResources().getColor(R.color.text_primary_pmsf,null));
                    mTitleOrder.setTextColor(getResources().getColor(R.color.text_primary_pmsf,null));
                    mToexchange.setText(R.string.toexchange_text_pmsf);
                    ne = SHOPDETAIL;
                    doChangePage(ol, ne);
                }
                break;
            case ADDRESSINFO:
                ol = getCurrentPage();
                if (ADDRESSINFO == ol) {
                    return;
                } else {
                    historyFms.add(ADDRESSINFO);
                    mToexchange.setText(R.string.submit);
                    ne = ADDRESSINFO;
                    doChangePage(ol, ne);
                }
                break;
        }
    }

    public void doChangePage(int oldPage, int newpage) {
        if (newpage == SHOPDETAIL || newpage == ADDRESSINFO) {
            mBack.setVisibility(View.VISIBLE);
            mToexchange.setVisibility(View.VISIBLE);
        } else {
            mBack.setVisibility(View.GONE);
            mToexchange.setVisibility(View.GONE);
        }
        FragmentTransaction tra = fgm.beginTransaction();
        for (Fragment fragment : fgm.getFragments()) {
            if (fragment instanceof Fragment) {
                tra.hide(fragment);
            }
        }
        tra.show(mFragments.get(newpage));
        tra.commit();
    }
}
