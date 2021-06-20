package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;

public class ErrorCode {
    public static String ISNEEDPHONENUMBER = "1013";
    public static String SUCCESS = "0000";
    public static String[] SUCCESSCODES = new String[]{SUCCESS, ISNEEDPHONENUMBER};
    public static String NOENROLL = "9999";

    public static String getErrorCodeMessage(String code, String defaultMessage, Context context) {
        return defaultMessage;
    }

}
