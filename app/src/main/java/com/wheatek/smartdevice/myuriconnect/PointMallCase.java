package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 核心业务交互类。
 */
public class PointMallCase {
    private Handler handler;
    private String host = "http://srv.wheatek.cn";
    public static final String QRPATH = "/Doctek-app/api/mobile/payment/watchQRcode.png?data=";
    public static final String SHAREPATH = "/Doctek-app/api/mobile/devicepay/findShareUrl";
    public static final String PHONEPATH = "/Doctek-app/api/mobile/devicepay/updateDeviceMobile";
    public static final String BUYPATH = "/Doctek-app/api/mobile/exchange/addFlow";
    public static final String SHOPLISTPATH = "/Doctek-app/api/mobile/exchange/list";
    public static final String ORDERLISTPATH = "/Doctek-app/api/mobile/exchange/listFlow";
    public static final String SHOPDETAIL = "/Doctek-app/api/mobile/exchange/exchangeDetail";
    public static final String SHOPDRECOMMENED = "/Doctek-app/api/mobile/exchange/listRandom";


    private String deviceId;
    private String phone;
    private MAddressInfo addressInfo;
    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean isNeedPhoneNumber = true;
    private List<ShopBean> shopBeanList = new ArrayList<>();
    private ShopBean shopBeanDetail;

    public ShopBean getShopBeanDetail() {
        return shopBeanDetail;
    }

    public void setShopBeanDetail(ShopBean shopBeanDetail) {
        this.shopBeanDetail = shopBeanDetail;
    }

    // 分页设计
    private Map<Integer, List<ShopBean>> map = new HashMap<>();

    private ShopBean recommendShopBean;
    private int totalpage = 2; // 最小为2

    public ShopBean getRecommendShopBean() {
        return recommendShopBean;
    }

    public void setRecommendShopBean(ShopBean recommendShopBean) {
        this.recommendShopBean = recommendShopBean;
    }

    private List<OrderHistoryBean> orderHistoryBeanList = new ArrayList<>();
    private Gson gson = new Gson();
    private int score;
    private String favorable1;
    private String favorable2;
    private String favorable3;

    public String getFavorable1() {
        return favorable1;
    }

    public void setFavorable1(String favorable1) {
        this.favorable1 = favorable1;
    }

    public String getFavorable2() {
        return favorable2;
    }

    public void setFavorable2(String favorable2) {
        this.favorable2 = favorable2;
    }

    public String getFavorable3() {
        return favorable3;
    }

    public void setFavorable3(String favorable3) {
        this.favorable3 = favorable3;
    }

    public boolean isNeedPhoneNumber() {
        return isNeedPhoneNumber;
    }

    public void setNeedPhoneNumber(boolean needPhoneNumber) {
        isNeedPhoneNumber = needPhoneNumber;
    }

    public void setShopBeanList(List<ShopBean> shopBeanList) {
        this.shopBeanList = shopBeanList;
    }

