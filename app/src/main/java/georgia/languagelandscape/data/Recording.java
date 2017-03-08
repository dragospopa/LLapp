package georgia.languagelandscape.data;

import java.util.ArrayList;

public class Recording {
    /*
    * Requirements:
    *   -a name
    *   -duration
    *   -description
    *   -geolocation
    *   -name of the city/country
    *   -language spoken
    *   -date of upload
    *   -have uploader name, ie the user who uploaded it
    *   -optional picture
    *   -optional video
    * */
    private String title = null;
    private long duration = 0L; // in milliseconds
    private String description = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String location = null;
    private ArrayList<String> language = null;
    private String date = null;
    private String uploader = null;
    private ArrayList<String> speakers = null;

    public Recording() {
    }

    public Recording(String title,
                     long duration,
                     String description,
                     double latitude,
                     double longitude,
                     String location,
                     ArrayList<String> language,
                     String date,
                     String uploader,
                     ArrayList<String> speakers) {

        this.title = title;
        this.duration = duration;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.language = language;
        this.date = date;
        this.uploader = uploader;
        this.speakers = speakers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getLanguage() {
        return language;
    }

    public void setLanguage(ArrayList<String> language) {
        this.language = language;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public ArrayList<String> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(ArrayList<String> speakers) {
        this.speakers = speakers;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                ", language=" + language +
                ", date='" + date + '\'' +
                ", uploader='" + uploader + '\'' +
                ", speakers=" + speakers +
                '}';
    }
}
