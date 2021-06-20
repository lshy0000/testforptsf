package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;
import android.widget.Toast;

import java.lang.ref.SoftReference;

public class ToastUtils {
    private static Object lock = new Object();

    public static ToastUtils INSTANCE;

    public SoftReference toastp;
    private Context mContext;

    private ToastUtils(Context context) {
        if (context == null && mContext == null) {
            throw new NullPointerException("context不能为空");
        }
        if (context != null) {
            mContext = context.getApplicationContext();
        }
    }

    public static ToastUtils getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new ToastUtils(context);
                }
            }
        }
        return INSTANCE;
    }


    public void showToast(String msg, int time) {
        Toast toast = null;
        if (toastp != null && toastp.get() != null) {
            toast = ((Toast) toastp.get());
            toast.setText(msg);
            toast.setDuration(time);
        } else {
            toast = Toast.makeText(mContext, msg, time);
            toastp = new SoftReference(toast);
        }
        toast.show();
    }
}
