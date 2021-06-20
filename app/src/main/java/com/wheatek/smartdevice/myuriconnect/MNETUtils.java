package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Iterator;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MNETUtils {
    private static final String url = "https://ee7ddfec-e9f9-408f-ac4b-d7c16adb25df.bspapp.com/http/device";
    private static final String charsetName = "utf-8";

    public static void main(String[] args) {
        postPhoneInfotest(null, null);
    }

    private static final void postPhoneInfotest(final Context context, final Callb callb) {
        final JSONObject json = new JSONObject();
        try {
            json.put("action", "register");
            json.put("imei", SystemInfoUtils.getDeviceID(context));
            json.put("os", SystemInfoUtils.getSdkVersion());
            json.put("version", SystemInfoUtils.getAppVersion(context).versionName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean var4 = false;
        System.out.println(json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postPhoneInfo(url, context, json.toString(), callb);
                } catch (Exception e) {
                    e.printStackTrace();
                    JSONObject message = new JSONObject();
                    try {
                        message.put("error", e.toString());
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    if (callb != null) callb.response(message.toString(), false);
                }
            }
        }).start();
    }

    public static void postPhoneInfo(String url, Context context, String jsonstr, Callb callb) {
        if (url.startsWith("https")) {
            postPhoneInfohttps(url, context, jsonstr, callb);
            return;
        }
        doconnect(url, context, jsonstr, "POST", callb);
    }

    public static void postPhoneInfohttps(String url, Context context, String jsonstr, Callb callb) {
        HTTPSTrustManager.allowAllSSL();
//                conn.setSSLSocketFactory(getSSLContext(context)?.getSocketFactory());
        doconnect(url, context, jsonstr, "POST", callb);
    }

    public static void getPhoneInfo(String url, Context context, String jsonstr, Callb callb) {
        if (url.startsWith("https")) {
            getPhoneInfohttps(url, context, jsonstr, callb);
            return;
        }
        doconnect(url, context, jsonstr, "GET", callb);
    }

    public static void getPhoneInfohttps(String url, Context context, String jsonstr, Callb callb) {
        HTTPSTrustManager.allowAllSSL();
//                conn.setSSLSocketFactory(getSSLContext(context)?.getSocketFactory());
        doconnect(url, context, jsonstr, "GET", callb);

    }

    private static void doconnect(String url, Context context, String jsonstr, String postorget, Callb callb) {
        try {
            if ("GET".equalsIgnoreCase(postorget.trim())) {
                // 拼接geturl
                url = URLEncoder.encode(getGetURL(url, jsonstr), charsetName);
            }
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod(postorget);
            conn.setRequestProperty("Content-Type", "application/json;charset=" + charsetName);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            if (!"GET".equalsIgnoreCase(postorget.trim())) {
                //POST请求
                PrintWriter out =
                        new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), charsetName));
                out.print(jsonstr);
                out.flush();
            }
            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = "";
            String lines = reader.readLine();
            while (true) {
                lines = new String(lines.getBytes(), Charset.forName(charsetName));
                response += lines;
                String c = reader.readLine();
                if (c == null) break;
                lines = c;
            }
            reader.close();
            // 断开连接
            conn.disconnect();
            boolean isSuccess = false;
            try {
                isSuccess = isSuccess(response);
            } catch (Exception e) {
            }
            if (callb != null) {
                callb.response(response, isSuccess);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            JSONObject message = new JSONObject();
            try {
                message.put("error", e.toString());
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            if (callb != null) {
                callb.response(message.toString(), false);
            }
        }
    }

    private static String getGetURL(String url, String postorget) {
        String re = url;

        if (url.contains("?")) {
            re = pingjie(postorget);
        } else {
            re = pingjie(postorget);
            re = "?" + re;
        }
        return re + url;
    }

    private static String pingjie(String postorget) {
        String re = "";
        try {
            JSONObject jsonObject = new JSONObject(postorget);
            Iterator<String> a = jsonObject.keys();
            int i = 0;
            while (a.hasNext()) {
                if (i != 0)
                    re += "&";
                i++;
                String key = a.next();
                Object value = jsonObject.get(key);
                re += key + "=" + value;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return re;
    }

    private static boolean isSuccess(String response) {
        boolean re = false;
        String result = "";
        try {
            result = new JSONObject(response).getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (String successcode : ErrorCode.SUCCESSCODES) {
            if (successcode.equals(result)) {
                re = true;
                break;
            }
        }
        return re;
    }

    public static SSLContext getSSLContext(Context inputContext) {
        SSLContext context = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream ini = inputContext.getAssets().open("root.crt");
            Certificate ca = cf.generateCertificate(ini);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keystore);
            // Create an SSLContext that uses our TrustManager
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }


    public interface Callb {
        void response(String response, boolean success);
    }
}