    public void setOrderHistoryBeanList(List<OrderHistoryBean> orderHistoryBeanList) {
        this.orderHistoryBeanList = orderHistoryBeanList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ShopBean> getShopBeanList() {
        return shopBeanList;
    }

    public List<OrderHistoryBean> getOrderHistoryBeanList() {
        return orderHistoryBeanList;
    }

    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    private static Object lock = new Object();

    public Map<Integer, List<ShopBean>> getMap() {
        return map;
    }

    public void setMap(Map<Integer, List<ShopBean>> map) {
        this.map = map;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getQrpath() {
        return QRPATH;
    }

    public String getPath() {
        return SHAREPATH;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public MAddressInfo getAddressInfo() {
        return addressInfo;
    }

    public static PointMallCase INSTANCE;

    public static PointMallCase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new PointMallCase(context);
                }
            }
        }
        return INSTANCE;
    }

    private PointMallCase(Context context) {
        this.context = context.getApplicationContext();
        deviceId = SystemInfoUtils.getSerial(context);
        sharedPreferences = context.getSharedPreferences("point_mail_call", Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "");
        int index = sharedPreferences.getInt("address_index", -1);
        if (index != -1)
            addressInfo = MAddressInfo.getinfoFromSP(context, index);
        handler = new Handler(Looper.getMainLooper());
    }

    public void saveAddress(MAddressInfo info) {
        if (info != null) {
            addressInfo = info;
            sharedPreferences.edit().putInt("address_index", 0).commit();
            MAddressInfo.setinfoToSP(context, 0, info);
        }
    }

    private void setPointMallInfoFCache(String var1) {
        sharedPreferences.edit().putString("PointMallInfoFCache", var1).commit();
    }

    private String getPointMallInfoFCache() {
        return sharedPreferences.getString("PointMallInfoFCache", null);
    }

    private void setRecommendForCache(String var1) {
        sharedPreferences.edit().putString("RecommendForCache", var1).commit();
    }

    private String getRecommendForCache() {
        return sharedPreferences.getString("RecommendForCache", null);
    }

    public void savePhone(String phone) {
        if (phone != null) {
            this.phone = phone;
            sharedPreferences.edit().putString("phone", phone).commit();
        }
    }

    public void releaseData() {
        shopBeanList.clear();
        map.clear();
        orderHistoryBeanList.clear();
    }

    private JSONObject getJSONObjectOrigin() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sn", deviceId);
            jsonObject.put("mobile", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void requestOrderList(MNETUtils.Callb callb, int currPage, int pageSize) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {
                jsonObject.put("currPage", currPage);
                jsonObject.put("pageSize", pageSize);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + ORDERLISTPATH, context, jsonObject.toString(), (v1, v2) -> {
                if (v2) {
                    this.orderHistoryBeanList.clear();
                    this.orderHistoryBeanList.addAll(JSONHepler.getOrderHistoryFromJson(gson, v1));
                }
                handler.post(() -> {
                    if (callb != null) {
                        callb.response(v1, v2);
                    }
                });
            });
        });
    }

    public void requestShopMap(MNETUtils.Callb callb, int currPage, int pageSize) {
        if (map.containsKey(currPage)) {
            if (callb != null) {
                callb.response(null, true);
            }
        } else {
            executor.execute(() -> {
                JSONObject jsonObject = getJSONObjectOrigin();
                try {
                    jsonObject.put("currPage", currPage);
                    jsonObject.put("pageSize", pageSize);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MNETUtils.postPhoneInfo(host + SHOPLISTPATH, context, jsonObject.toString(), (v1, v2) -> {
                    if (v2) {
                        totalpage = JSONHepler.getTotalPageFromJson(gson, v1);
                        if (map.containsKey(currPage) && map.get(currPage) != null) {
                            map.get(currPage).clear();
                            map.get(currPage).addAll(JSONHepler.getShopBeansFromJson(gson, v1));
                        } else {
                            map.put(currPage, JSONHepler.getShopBeansFromJson(gson, v1));
                        }
                    }
                    handler.post(() -> {
                        if (callb != null) {
                            callb.response(v1, v2);
                        }
                    });
                });
            });
        }
    }


    public void requestShopList(MNETUtils.Callb callb, int currPage, int pageSize) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {
                jsonObject.put("curPage", currPage);
                jsonObject.put("pageSize", pageSize);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + SHOPLISTPATH, context, jsonObject.toString(), (v1, v2) -> {
                if (v2) {
                    this.shopBeanList.clear();
                    this.shopBeanList.addAll(JSONHepler.getShopBeansFromJson(gson, v1));
                }
                handler.post(() -> {
                    if (callb != null) {
                        callb.response(v1, v2);
                    }
                });
            });
        });
    }

    public void requestBuyShop(int shopIndex, int shopcount, String delivery, String recivername, String number, String address, String addressdetail, int[] addressindex, MNETUtils.Callb callb) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {/*{
                "address": "xx寺，xx庙",
                        "exchangeId": 1,
                        "mobile": "110",
                        "num": 1,
                        "pca": "上海-上海-浦东",
                        "sendType": 1,
                        "sn": "76666666666"
            }*/
                jsonObject.put(" receiverName", recivername);
                jsonObject.put("address", addressdetail);
                jsonObject.put("exchangeId", shopIndex);
                jsonObject.put("num", shopcount);
                jsonObject.put("pca", address);
                jsonObject.put("mobile", number);
                jsonObject.put("sendType", 1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + BUYPATH, context, jsonObject.toString(), (var1, var2) -> {
                if (var2) {
                    MAddressInfo info = new MAddressInfo();
                    info.setCretetime(System.currentTimeMillis());
                    info.setAddress(address);
                    info.setAddressdetail(addressdetail);
                    info.setProvinceindex(addressindex[0]);
                    info.setCityindex(addressindex[1]);
                    info.setAreaindex(addressindex[2]);
                    info.setRecivernumber(number);
                    info.setRecivername("default");
                    saveAddress(info);
                }
                if (var2) {
                    requestGetPointMallInfo(callb);
                } else {
                    handler.post(() -> {
                        if (callb != null) {
                            callb.response(var1, var2);
                        }
                    });
                }
            });
        });
    }

    public void requestGetPointMallInfoFCache(MNETUtils.Callb callb) {
        if (getPointMallInfoFCache() != null && getPointMallInfoFCache().length() > 0) {
            dealMallInfo(getPointMallInfoFCache(), true, callb);
            requestGetPointMallInfo(callb);
        } else {
            requestGetPointMallInfo(callb);
        }
    }
    public void requestGetRecommmendInfoForCache(MNETUtils.Callb callb) {
        if (getRecommendForCache() != null && getRecommendForCache().length() > 0) {
            dealRecommend(getRecommendForCache(), true, callb);
            requestGetRecommmendInfo(callb);
        } else {
            requestGetRecommmendInfo(callb);
        }
    }
    public void requestGetPointMallInfo(MNETUtils.Callb callb) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            MNETUtils.postPhoneInfo(host + SHAREPATH, context, jsonObject.toString(), ((var1, var2) -> {
                dealMallInfo(var1, var2, callb);
                if (var2) {
                    setPointMallInfoFCache(var1);
                }
            }));
        });
    }

    private void dealMallInfo(String var1, boolean var2, MNETUtils.Callb callb) {
        if (var2) {
            isNeedPhoneNumber = JSONHepler.isNeedPhoneNumber(var1);
            String phoneNumber = JSONHepler.getNumberfromResp(var1);
            if (phoneNumber != null && phoneNumber.length() > 0) {
                savePhone(phoneNumber);
            }
            score = JSONHepler.getScorefromResp(var1);
            favorable1 = JSONHepler.getKeyfromResp(var1, "favorable1");
            favorable2 = JSONHepler.getKeyfromResp(var1, "favorable2");
            favorable3 = JSONHepler.getKeyfromResp(var1, "favorable3");
        }
        handler.post(() -> {
            if (callb != null) {
                callb.response(var1, var2);
            }
        });
    }

    private void dealRecommend(String v1, boolean v2, MNETUtils.Callb callb) {
        boolean rebool = v2;
        if (v2) {
            List<ShopBean> re = JSONHepler.getShopBeansFromJson(gson, v1);
            if (re != null && re.size() > 0) {
                setRecommendShopBean(re.get(0));
                rebool = true;
            } else {
                rebool = false;
            }
        }
        boolean finalRebool = rebool;
        handler.post(() -> {
            if (callb != null) {
                callb.response(v1, finalRebool);
            }
        });
    }

    public void requestGetRecommmendInfo(MNETUtils.Callb callb) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {
                jsonObject.put("curPage", 1);
                jsonObject.put("pageSize", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + SHOPDRECOMMENED, context, jsonObject.toString(), (v1, v2) -> {

                if (v2) {
                    setRecommendForCache(v1);
                }
                dealRecommend(v1, v2, callb);

            });
        });
    }

    public void requestUploadMobile(String mobile, MNETUtils.Callb callb) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {
                jsonObject.put("sn", deviceId);
                jsonObject.put("mobile", mobile);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + PHONEPATH, context, jsonObject.toString(), (var1, var2) -> {
                if (var2) {
                    savePhone(mobile);
                    requestGetPointMallInfo(callb);
                } else {
                    handler.post(() -> {
                        if (callb != null) {
                            callb.response(var1, var2);
                        }
                    });
                }
            });
        });
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void requestShopDetail(int id, MNETUtils.Callb callb) {
        executor.execute(() -> {
            JSONObject jsonObject = getJSONObjectOrigin();
            try {
                jsonObject.put("exchangeId", id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MNETUtils.postPhoneInfo(host + SHOPDETAIL, context, jsonObject.toString(), (var1, var2) -> {
                if (var2) {
                    shopBeanDetail = JSONHepler.getShopBeanFromJson(gson, var1);
                }
                handler.post(() -> {
                    if (callb != null) {
                        callb.response(var1, var2);
                    }
                });
            });
        });
    }
}
