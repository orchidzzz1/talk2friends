package com.example.frontend.store;
import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {

    private static final String PREF_NAME = "Preferences";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_AUTHENTICATED = "isAuthenticated";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_INTERESTS = "interests";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FRIENDS = "friends";
    private static final String KEY_REQUESTS = "requests";

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
    public static void saveEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(KEY_EMAIL, "");
    }
    public static void saveFriends(Context context, String[] friends) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_FRIENDS, String.join(",",friends));
        editor.apply();
    }

    public static String[] getFriends(Context context) {
        try{
            return (getSharedPreferences(context).getString(KEY_FRIENDS, "")).split(",");
        }catch(NullPointerException error){
            String[] friends = new String[]{};
            return friends;
        }

    }
    public static void saveRequests(Context context, String[] requests) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_REQUESTS, String.join(",",requests));
        editor.apply();
    }

    public static String[] getRequests(Context context) {
        try{
            return (getSharedPreferences(context).getString(KEY_REQUESTS, "")).split(",");
        }catch(NullPointerException error){
            String[] requests = new String[]{};
            return requests;
        }

    }
}
