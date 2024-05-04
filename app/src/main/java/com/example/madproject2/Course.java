package com.example.madproject2;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {
    private String courseName;
    private String courseFee;
    private String duration;
    private String startingDate;
    private String publishedDate;
    private String closingDate;

    public Course(String courseName, String courseFee, String duration,
                  String startingDate, String publishedDate, String closingDate) {
        this.courseName = courseName;
        this.courseFee = courseFee;
        this.duration = duration;
        this.startingDate = startingDate;
        this.publishedDate = publishedDate;
        this.closingDate = closingDate;
    }

    protected Course(Parcel in) {
        courseName = in.readString();
        courseFee = in.readString();
        duration = in.readString();
        startingDate = in.readString();
        publishedDate = in.readString();
        closingDate = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getCourseName() {
        return courseName;
    }

    public String getCourseFee() {
        return courseFee;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return courseName; // Return the course name when toString() is called
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(courseFee);
        dest.writeString(duration);
        dest.writeString(startingDate);
        dest.writeString(publishedDate);
        dest.writeString(closingDate);
    }
}
