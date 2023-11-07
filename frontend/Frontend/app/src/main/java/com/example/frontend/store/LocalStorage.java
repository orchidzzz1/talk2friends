package com.example.frontend.store;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    private static final String PREF_NAME = "Preferences";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_AUTHENTICATED = "isAuthenticated";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_INTERESTS = "interests";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(Context context, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(KEY_TOKEN, "");
    }

    public static void saveAuthenticated(Context context, boolean isAuthenticated) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(KEY_AUTHENTICATED, isAuthenticated);
        editor.apply();
    }

    public static boolean getAuthenticated(Context context) {
        return getSharedPreferences(context).getBoolean(KEY_AUTHENTICATED, false);
    }

    public static void clearUserData(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_AUTHENTICATED);
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
    public static void saveUserName(Context context, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_USERNAME, userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return getSharedPreferences(context).getString(KEY_USERNAME, null);
    }
    public static void saveInterests(Context context, String[] interests) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_INTERESTS, String.join(",",interests));
        editor.apply();
    }

    public static String[] getInterests(Context context) {
        try{
            return (getSharedPreferences(context).getString(KEY_INTERESTS, "")).split(",");
        }catch(NullPointerException error){
            String[] interests = new String[]{};
            return interests;
        }

    }
}
