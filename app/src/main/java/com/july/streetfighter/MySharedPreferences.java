package com.july.streetfighter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {

    static final String PREF_USER_ID = "user_id";
    static final String PREF_USER_AUTH = "user_auth";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * 유저 ID 저장
     **/
    public static void setUserId(Context ctx, String id) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_ID, id);
        editor.commit();
    }

    /**
     * 유저 권한 저장
     **/
    public static void setUserAuth(Context ctx, int auth) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_USER_AUTH, auth);
        editor.apply();
    }

    /**
     * 유저 ID 조회
     **/
    public static String getUserId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_ID, "-2");
    }

    /**
     * 유저 권한 조회
     **/
    public static int getUserAuth(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_USER_AUTH, -1);
    }

    /**
     * 로그아웃
     **/
    public static void clearUserInfo(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.apply();
    }

}
