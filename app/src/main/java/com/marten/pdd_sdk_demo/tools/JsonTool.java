package com.marten.pdd_sdk_demo.tools;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonTool {

    //Json转对象
    public static <T> T jsonToObject(String str, Class<T> tClass) {
        try {
            if (str != null && !str.isEmpty()) {
                T data = JSONArray.parseObject(str, tClass);
                return data;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("jsonException", e.getMessage());
            return null;
        }
    }

    //Json转集合（List）
    public static <T> List<T> jsonToList(String str, Class<T> tList) {
        try {
            if (str != null && !str.isEmpty()) {
                List<T> list = JSONArray.parseArray(str, tList);
                return list;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("jsonException", e.getMessage());
            return null;
        }
    }

    public static <T> String objectToJson(T t) {
        return JSONObject.toJSONString(t);
    }

}
