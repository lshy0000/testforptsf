package com.wheatek.smartdevice;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wheatek.smartdevice.wheelview.view.WheelView;

public class AlerDialogUtils {


    public static Dialog createSurePhoneDialog(ViewGroup decorView, Context context, String phone, View.OnClickListener surelistener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pmsf, decorView, false);
        MViewHolder viewHolder = new MViewHolder(view);
        viewHolder.mTitle.setText(phone);
        final Dialog dialog = new Dialog(context);
        viewHolder.mCancel.setOnClickListener((v) -> {
            if (dialog != null) dialog.dismiss();
        });
        viewHolder.mSure.setOnClickListener((v) -> {
            if (dialog != null) {
                if (surelistener != null) {
                    surelistener.onClick(v);
                }
                dialog.dismiss();
            }
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

    public static class MViewHolder_SLCTAREA {
        public TextView mCancel;
        public TextView mSure;
        public WheelView mWheelview1;
        public WheelView mWheelview2;
        public WheelView mWheelview3;

        public MViewHolder_SLCTAREA(View convertView, View.OnClickListener surelistener, Dialog dialog) {
            mCancel = convertView.findViewById(R.id.cancel);
            mSure = convertView.findViewById(R.id.sure);
            mWheelview1 = convertView.findViewById(R.id.wheelview1);
            mWheelview2 = convertView.findViewById(R.id.wheelview2);
            mWheelview3 = convertView.findViewById(R.id.wheelview3);
            mCancel.setOnClickListener(v -> dialog.dismiss());
            mSure.setOnClickListener(v -> {
                dialog.dismiss();
                if (surelistener != null) surelistener.onClick(v);
            });
        }
    }

    public static class MViewHolder {
        public TextView mTitle;
        public TextView mHinttext;
        public TextView mHinttext2;
        public TextView mCancel;
        public View mLine;
        public TextView mSure;

        public MViewHolder(View convertView) {
            mTitle = convertView.findViewById(R.id.title);
            mHinttext = convertView.findViewById(R.id.hinttext);
            mHinttext2 = convertView.findViewById(R.id.hinttext2);
            mCancel = convertView.findViewById(R.id.cancel);
            mLine = convertView.findViewById(R.id.line);
            mSure = convertView.findViewById(R.id.sure);
        }
    }


    public static Dialog createSureNoPhoneDialog(ViewGroup decorView, Context context, View.OnClickListener surelistener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog3_pmsf, decorView, false);
        MViewHolder viewHolder = new MViewHolder(view);
        final Dialog dialog = new Dialog(context);
        viewHolder.mSure.setOnClickListener((v) -> {
            if (dialog != null) {
                if (surelistener != null) {
                    surelistener.onClick(v);
                }
                dialog.dismiss();
            }
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


    public static Dialog createScoreNoPhoneDialog(ViewGroup decorView, Context context, int soce, View.OnClickListener surelistener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog2_pmsf, decorView, false);
        MViewHolder viewHolder = new MViewHolder(view);
        viewHolder.mHinttext2.setText(String.format(context.getResources().getString(R.string.current_score_tex_pmsf), soce));
        final Dialog dialog = new Dialog(context);
        viewHolder.mSure.setOnClickListener((v) -> {
            if (dialog != null) {
                if (surelistener != null) {
                    surelistener.onClick(v);
                }
                dialog.dismiss();
            }
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

    public static Dialog createSelectAreaPhoneDialog(ViewGroup decorView, Context context, int[] currentindex, View.OnClickListener surelistener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_area_select_pmsf, decorView, false);
        final Dialog dialog = new Dialog(context);
        WheelProxyView viewHolder = new WheelProxyView(view, surelistener, dialog,context,currentindex);
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
}
