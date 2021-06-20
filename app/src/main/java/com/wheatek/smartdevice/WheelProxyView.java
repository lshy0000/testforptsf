package com.wheatek.smartdevice;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.wheatek.smartdevice.myuriconnect.ChinaareaBean;
import com.wheatek.smartdevice.wheelview.adapter.WheelAdapter;
import com.wheatek.smartdevice.wheelview.listener.OnItemSelectedListener;
import com.wheatek.smartdevice.wheelview.view.WheelView;

import java.util.List;

public class WheelProxyView {
    private SimpleArrayWheelAdapter adapter1;
    private SimpleArrayWheelAdapter2 adapter2;
    private SimpleArrayWheelAdapter3 adapter3;
    AlerDialogUtils.MViewHolder_SLCTAREA viewHolder;
    public int[] currentindex ;

    public WheelProxyView(View convertView, View.OnClickListener surelistener, Dialog dialog, Context context,int[]currentindex) {
        this.currentindex=currentindex;
        viewHolder = new AlerDialogUtils.MViewHolder_SLCTAREA(convertView, surelistener, dialog);
        viewHolder.mSure.setTag(this);
        initWheelView(context);
    }

    public void initWheelView(Context context) {
        initWheel(viewHolder.mWheelview1, context, 12, 9);
        initWheel(viewHolder.mWheelview2, context, 12, 9);
        initWheel(viewHolder.mWheelview3, context, 12, 9);
        adapter1 = new SimpleArrayWheelAdapter();
        viewHolder.mWheelview1.setAdapter(adapter1);
        viewHolder.mWheelview1.setCurrentItem(currentindex[0]);
        adapter2 = new SimpleArrayWheelAdapter2(currentindex[0]);
        viewHolder.mWheelview2.setAdapter(adapter2);
        viewHolder.mWheelview2.setCurrentItem(currentindex[1]);
        adapter3 = new SimpleArrayWheelAdapter3(currentindex[0], currentindex[1]);
        viewHolder.mWheelview3.setAdapter(adapter3);
        viewHolder.mWheelview3.setCurrentItem(currentindex[2]);
        viewHolder.mWheelview1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (currentindex[0] != position) {
                    currentindex[0] = position;
                    currentindex[1] = 0;
                    currentindex[2] = 0;
                    refreshView(false, true, true);
                }
            }
        });
        viewHolder.mWheelview2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                if (currentindex[1] != position) {
                    currentindex[1] = position;
                    currentindex[2] = 0;
                    refreshView(false, false, true);
                }
            }
        });
        viewHolder.mWheelview3.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                currentindex[2] = position;
            }
        });
    }

    public void refreshView(boolean b1, boolean b2, boolean b3) {
        adapter2.setIndex(currentindex[0]);
        adapter3.setIndex(currentindex[0], currentindex[1]);
        if (b1) {
            viewHolder.mWheelview1.setCurrentItem(currentindex[0]);
            viewHolder.mWheelview1.invalidate();
        }
        if (b2) {
            viewHolder.mWheelview2.setCurrentItem(currentindex[1]);
            viewHolder.mWheelview2.invalidate();
        }
        if (b3) {
            viewHolder.mWheelview3.setCurrentItem(currentindex[2]);
            viewHolder.mWheelview3.invalidate();
        }
    }

    public static void initWheel(final WheelView myWheelView, Context context, int size, int showcount) {
//        myWheelView.setDividerColor(context.getColor(R.color.transparent));
//        myWheelView.setTextColorCenter(context.getColor(R.color.colorPrimary_wyh_traf));
//        myWheelView.setTextColorOut(context.getColor(R.color.static_newui_tab));
        myWheelView.setTextSize(size);
        myWheelView.setAlphaGradient(false);
        myWheelView.setCyclic(false);
        myWheelView.setItemsVisibleCount(showcount);
        myWheelView.setAlphaGradient(true);
        myWheelView.setTextXOffset(0);
//        myWheelView.setCurrentItem((Integer) data);
        myWheelView.setDividerWidth(1);
        myWheelView.setDividerType(WheelView.DividerType.WRAP);

    }

    public class SimpleArrayWheelAdapter implements WheelAdapter {
        List<ChinaareaBean> mlist;

        SimpleArrayWheelAdapter() {
            mlist = ChinaareaBean.CHINEAAREALIST;
        }

        @Override
        public int getItemsCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int index) {
            return mlist.get(index).getName();
        }

        @Override
        public int indexOf(Object o) {
            int i = 0;
            for (Object o1 : mlist) {
                if (o1.equals(o)) {
                    return i;
                }
                i++;
            }
            return 0;
        }
    }

    public class SimpleArrayWheelAdapter2 implements WheelAdapter {
        List<ChinaareaBean.City> mlist;
        public int index;

        public void setIndex(int index) {
            this.index = index;
            mlist = ChinaareaBean.CHINEAAREALIST.get(index).getCity();
        }

        SimpleArrayWheelAdapter2(int index) {
            setIndex(index);
        }

        @Override
        public int getItemsCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int index) {
            return mlist.get(index).getName();
        }

        @Override
        public int indexOf(Object o) {
            int i = 0;
            for (Object o1 : mlist) {
                if (o1.equals(o)) {
                    return i;
                }
                i++;
            }
            return 0;
        }
    }

    public class SimpleArrayWheelAdapter3 implements WheelAdapter {
        List<String> mlist;
        int index1, index2;

        public void setIndex(int index1, int index2) {
            this.index1 = index1;
            mlist = ChinaareaBean.CHINEAAREALIST.get(index1).getCity().get(index2).getArea();
        }

        SimpleArrayWheelAdapter3(int index1, int index2) {
            setIndex(index1, index2);
        }

        @Override
        public int getItemsCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int index) {
            return mlist.get(index);
        }

        @Override
        public int indexOf(Object o) {
            int i = 0;
            for (Object o1 : mlist) {
                if (o1.equals(o)) {
                    return i;
                }
                i++;
            }
            return 0;
        }
    }
}