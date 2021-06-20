package com.wheatek.smartdevice.uiv2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.wheatek.smartdevice.AlerDialogUtils;
import com.wheatek.smartdevice.LoadingView;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.CheckTextUtils;
import com.wheatek.smartdevice.myuriconnect.JSONHepler;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ShopBean;
import com.wheatek.smartdevice.myuriconnect.ToastUtils;

import static android.app.Activity.RESULT_OK;

public class PointsMallShareFragmentV2 extends Fragment implements LoadingView {
    private static final boolean DETAILOPEN = false; // 开启直接跳转到详情
    View view;
    PointMallCase pointMallCase;
    private FrameLayout mLeftlay;
    private LinearLayout mLeftlayPhone;
    private EditText mEditPhone;
    private Button mBtn;
    private LinearLayout mLeftlayQr;
    private ImageView mQrImage;
    private TextView mTexthintPhone;
    private TextView mPointScore;
    private TextView mBackChildfgmText;
    private TextView mMyOrderText;
    private TextView mhtml1;
    private TextView mhtml2;
    private FrameLayout mChildFmLay;
    private TextView mToshop;
    private ImageView mImage;
    private LinearLayout mImagemessage;
    private TextView mShopName;
    private TextView mShopPrice;
    private TextView mShopScore;
    private Button mToshopbtn;
    private FrameLayout mProgress;
    private TextView mCddxq;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.simple_mall_share_fragment_v2, null);
        initView(view);
        pointMallCase = PointMallCase.getInstance(getContext());
        toloading();
        togetRecommmendInfo();
        return view;
    }

    private void togetRecommmendInfo() {
        pointMallCase.requestGetRecommmendInfoForCache((var1, var2) -> {
            bindDataViewRecommend(var1, var2);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void toloading() {
        loadingView();
        pointMallCase.requestGetPointMallInfoFCache((var1, var2) -> {
            bindDataView(var1, var2);
        });
    }

    private void bindDataView(String var1, boolean var2) {
        cancelloadingView();
        if (!var2) {
            ToastUtils.getInstance(getContext()).showToast("network error" + var1.toString(), Toast.LENGTH_LONG);
            return;
        }
        boolean isneedphone = pointMallCase.isNeedPhoneNumber();
        showOrHindNeedPhoneView(isneedphone);
        if (!isneedphone) {
            String qr = JSONHepler.getQRfromResp(pointMallCase, var1);
            Glide.with(getContext()).load(qr).into(mQrImage);
            mTexthintPhone.setText(String.format(getResources().getString(R.string.prefix_number_pmsf), pointMallCase.getPhone()));
        }
        mPointScore.setText("" + pointMallCase.getScore());
    }

    private void bindDataViewRecommend(String var1, boolean var2) {
        if (!var2 || pointMallCase.getRecommendShopBean() == null) {
//            mImage.setVisibility(View.GONE);
//            mImagemessage.setVisibility(View.GONE);
//            this.currentShop = null;
        } else {
            this.currentShop = pointMallCase.getRecommendShopBean();
            mImage.setVisibility(View.VISIBLE);
            mImagemessage.setVisibility(View.VISIBLE);
//            Glide.
            Glide.with(getActivity()).load(currentShop.getImgUrlTump()).into(mImage);
            mShopName.setText(currentShop.getTitle());
            mShopPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //下划线
//            mShopPrice.getPaint().setAntiAlias(true);//抗锯齿
            mShopPrice.setText(String.format(getResources().getString(R.string.prefix_price_pmsf), currentShop.getPrice()));
            mShopScore.setText(String.valueOf(currentShop.getIntegral()));
            String s1 = String.format(getResources().getString(R.string.sharetext_friend_text_pmsf), pointMallCase.getFavorable1());
            mhtml1.setText(Html.fromHtml(s1, Html.FROM_HTML_MODE_LEGACY));
            String s2 = String.format(getResources().getString(R.string.sharehinttext_pmsf), pointMallCase.getFavorable2());
            mhtml2.setText(Html.fromHtml(s2, Html.FROM_HTML_MODE_LEGACY));
            mCddxq.setText(pointMallCase.getFavorable3());
        }
    }


    private void showOrHindNeedPhoneView(boolean isShow) {
        if (isShow) {
            mLeftlayQr.setVisibility(View.GONE);
            mLeftlayPhone.setVisibility(View.VISIBLE);
        } else {
            mLeftlayQr.setVisibility(View.VISIBLE);
            mLeftlayPhone.setVisibility(View.GONE);
        }
    }

    private void createDialog(String phone) {
        String realP = phone.replaceAll("-", "");
        String showtext;
        if (CheckTextUtils.isMobileNo(realP)) {
            showtext = realP.substring(0, 3) + "-" + realP.substring(3, 7) + "-" + realP.substring(7, 11);
        } else {
            ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.please_phone_right), Toast.LENGTH_LONG);
            return;
        }
        AlerDialogUtils.createSurePhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), showtext, v -> {
            loadingView();
            pointMallCase.requestUploadMobile(realP, (var1, var2) -> {
                bindDataView(var1, var2);
            });
        });
    }

    @Override
    public void cancelloadingView() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void loadingView() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void createDialog2() {
        AlerDialogUtils.createSureNoPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), null);
    }

    private void initView(View view) {
        mhtml1 = view.findViewById(R.id.html1);
        mhtml1.setText(Html.fromHtml(getResources().getString(R.string.sharetext_friend_text_pmsf), Html.FROM_HTML_MODE_LEGACY));
        mhtml2 = view.findViewById(R.id.html2);
        mhtml2.setText(Html.fromHtml(getResources().getString(R.string.sharehinttext_pmsf), Html.FROM_HTML_MODE_LEGACY));
        mLeftlay = view.findViewById(R.id.leftlay);
        mLeftlayPhone = view.findViewById(R.id.leftlay_phone);
        mEditPhone = view.findViewById(R.id.edit_phone);
        mBtn = view.findViewById(R.id.btn);
        mBtn.setOnClickListener(v -> createDialog(mEditPhone.getText().toString().trim()));
        mLeftlayQr = view.findViewById(R.id.leftlay_qr);
        mQrImage = view.findViewById(R.id.qr_image);
        mTexthintPhone = view.findViewById(R.id.texthint_phone);
        mPointScore = view.findViewById(R.id.point_score);
        mBackChildfgmText = view.findViewById(R.id.back_childfgm_text);
        mMyOrderText = view.findViewById(R.id.my_order_text);
        mChildFmLay = view.findViewById(R.id.child_fm_lay);
        mToshop = view.findViewById(R.id.toshop);
        mToshop.setOnClickListener(v -> toShop());
        mImage = view.findViewById(R.id.image);
        mImagemessage = view.findViewById(R.id.imagemessage);
        mShopName = view.findViewById(R.id.shop_name);
        mShopPrice = view.findViewById(R.id.shop_price);
        mShopScore = view.findViewById(R.id.shop_score);
        mToshopbtn = view.findViewById(R.id.toshopbtn);
        mToshopbtn.setOnClickListener(v -> toShop());
        mProgress = view.findViewById(R.id.progress);
        mImagemessage.setOnClickListener(v -> toShop());
        mImage.setOnClickListener(v -> toShop());
        mCddxq = view.findViewById(R.id.cddxq);

    }

    ShopBean currentShop;

    private void toShop() {
        if (pointMallCase.isNeedPhoneNumber()) {
            createDialog2();
            return;
        }
        Intent intent = new Intent(getContext(), ShopActivity.class);
        if (currentShop != null) {
            intent.putExtra("shopId", currentShop.getId());
            intent.putExtra("detailOpen", DETAILOPEN);
        }
        startActivityForResult(intent, 0x0021);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x0021 && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("finishthis", false)) {
                getActivity().finish();
            }
        }
    }

//    @Override
//    public void onChildFmChanged(int currentchildfgmIndex, Object object) {
//        mBackChildfgmText.setVisibility(currentchildfgmIndex == 0 ? View.GONE : View.VISIBLE);
//        if (currentchildfgmIndex == 2 && object != null && ((Boolean) object)) //购买成功后刷新数据
//            toloading();
//    }

}
