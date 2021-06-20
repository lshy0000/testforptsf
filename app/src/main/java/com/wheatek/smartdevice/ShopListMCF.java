package com.wheatek.smartdevice;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ShopBean;

public class ShopListMCF extends MChildFragment {
    private TextView mHinttext;
    private RecyclerView mRecycleview;
    private MAdapter mAdapter;
    private View.OnClickListener mlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!pointMallCase.isNeedPhoneNumber()) {
                int position = (int) v.getTag();
                childFgmControll.toOrderMCF(position);
            } else {
                AlerDialogUtils.createSureNoPhoneDialog(((ViewGroup) getActivity().getWindow().getDecorView()), getContext(), null);
            }
        }
    };

    public ShopListMCF(PointMallCase pointMallCase, ChildFgmControll childFgmControll, LoadingView loadingView) {
        super(pointMallCase, childFgmControll, loadingView);
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shoplist_fragment, container, false);
        initView(view);
        mRecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MAdapter(getContext());
        mRecycleview.setAdapter(mAdapter);
        if (pointMallCase == null) {
            pointMallCase=PointMallCase.getInstance(getContext());
        }
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            pointMallCase.requestShopList((var1, var2) -> {
                bindDataView(var1, var2);
            }, 1, 10000);
        }
    }

    public ShopListMCF() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindDataView(String re, boolean success) {
        if (pointMallCase.getShopBeanList().size() != 0) {
            mHinttext.setText(getResources().getString(R.string.shoplist_hint_text_pmsf));
        }
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
        mHinttext = view.findViewById(R.id.hinttext);
        mRecycleview = view.findViewById(R.id.recycleview);
    }

    private class MAdapter extends RecyclerView.Adapter<MHolder> {
        Context context;

        public MAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.shoplist_item_pmsf, parent, false);
            return new MHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MHolder holder, int position) {
            ShopBean bean = pointMallCase.getShopBeanList().get(position);
            holder.mTodobtn.setTag(position);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mlistener);
            holder.mTodobtn.setOnClickListener(mlistener);
            holder.mName.setText(bean.getTitle());
            Glide.with(context).load(bean.getImgUrlTump()).into(holder.mImage);

            holder.mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //下划线
//            holder.mPrice.getPaint().setAntiAlias(true);//抗锯齿
            holder.mPrice.setText(String.format(getResources().getString(R.string.prefix_price_pmsf), bean.getPrice()));
            holder.mScore.setText(String.valueOf(bean.getIntegral()));
        }

        @Override
        public int getItemCount() {
            return pointMallCase.getShopBeanList().size();
        }

    }

    private static class MHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mName;
        private TextView mPrice;
        private TextView mScore;
        private Button mTodobtn;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mName = itemView.findViewById(R.id.name);
            mPrice = itemView.findViewById(R.id.price);
            mScore = itemView.findViewById(R.id.score);
            mTodobtn = itemView.findViewById(R.id.todobtn);
        }
    }
}
