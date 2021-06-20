package com.wheatek.smartdevice;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wheatek.smartdevice.myuriconnect.OrderHistoryBean;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderHistoryMCF extends MChildFragment {
    private TextView mHinttext;
    private RecyclerView mRecycleview;

    private MAdapter mAdapter;
    private View.OnClickListener mlistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
        }
    };

    public OrderHistoryMCF(PointMallCase pointMallCase, ChildFgmControll childFgmControll, LoadingView loadingView) {
        super(pointMallCase, childFgmControll, loadingView);
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orderhistory_fragment, container, false);
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
            pointMallCase.requestOrderList((var1, var2) -> {
                bindDataView(var1, var2);
            }, 1, 10000);
        }
    }

    public OrderHistoryMCF() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindDataView(String re, boolean success) {
        if (success) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initView(View view) {
        mHinttext = view.findViewById(R.id.hinttext);
        mRecycleview = view.findViewById(R.id.recycleview);
    }

    private class MAdapter extends RecyclerView.Adapter<MHolder> {
        Context context;
        SimpleDateFormat simpleDateFormat;

        public MAdapter(Context context) {
            this.context = context;
            simpleDateFormat = new SimpleDateFormat(context.getResources().getString(R.string.data_fromate_text_pmsf));
        }

        @NonNull
        @Override
        public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.orderhistory_item_pmsf, parent, false);
            return new MHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MHolder holder, int position) {
            OrderHistoryBean bean = pointMallCase.getOrderHistoryBeanList().get(position);
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(mlistener);
            holder.mCount.setText(String.format(getResources().getString(R.string.count_fromate_text_pmsf), bean.getNum()));
            holder.mName.setText(getStateString(holder.mName.getContext(), bean.getStatus()));
            holder.mScore.setText(String.format(context.getString(R.string.score_allfromat_text_pmsf), bean.getIntegral()));
            holder.mName.setText(bean.getTitle());
            Glide.with(context).load(bean.getImgUrlTump()).into(holder.mImage);
            holder.mTime.setText(simpleDateFormat.format(new Date(bean.getCreateDate())));
        }

        @Override
        public int getItemCount() {
            return pointMallCase.getOrderHistoryBeanList().size();
        }

    }


    public String getStateString(Context context, int state) {
        return context.getResources().getStringArray(R.array.statearray)[state];
    }

    private static class MHolder extends RecyclerView.ViewHolder {
        private ImageView mImage;
        private TextView mTime;
        private TextView mName;
        private TextView mCount;
        private TextView mScore;
        private TextView mState;

        public MHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image);
            mTime = itemView.findViewById(R.id.time);
            mName = itemView.findViewById(R.id.name);
            mCount = itemView.findViewById(R.id.count);
            mScore = itemView.findViewById(R.id.score);
            mState = itemView.findViewById(R.id.state);
        }
    }
}
