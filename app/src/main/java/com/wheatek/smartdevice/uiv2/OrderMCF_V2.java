package com.wheatek.smartdevice.uiv2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wheatek.smartdevice.AlerDialogUtils;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.WheelProxyView;
import com.wheatek.smartdevice.myuriconnect.CheckTextUtils;
import com.wheatek.smartdevice.myuriconnect.MAddressInfo;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ShopBean;
import com.wheatek.smartdevice.myuriconnect.ToastUtils;

public class OrderMCF_V2 extends FragmentC_V2 {
    View view;
    private TextView mName;
    private TextView mJian;
    private EditText mCount;
    private TextView mJia;
    private EditText mNumber;
    private TextView mArea;
    private EditText mDeliver;
    private EditText mDetail;
    private EditText mDetailInput;
    private TextView mCalText;
    private TextView mMyScoreText;
    private Button mSubmit;
    private ShopBean shopBean;
    private int ct;
    private Dialog pointMallDialog_sa;
    private int[] currentindex = new int[3];
    private TextView mClearText;
    private EditText mRevicername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.order_fragment_v2, container, false);
        if (pointMallCase == null) {
            pointMallCase = PointMallCase.getInstance(getContext());
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        mName = view.findViewById(R.id.name);
        mJian = view.findViewById(R.id.jian);
        mCount = view.findViewById(R.id.count);
        mCount.setText("1");
        mJia = view.findViewById(R.id.jia);
        mNumber = view.findViewById(R.id.number);
        mArea = view.findViewById(R.id.area);
        mDetail = view.findViewById(R.id.detail);
        mDetailInput = view.findViewById(R.id.detail_input);
        mCalText = view.findViewById(R.id.cal_text);
        mMyScoreText = view.findViewById(R.id.my_score_text);
        mDeliver = view.findViewById(R.id.deliver);
        mSubmit = view.findViewById(R.id.submit);
        mClearText = view.findViewById(R.id.clear_text);
        mRevicername = view.findViewById(R.id.revicername);
        mJia.setOnClickListener(v -> {
            changect(ct + 1, false);
        });
        mJian.setOnClickListener(v -> {
            if (ct > 1) {
                changect(ct - 1, false);
            } else {
                ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.nocanjian_pmsf), Toast.LENGTH_LONG);
            }
        });
        mSubmit.setOnClickListener(v -> {
            checkupload();
        });
        mDetailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mDetail.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mArea.setOnClickListener(v -> {
            createdddd();
        });
        mClearText.setOnClickListener(v -> {
            mNumber.setText("");
        });

    }

    private void createdddd() {
        if (pointMallDialog_sa == null) {
            pointMallDialog_sa = AlerDialogUtils.createSelectAreaPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext()
                    , currentindex, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WheelProxyView viewHolder = ((WheelProxyView) v.getTag());
                            currentindex = new int[]{viewHolder.currentindex[0],
                                    viewHolder.currentindex[1],
                                    viewHolder.currentindex[2]};
                            mArea.setText(MAddressInfo.getAddressfromindex(getContext(), currentindex));
                        }
                    });
        }
        if (!pointMallDialog_sa.isShowing()) {
            pointMallDialog_sa.show();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            binddata(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void binddata(boolean init) {
        shopBean = ((ShopActivity) getActivity()).showBean.shopBean;
        if (shopBean == null) return;
        ct = ((ShopActivity) getActivity()).showBean.bugcount;
        mNumber.setText(pointMallCase.getPhone());
        if (pointMallCase.getAddressInfo() != null) {
            mNumber.setText(pointMallCase.getAddressInfo().getRecivernumber());
            mArea.setText(pointMallCase.getAddressInfo().getAddress());
            mDetail.setText(pointMallCase.getAddressInfo().getAddressdetail());
            mDetailInput.setText(pointMallCase.getAddressInfo().getAddressdetail());
            currentindex = new int[]{pointMallCase.getAddressInfo().getProvinceindex(),
                    pointMallCase.getAddressInfo().getCityindex(),
                    pointMallCase.getAddressInfo().getAreaindex(),};
        }
        mName.setText("" + shopBean.getTitle());
        mMyScoreText.setText(String.format(getResources().getString(R.string.current_score_pmsf), pointMallCase.getScore()));
        mCount.addTextChangedListener(mW);
        changect(ct, init);
    }

    public MAddressInfo getCurrentAddressInfo() {
        MAddressInfo addressInfo = new MAddressInfo();
        addressInfo.setRecivername(mRevicername.getText().toString());
        addressInfo.setRecivernumber(mNumber.getText().toString().trim());
        addressInfo.setAddressdetail(mDetail.getText().toString());
        addressInfo.setAddress(mArea.getText().toString());
        return addressInfo;

    }

    public boolean checkupload() {
        if (ct * shopBean.getIntegral() > pointMallCase.getScore()) {
//            ToastUtils.getInstance(getContext()).showToast("积分不足", Toast.LENGTH_LONG);
            AlerDialogUtils.createScoreNoPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), pointMallCase.getScore(), null);
            return false;
        }
        if (!CheckTextUtils.isMobileNo(mNumber.getText().toString().trim())) {
            ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.please_phone_right), Toast.LENGTH_LONG);
            return false;
        }
        if (mArea.getText().toString().length() == 0) {
            ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.no_address_pmsf), Toast.LENGTH_LONG);
            return false;
        }
        if (mDetail.getText().toString().length() == 0) {
            ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.no_address_detail_pmsf), Toast.LENGTH_LONG);
            return false;
        }
        if (mRevicername.getText().toString().length() == 0) {
            ToastUtils.getInstance(getContext()).showToast(getResources().getString(R.string.no_recivername_detail_pmsf), Toast.LENGTH_LONG);
            return false;
        }
        return true;
//        loadingView.loadingView();
//        pointMallCase.requestBuyShop(shopBean.getId(), ct, mDeliver.getText().toString().trim(), "reciver_default_v1"
//                , mNumber.getText().toString().trim()
//                , mArea.getText().toString().trim(),
//                mDetail.getText().toString().trim(), currentindex, (var1, var2) -> {
//                    loadingView.cancelloadingView();
//                    if (var2) {
//                        childFgmControll.toOrderResultMCF(var1, var2);
//                    } else {
//                        ToastUtils.getInstance(getContext()).showToast(var1, Toast.LENGTH_LONG);
//                    }
//                });
    }

    TextWatcher mW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int k = ct;
            try {
                k = Integer.valueOf(s.toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            changect(k, false);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void changect(int valueOf, boolean init) {
        if (shopBean == null) {
            return;
        }
        int k = ct;
        if (k != valueOf || init) {
            if (!init)
                ct = valueOf;
            String r = String.format(getResources().getString(R.string.buy_score_text_pmsf), ct, ct * shopBean.getIntegral());
            mCalText.setText(Html.fromHtml(r, Html.FROM_HTML_MODE_LEGACY));
            mCount.removeTextChangedListener(mW);
            mCount.setText(String.valueOf(ct));
            mCount.addTextChangedListener(mW);
        }

    }

    @Override
    public void refresh() {

    }

    public int[] getCurrentAreaIndex() {
        return currentindex;
    }
}
