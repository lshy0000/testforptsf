package com.wheatek.smartdevice;

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
import com.wheatek.smartdevice.myuriconnect.CheckTextUtils;
import com.wheatek.smartdevice.myuriconnect.JSONHepler;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ToastUtils;

import java.util.List;

public class PointsMallShareFragment extends Fragment implements ChildFgmControll.PageChangeListener, LoadingView {
    public static String SHOPLINKEXTR = "shoplink";
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
    private View progress;
    private FrameLayout mChildFmLay;
    private int currentchildfgmIndex = 0;
    List<MChildFragment> mChildFragments;
    public ChildFgmControll childFgmControll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.simple_mall_share_fragment, null);
        initView(view);
        pointMallCase = PointMallCase.getInstance(getContext());
        childFgmControll = new ChildFgmControll(getContext(), this);
        childFgmControll.setPageChangeListener(this);
        childFgmControll.setmChildFragments(ChildFgmControll.getDefaultChildFragments(pointMallCase, childFgmControll, this));
        toloading();
        return view;
    }

    @Override
    public void onResume() {
//        toloading();
        super.onResume();
    }

    private void toloading() {
        loadingView();
        pointMallCase.requestGetPointMallInfo((var1, var2) -> {
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
        if (isneedphone) {
//            return;
        }
        String qr = JSONHepler.getQRfromResp(pointMallCase, var1);
        Glide.with(getContext()).load(qr).into(mQrImage);
        mPointScore.setText("" + pointMallCase.getScore());
        mTexthintPhone.setText(String.format(getResources().getString(R.string.prefix_number_pmsf), pointMallCase.getPhone()));
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
        progress.setVisibility(View.GONE);
    }

    @Override
    public void loadingView() {
        progress.setVisibility(View.VISIBLE);
    }

    private void createDialog2() {
        AlerDialogUtils.createSureNoPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), null);
    }

    private void initView(View view) {
        progress = view.findViewById(R.id.progress);
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
        mBackChildfgmText.setOnClickListener(v -> childFgmControll.toIndexMCF());
        mMyOrderText = view.findViewById(R.id.my_order_text);

        mMyOrderText.setOnClickListener(v -> {
            if (!pointMallCase.isNeedPhoneNumber())
                childFgmControll.toHistoryMCF();
            else {
                createDialog2();
            }
        });
        mChildFmLay = view.findViewById(R.id.child_fm_lay);
    }

    public void onChildFmChanged(int currentchildfgmIndex, Object object) {
        mBackChildfgmText.setVisibility(currentchildfgmIndex == 0 ? View.GONE : View.VISIBLE);
        if (currentchildfgmIndex == 2 && object != null && ((Boolean) object)) //购买成功后刷新数据
            toloading();
    }


}
