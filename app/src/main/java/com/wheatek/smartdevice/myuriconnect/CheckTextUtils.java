package com.wheatek.smartdevice.myuriconnect;

import java.util.regex.Pattern;

public class CheckTextUtils {
    public static boolean isMobileNo(String mobileNo) {
        Pattern mobilePattern = Pattern.compile("^((13[0-9])|(14[5,8])|(15[0-9])|(16[6-7])|(17[0-9])|(18[0-9])|(198))\\d{8}$");
        return mobilePattern.matcher(mobileNo).matches();
    }

}
