package com.example.frontend.ClientAPI;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.gson.Gson;
import com.example.frontend.store.LocalStorage;
import com.example.frontend.models.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import android.content.Context;
public class ClientAPI{
    private static final String BASE_URL = "https://talk2friends.cyclic.app/api";
    private final API API;
    private String token;
    private final static LocalStorage store = new LocalStorage();
    private final Context context;
    public ClientAPI(Context context){
        this.context = context;
        // get token
        this.token = store.getToken(this.context);
        this.API = new API(BASE_URL, this.token);

    }

    public boolean authorize() throws JSONException {
        JSONObject res = this.API.makeGetRequest("/user/authorize", new JSONObject());
        boolean isVerified = res.getBoolean("isVerified");
        store.saveAuthenticated(this.context, isVerified);
        return isVerified;
    }
    public User getUser() throws JSONException {
        JSONObject res = API.makeGetRequest("/user/get", new JSONObject());
        JSONObject userJson = res.getJSONObject("user");
        userJson.remove("pk");
        userJson.remove("sk");
        System.out.println(userJson);
        Gson gson = new Gson();
        User user = gson.fromJson(String.valueOf(userJson), User.class);
        // save some info to local storage
        store.saveUserName(this.context, user.getUserName());
        store.saveInterests(this.context, user.getInterests());
        store.saveFriends(this.context, user.getFriends());
        store.saveRequests(this.context, user.getRequests());
        return user;

    }
    public void addUser(User user) throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.toJson(user));
        System.out.println(user);
        System.out.println(json);
        API.makePostRequest("/user/add", json);
    }

    public void updateUser(User user) throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.toJson(user));
        API.makePostRequest("/user/update", json);
    }
    public void addInterest(String interest) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("interest", interest);
        API.makePostRequest("/user/addInterest", params);
    }
    public void removeInterest(String interest) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("interest", interest);
        API.makePostRequest("/user/removeInterest", params);
    }

    public void addFriend(String friendEmail) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("friendEmail", friendEmail);
        API.makePostRequest("/user/friends/add", params);
    }

    public void removeFriend(String friendEmail) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("friendEmail", friendEmail);
        API.makePostRequest("/user/friends/remove", params);
    }

    public void sendVerification() {
        API.makePostRequest("/user/sendVerification", new JSONObject());
    }
    public void recommend(String email) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("email", email);
        String userName = store.getUserName(this.context);
        params.put("userName", userName);

        API.makePostRequest("/user/recommend", params);
    }
    public Map<String, List<String>> getRecommended() throws JSONException {
        JSONObject params = new JSONObject();
        String[] interests = store.getInterests(this.context);
        JSONArray jsonInterests = new JSONArray(interests);
        System.out.println(jsonInterests.length());

        params.put("interests", jsonInterests);
        JSONObject res = API.makeGetRequest("/user/friends/recommended", params);
        JSONObject usersJson = res.getJSONObject("users");
        Map<String, List<String>> users = new HashMap();

        // parse to create array of users
        for (Iterator<?> it = usersJson.keys(); it.hasNext(); ) {
            Object key = it.next();
            List<String> userInterests = new ArrayList<>();
            JSONArray vals = usersJson.getJSONArray(key.toString());
            for (int i = 0; i < vals.length(); i++) {
                System.out.println(i);
                String val = vals.getString(i);
                userInterests.add(val);
            }
            users.put(key.toString(), userInterests);
        }
        return users;
    }
    public boolean verify(int code) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("code", code);
        JSONObject res = API.makePostRequest("/user/verify", params);
        return (boolean) res.get("isVerified");
    }

    public void addMeeting(Meeting meeting) throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.toJson(meeting));
        API.makePostRequest("/meeting/create", json);
    }

    public Meeting[] getMeetings() throws JSONException {
        JSONObject res = API.makeGetRequest("/meeting/get", new JSONObject());
        Gson gson = new Gson();
        // parse to create array of meetings
        JSONArray jsonArr = res.getJSONArray("meetings");
        for (int i = 0; i < jsonArr.length(); i++) {
            JSONObject meeting = jsonArr.getJSONObject(i);
            meeting.remove("pk");
            meeting.put("meetingId", meeting.get("sk"));
            meeting.remove("sk");
        }

        Type MeetingList = new TypeToken<Meeting[]>(){}.getType();
        return gson.fromJson(String.valueOf(jsonArr), MeetingList);
    }

    public void editMeeting(Meeting meeting) throws JSONException {
        Gson gson = new Gson();
        JSONObject json = new JSONObject(gson.toJson(meeting));
        API.makePostRequest("/meeting/update", json);
    }

    public void removeMeeting(String meetingId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("meetingId", meetingId);
        API.makePostRequest("/meeting/remove", params);
    }

    public void rsvp(String meetingId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("meetingId", meetingId);
        API.makePostRequest("/meeting/rsvp", params);
    }
    public void cancel(String meetingId) throws JSONException {
        JSONObject params = new JSONObject();
        params.put("meetingId", meetingId);
        API.makePostRequest("/meeting/cancel", params);
    }


}