package georgia.languagelandscape.data;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

import georgia.languagelandscape.database.RecordingTableContract;

public class Recording implements Parcelable{

    private String recordingID = null;
    private String title = null;
    private long duration = 0L; // in milliseconds
    private String description = null;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String location = null;
    private ArrayList<String> language = null;
    private String date = null;
    private User uploader = null;
    private ArrayList<String> speakers = null;

    public Recording() {
        if (recordingID == null) {
            recordingID = UUID.randomUUID().toString();
        }
    }

    public Recording(
                     String ID,
                     String title,
                     long duration,
                     String description,
                     double latitude,
                     double longitude,
                     String location,
                     ArrayList<String> language,
                     String date,
                     User uploader,
                     ArrayList<String> speakers) {

        if (recordingID == null) {
            recordingID = UUID.randomUUID().toString();
        }

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

    public String getRecordingID() {
        return recordingID;
    }

    public void setRecordingID(String recordingID) {
        this.recordingID = recordingID;
    }

    public void setRecordingID() {
        this.recordingID = UUID.randomUUID().toString();
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

    public User getUploader() {
        return uploader;
    }

    public void setUploader(User uploader) {
        this.uploader = uploader;
    }

    public ArrayList<String> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(ArrayList<String> speakers) {
        this.speakers = speakers;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues(10);
        Gson gson = new Gson();

        values.put(RecordingTableContract.COLUMN_ID, recordingID);
        values.put(RecordingTableContract.COLUMN_TITLE, title);
        values.put(RecordingTableContract.COLUMN_DURATION, duration);
        values.put(RecordingTableContract.COLUMN_DESCRIPTION, description);
        values.put(RecordingTableContract.COLUMN_LATITUDE, latitude);
        values.put(RecordingTableContract.COLUMN_LONGITUDE, longitude);
        values.put(RecordingTableContract.COLUMN_LOCATION, location);
        values.put(RecordingTableContract.COLUMN_LANGUAGE, gson.toJson(language));
        values.put(RecordingTableContract.COLUMN_DATE, date);
        values.put(RecordingTableContract.COLUMN_UPLOADER, gson.toJson(uploader));
        values.put(RecordingTableContract.COLUMN_SPEAKER, gson.toJson(speakers));

        return values;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id='" + recordingID + '\'' +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();

        dest.writeString(recordingID);
        dest.writeString(title);
        dest.writeLong(duration);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(location);
        dest.writeStringList(language);
        dest.writeString(date);
        dest.writeString(gson.toJson(uploader));
        dest.writeStringList(speakers);
    }


    protected Recording(Parcel in) {
        Gson gson = new Gson();

        recordingID = in.readString();
        title = in.readString();
        duration = in.readLong();
        description = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        location = in.readString();
        language = in.createStringArrayList();
        date = in.readString();
        Type type = new TypeToken<User>() {}.getType();
        uploader = gson.fromJson(in.readString(), type);
        speakers = in.createStringArrayList();
    }

    public static final Creator<Recording> CREATOR = new Creator<Recording>() {
        @Override
        public Recording createFromParcel(Parcel in) {
            return new Recording(in);
        }

        @Override
        public Recording[] newArray(int size) {
            return new Recording[size];
        }
    };
}
