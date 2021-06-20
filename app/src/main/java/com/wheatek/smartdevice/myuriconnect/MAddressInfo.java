package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;
import android.content.SharedPreferences;

public class MAddressInfo {
    String recivername;
    String recivernumber;
    String address;
    int provinceindex;
    int cityindex;
    int areaindex;
    String addressdetail;
    long cretetime;

    public static MAddressInfo getinfoFromSP(Context context, int index) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("point_mail_call_address_" + index, Context.MODE_PRIVATE);
        MAddressInfo re = new MAddressInfo();
        re.setRecivername(sharedPreferences.getString("recivername", ""));
        re.setRecivernumber(sharedPreferences.getString("recivernumber", ""));
        re.setAddress(sharedPreferences.getString("address", ""));
        re.setProvinceindex(sharedPreferences.getInt("provinceindex", -1));
        re.setCityindex(sharedPreferences.getInt("cityindex", -1));
        re.setAreaindex(sharedPreferences.getInt("areaindex", -1));
        re.setAddressdetail(sharedPreferences.getString("addressdetail", ""));
        re.setCretetime(sharedPreferences.getLong("cretetime", -1));
        return re;
    }

    public static void setinfoToSP(Context context, int index, MAddressInfo info) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("point_mail_call_address_" + index, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("recivername", info.getRecivername());
        edit.putString("recivernumber", info.getRecivernumber());
        edit.putString("address", info.getAddress());
        edit.putInt("provinceindex", info.getProvinceindex());
        edit.putInt("cityindex", info.getCityindex());
        edit.putInt("areaindex", info.getAreaindex());
        edit.putString("addressdetail", info.getAddressdetail());
        edit.putLong("cretetime", info.getCretetime());
        edit.commit();
    }

    public String getRecivername() {
        return recivername;
    }

    public void setRecivername(String recivername) {
        this.recivername = recivername;
    }

    public String getRecivernumber() {
        return recivernumber;
    }

    public void setRecivernumber(String recivernumber) {
        this.recivernumber = recivernumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityindex() {
        return cityindex;
    }

    public void setCityindex(int cityindex) {
        this.cityindex = cityindex;
    }

    public int getProvinceindex() {
        return provinceindex;
    }

    public void setProvinceindex(int provinceindex) {
        this.provinceindex = provinceindex;
    }

    public int getAreaindex() {
        return areaindex;
    }

    public void setAreaindex(int areaindex) {
        this.areaindex = areaindex;
    }

    public String getAddressdetail() {
        return addressdetail;
    }

    public void setAddressdetail(String addressdetail) {
        this.addressdetail = addressdetail;
    }

    public long getCretetime() {
        return cretetime;
    }

    public void setCretetime(long cretetime) {
        this.cretetime = cretetime;
    }

    public static String O = "-";

    public static String getAddressfromindex(Context context,int[] indexes) {
        ChinaareaBean bean = ChinaareaBean.CHINEAAREALIST.get(indexes[0]);
        ChinaareaBean.City city = bean.getCity().get(indexes[1]);
        return bean.getName() + O + city.getName() + O + city.getArea().get(indexes[2]);

    }


}
