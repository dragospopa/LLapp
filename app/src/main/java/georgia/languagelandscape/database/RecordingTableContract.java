package georgia.languagelandscape.database;

import android.provider.BaseColumns;

public final class RecordingTableContract implements BaseColumns {
    public static final String TABLE_NAME = "MyRecordings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_LANGUAGE = "language";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_UPLOADER = "uploader";
    public static final String COLUMN_SPEAKER = "speaker";

    public static final String[] ALL_COLUMNS =
            {COLUMN_ID, COLUMN_TITLE, COLUMN_DURATION, COLUMN_DESCRIPTION, COLUMN_LONGITUDE,
                    COLUMN_LATITUDE, COLUMN_LOCATION, COLUMN_LANGUAGE, COLUMN_DATE, COLUMN_UPLOADER,
                    COLUMN_SPEAKER};

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " TEXT PRIMARY KEY,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_DURATION + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_LONGITUDE + " REAL,"
            + COLUMN_LATITUDE + " REAL,"
            + COLUMN_LOCATION + " TEXT,"
            + COLUMN_LANGUAGE + " TEXT," // the arraylist will be stored as a json string
            + COLUMN_DATE + " TEXT,"
            + COLUMN_UPLOADER + " TEXT,"
            + COLUMN_SPEAKER + " TEXT" + ");"; // same goes for the arraylist of speakers

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private RecordingTableContract() {}

}