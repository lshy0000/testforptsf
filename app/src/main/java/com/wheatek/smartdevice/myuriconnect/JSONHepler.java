package com.wheatek.smartdevice.myuriconnect;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析类辅助
 */

//{
//        "code": "0000",
//        "msg": "成功",
//        "result": {
//        "integral": 0,
//        "shopLink": "https://shop95338401.m.youzan.com/wscshop/showcase/homepage?kdt_id=95146233&ps=320",
//        "link": "https://j.youzan.com/7yPoD2",
//        "mobile": "15000654072"
//        }
//        }

public class JSONHepler {

    public static String getShoppingfromResp(String jsonstr) {
        String re = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            jsonObject = jsonObject.getJSONObject("result");
            re = jsonObject.getString("shopLink");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getQRfromResp(PointMallCase pointMallCase, String jsonstr) {
        String re = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            jsonObject = jsonObject.getJSONObject("result");
            re = jsonObject.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pointMallCase.getHost() + pointMallCase.getQrpath() + re;
    }

    public static int getScorefromResp(String jsonstr) {
        int re = 0;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            jsonObject = jsonObject.getJSONObject("result");
            re = jsonObject.getInt("integral");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getKeyfromResp(String jsonstr,String key) {
        String re = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            jsonObject = jsonObject.getJSONObject("result");
            re = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String getNumberfromResp(String jsonstr) {
        String re = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            jsonObject = jsonObject.getJSONObject("result");
            re = jsonObject.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static boolean isNeedPhoneNumber(String jsonstr) {
        boolean re = false;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            re = ErrorCode.ISNEEDPHONENUMBER.equals(jsonObject.getString("code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static ShopBean getShopBeanFromJson(Gson gson, String jsonstr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            String result = jsonObject.get("result").toString();
            if (result.startsWith("{")) {
                if (gson == null) {
                    gson = new Gson();
                }
                return gson.fromJson(result, ShopBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<ShopBean> getShopBeansFromJson(Gson gson, String jsonstr) {
        List<ShopBean> re = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            String result = jsonObject.getJSONObject("result").get("list").toString();
            if (result.startsWith("[")) {
                if (gson == null) {
                    gson = new Gson();
                }
                re.addAll(gson.fromJson(result, new TypeToken<ArrayList<ShopBean>>() {
                }.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static List<OrderHistoryBean> getOrderHistoryFromJson(Gson gson, String jsonstr) {
        List<OrderHistoryBean> re = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            String result = jsonObject.get("result").toString();
            if (result.startsWith("[")) {
                if (gson == null) {
                    gson = new Gson();
                }
                re.addAll(gson.fromJson(result, new TypeToken<ArrayList<OrderHistoryBean>>() {
                }.getType()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }


    public static int getTotalPageFromJson(Gson gson, String jsonstr) {
        int re = 1;
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            re = jsonObject.getJSONObject("result").getInt("totalPage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }


}
