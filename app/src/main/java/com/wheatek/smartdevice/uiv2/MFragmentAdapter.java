package com.wheatek.smartdevice.uiv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wheatek.smartdevice.R;
import com.wheatek.smartdevice.myuriconnect.PointMallCase;

public class MFragmentAdapter extends RecyclerView.Adapter<MFragmentAdapter.RecyViewHolder> {

    int total;
    Context context;
    PointMallCase pointMallCase;
    int pageSize;
    View.OnClickListener mlistener;
    LoadingDataI loadingDataI;

    public MFragmentAdapter(int total, Context context, PointMallCase pointMallCase, int pageSize, View.OnClickListener mlistener, LoadingDataI loadingDataI) {
        this.total = total;
        this.context = context;
        this.pointMallCase = pointMallCase;
        this.pageSize = pageSize;
        this.mlistener = mlistener;
        this.loadingDataI = loadingDataI;
    }

    @NonNull
    @Override
    public RecyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyleview, parent, false);
        return new RecyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyViewHolder holder, int position) {
        if (pointMallCase.getMap().containsKey(position+1) && pointMallCase.getMap().get(position+1) != null) {
            refreshpos(holder, position, false);
        } else {
            loadingDataI.loadPage(position, pageSize, (v1, v2) -> {
                if (v2)
                    refreshpos(holder, position, true);
            });
        }
    }

    private void refreshpos(RecyViewHolder holder, int position, boolean init) {
        if (init || holder.mRecycleview.getAdapter() == null) {
            holder.mRecycleview.setLayoutManager(new GridLayoutManager(context, 3));
            MAdapter adapter = new MAdapter(context, pointMallCase, mlistener);
            adapter.setPosition(position);
            holder.mRecycleview.setAdapter(adapter);
        } else {
            MAdapter adapter = ((MAdapter) holder.mRecycleview.getAdapter());
            adapter.setPosition(position);
        }
    }

    @Override
    public int getItemCount() {
        return total;
    }

    public static class RecyViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView mRecycleview;

        public RecyViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecycleview = (RecyclerView) itemView.findViewById(R.id.recycleview);
        }
    }


}
