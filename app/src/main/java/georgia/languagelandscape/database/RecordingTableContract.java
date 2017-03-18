package georgia.languagelandscape.database;

import android.provider.BaseColumns;

public final class RecordingTableContract implements BaseColumns {
    public static final String TABLE_NAME = "My Recordings";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DURATION = "My Recordings";
    public static final String COLUMN_DESCRIPTION = "My Recordings";
    public static final String COLUMN_LONGITUDE = "My Recordings";
    public static final String COLUMN_LATITUDE = "My Recordings";
    public static final String COLUMN_LOCATION = "My Recordings";
    public static final String COLUMN_LANGUAGE = "My Recordings";
    public static final String COLUMN_DATE = "My Recordings";
    public static final String COLUMN_UPLOADER = "My Recordings";
    public static final String COLUMN_SPEAKER = "My Recordings";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ("
            + RecordingTableContract._ID + " INTEGER PRIMARY KEY,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DURATION + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_LONGITUDE + " REAL,"
            + COLUMN_LATITUDE + " REAL,"
            + COLUMN_LOCATION + " TEXT,"
            + COLUMN_LANGUAGE + " TEXT," // the arraylist will be stored as a json string
            + COLUMN_DATE + " TEXT,"
            + COLUMN_UPLOADER + " TEXT,"
            + COLUMN_SPEAKER + " TEXT)"; // same goes for the arraylist of speakers

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private RecordingTableContract() {}

}