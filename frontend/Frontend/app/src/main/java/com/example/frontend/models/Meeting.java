package com.example.frontend.models;
import java.util.ArrayList;
import java.util.List;


public class Meeting {
    private String meetingId;
    private String title;
    private String datetime;
    private String location;

    private String description;
    private String[] participants;

    // Constructor
    public Meeting(String meetingId, String title, String datetime, String[] participants, String description, String location) {
        this.meetingId = meetingId;
        this.title = title;
        this.datetime = datetime;
        this.participants = participants;
        this.location = location;
        this.description = description;
    }

    // Getters
    public String getDescription() {
        return description;
    }
    public String getMeetingId() {
        return meetingId;
    }
    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getDatetime() {
        return datetime;
    }

    public String[] getParticipants() {
        return participants;
    }
}


