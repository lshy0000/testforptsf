package com.wheatek.smartdevice.uiv2;

import android.annotation.NonNull;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;
import com.wheatek.smartdevice.myuriconnect.ShopBean;

public class MAdapter extends RecyclerView.Adapter<MAdapter.MHolder> {
    Context context;
    PointMallCase pointMallCase;
    View.OnClickListener mlistener;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        if (this.position != position) {
            this.position = position;
            notifyDataSetChanged();
        }
    }

    public MAdapter(Context context, PointMallCase pointMallCase, View.OnClickListener mlistener) {
        this.context = context;
        this.pointMallCase = pointMallCase;
        this.mlistener = mlistener;
    }

    @NonNull
    @Override
    public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shoplist_item2_pmsf, parent, false);
        return new MHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MHolder holder, int position) {
        ShopBean bean = pointMallCase.getMap().get(getPosition()+1).get(position);
        holder.mTodobtn.setTag(bean);
        holder.itemView.setTag(bean);
        holder.itemView.setOnClickListener(mlistener);
        holder.mTodobtn.setOnClickListener(mlistener);
        holder.mName.setText(bean.getTitle());
        Glide.with(context).load(bean.getImgUrlTump()).into(holder.mImage);
        holder.mPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //下划线
//            holder.mPrice.getPaint().setAntiAlias(true);//抗锯齿
        holder.mPrice.setText(String.format(context.getResources().getString(R.string.prefix_price_pmsf), bean.getPrice()));
        holder.mScore.setText(String.valueOf(bean.getIntegral()));
    }

    @Override
    public int getItemCount() {
        return pointMallCase.getMap().get(getPosition()+1).size();
    }


    public static class MHolder extends RecyclerView.ViewHolder {
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