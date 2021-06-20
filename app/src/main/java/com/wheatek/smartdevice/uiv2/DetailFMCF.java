package com.wheatek.smartdevice.uiv2;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.ShopBean;
import com.wheatek.smartdevice.myuriconnect.ToastUtils;

public class DetailFMCF extends FragmentC_V2 {

    private ImageView mImage;
    private TextView mName;
    private TextView mPrice;
    private TextView mScore;
    private TextView mDetail;
    private TextView mCalText;
    private TextView mMyScoreText;
    private RelativeLayout mJian;
    private TextView mCount;
    private RelativeLayout mJia;

    @Override
    public void refresh() {
        ((ShopActivity) getActivity()).loadingView();
        pointMallCase.requestShopDetail(((ShopActivity) getActivity()).showBean.shopBean.getId(), (v1, v2) -> {
            if (v2) {
                ((ShopActivity) getActivity()).showBean.shopBean = pointMallCase.getShopBeanDetail();
                shopBean = pointMallCase.getShopBeanDetail();
                bindDataView();
            }
        });
    }

    private void bindDataView() {
        ((ShopActivity) getActivity()).cancelloadingView();
        Glide.with(context).load(shopBean.getImgUrlTump()).into(mImage);
        mName.setText(shopBean.getTitle());
        mDetail.setText(shopBean.getMemo());
        mScore.setText(String.valueOf(shopBean.getIntegral()));
        mPrice.setText(String.format(getResources().getString(R.string.prefix_price_pmsf), shopBean.getPrice()));
        refreshText();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Context context;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();
        view = LayoutInflater.from(context).inflate(R.layout.detail_fgm_pmsf, container, false);
        initView(view);
        return view;
    }

    int count = 1;
    ShopBean shopBean;

    private void initView(View view) {
        mImage = view.findViewById(R.id.image);
        mName = view.findViewById(R.id.name);
        mPrice = view.findViewById(R.id.price);
        mScore = view.findViewById(R.id.score);
        mDetail = view.findViewById(R.id.detail);
        mCalText = view.findViewById(R.id.cal_text);
        mMyScoreText = view.findViewById(R.id.my_score_text);
        mJian = view.findViewById(R.id.jian);
        mCount = view.findViewById(R.id.count);
        mJian.setOnClickListener(v -> {
            if (count < 2) {
                ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.nocanjian_pmsf), Toast.LENGTH_LONG);
                return;
            }
            count--;
            refreshText();
        });
        mJia = view.findViewById(R.id.jia);
        mJia.setOnClickListener(v -> {
            count++;
            refreshText();
        });
    }

    private void refreshText() {
        mCount.setText(String.valueOf(count));
        String r = String.format(getResources().getString(R.string.buy_score_text_pmsf), count, count * shopBean.getIntegral());
        mCalText.setText(Html.fromHtml(r, Html.FROM_HTML_MODE_LEGACY));
        mCount.setText(String.valueOf(count));
        //通知activity 更新数目
        ((ShopActivity) getActivity()).showBean.bugcount = count;
        mMyScoreText.setText(String.format(getResources().getString(R.string.current_score_pmsf), pointMallCase.getScore()));
    }
}
