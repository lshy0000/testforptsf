/**
 * Copyright 2021 bejson.com
 */
package com.wheatek.smartdevice.myuriconnect;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.wheatek.smartdevice.myuriconnect.ChinaArea.CHINAAREA;

/**
 * Auto-generated: 2021-06-11 18:10:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChinaareaBean {

    public static List<ChinaareaBean> CHINEAAREALIST = new Gson().fromJson(CHINAAREA.toString(), new TypeToken<ArrayList<ChinaareaBean>>() {}.getType());

    private String name;
    private List<City> city;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public List<City> getCity() {
        return city;
    }

    /**
     * Auto-generated: 2021-06-11 18:10:48
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class City {

        private String name;
        private List<String> area;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setArea(List<String> area) {
            this.area = area;
        }

        public List<String> getArea() {
            return area;
        }
    }

}