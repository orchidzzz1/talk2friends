package com.example.frontend.models;
import java.util.ArrayList;
import java.util.List;
import com.example.frontend.models.Meeting;

public class User {
    private String userName;
    private String email;
    private String affiliation;
    private boolean international;
    private String[] interests;

    private String[] friends;
    private String[] requests;
    private Meeting[] createdMeetings;

    // Constructor
    public User(String userName, String email, String[] interests, String affiliation, boolean international, String[] friends, String[] requests, Meeting[] createdMeetings) {
        this.userName = userName;
        this.email = email;
        this.interests = interests;
        this.affiliation = affiliation;
        this.international = international;
        this.createdMeetings = createdMeetings;
        this.requests = requests;
        this.friends = friends;
    }
    public String[] getInterests() {
        return interests;
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
    public String getAffiliation() {
        return affiliation;
    }
    public boolean getInternational() {
        return international;
    }
    public Meeting[] getCreatedMeetings() {
        return createdMeetings;
    }
    public String[] getFriends() {
        return friends;
    }
    public String[] getRequests() {
        return requests;
    }
}