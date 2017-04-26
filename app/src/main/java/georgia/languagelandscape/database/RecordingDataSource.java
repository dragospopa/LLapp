/**
 * Copyright (C) 2017 Language Landscape Organisation - All Rights Reserved
 *
 * Reference list:
 *      bumptech, Glide 3.7.0, 2016
 *
 */
package georgia.languagelandscape.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.data.User;

/**
 * The data provider for Recording.
 * To use:
 *      <pre>
 *      {@code
 *      RecordingDataSource datasource = new RecordingDataSource(context);
 *      datasource.open();
 *      // do something with data
 *      datasource.close();}
 *      </pre>
 */
public class RecordingDataSource {

    private Context context;
    private SQLiteDatabase database = null;
    private SQLiteOpenHelper dbHelper = null;
    public static final String LOG_TAG = "RecordingDatasource";

    public RecordingDataSource(Context context) {
        this.context = context;
        dbHelper = new RecordingReaderDBHelper(context);
    }

    private static class RecordingTableContract implements BaseColumns {
        private static final String TABLE_NAME = "MyRecordings";
        private static final String COLUMN_RECORDING_ID = "id";
        private static final String COLUMN_TITLE = "title";
        private static final String COLUMN_DURATION = "duration";
        private static final String COLUMN_DESCRIPTION = "description";
        private static final String COLUMN_LONGITUDE = "longitude";
        private static final String COLUMN_LATITUDE = "latitude";
        private static final String COLUMN_LOCATION = "location";
        private static final String COLUMN_LANGUAGE = "language";
        private static final String COLUMN_DATE = "date";
        private static final String COLUMN_UPLOADER = "uploader";
        private static final String COLUMN_SPEAKER = "speaker";
        private static final String COLUMN_FILEPATH = "filepath";

        private static final String[] ALL_COLUMNS =
                {COLUMN_RECORDING_ID, COLUMN_TITLE, COLUMN_DURATION,
                        COLUMN_DESCRIPTION, COLUMN_LONGITUDE, COLUMN_LATITUDE, COLUMN_LOCATION,
                        COLUMN_LANGUAGE, COLUMN_DATE, COLUMN_UPLOADER, COLUMN_SPEAKER,
                        COLUMN_FILEPATH};

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + COLUMN_RECORDING_ID + " TEXT PRIMARY KEY,"
                        + COLUMN_TITLE + " TEXT,"
                        + COLUMN_DURATION + " INTEGER,"
                        + COLUMN_DESCRIPTION + " TEXT,"
                        + COLUMN_LONGITUDE + " REAL,"
                        + COLUMN_LATITUDE + " REAL,"
                        + COLUMN_LOCATION + " TEXT,"
                        + COLUMN_LANGUAGE + " TEXT," // the arraylist will be stored as a json string
                        + COLUMN_DATE + " TEXT,"
                        + COLUMN_UPLOADER + " TEXT,"
                        + COLUMN_SPEAKER + " TEXT,"  // same goes for the arraylist of speakers
                        + COLUMN_FILEPATH + " TEXT" + ");";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        private RecordingTableContract() {}

    }

    private static class RecordingReaderDBHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "LL_recording.db";
        private static final int DATABASE_VERSION = 1;

        private RecordingReaderDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RecordingTableContract.SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(RecordingTableContract.SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public ContentValues toValues(Recording recording) {
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(RecordingTableContract.COLUMN_RECORDING_ID, recording.getRecordingID());
        values.put(RecordingTableContract.COLUMN_TITLE, recording.getTitle());
        values.put(RecordingTableContract.COLUMN_DURATION, recording.getDuration());
        values.put(RecordingTableContract.COLUMN_DESCRIPTION, recording.getDescription());
        values.put(RecordingTableContract.COLUMN_LATITUDE, recording.getLatitude());
        values.put(RecordingTableContract.COLUMN_LONGITUDE, recording.getLongitude());
        values.put(RecordingTableContract.COLUMN_LOCATION, recording.getLocation());
        values.put(RecordingTableContract.COLUMN_DATE, recording.getDate());
        values.put(RecordingTableContract.COLUMN_FILEPATH, recording.getFilePath());

        String languageString = gson.toJson(recording.getLanguage());
        values.put(RecordingTableContract.COLUMN_LANGUAGE, languageString);

        String uploaderString = gson.toJson(recording.getUploader());
        values.put(RecordingTableContract.COLUMN_UPLOADER, uploaderString);

        String speakerString = gson.toJson(recording.getSpeakers());
        values.put(RecordingTableContract.COLUMN_SPEAKER, speakerString);

        return values;
    }

    public Recording insertRecording(Recording recording) {
        ContentValues values = toValues(recording);
        database.insert(RecordingTableContract.TABLE_NAME, null, values);
        return recording;
    }

    public long getRecordingCount() {
        return DatabaseUtils.queryNumEntries(database, RecordingTableContract.TABLE_NAME);
    }

    public boolean deleteRecording(String recordingID) {
        String whereClause = RecordingTableContract.COLUMN_RECORDING_ID + "=?";
        String[] whereArgs = new String[]{recordingID};
        return database.delete(RecordingTableContract.TABLE_NAME, whereClause, whereArgs) > 0;
    }

    /**
     * Helper function to construct a recording, given a Cursor object.
     * The Cursor object is left as it is. It is the caller's responsibility to close the Cursor
     * @param cursor cursor pointing to a row. It's guaranteed that the cursor is pointing to
     *               at least one valid row.
     * @return a Recording construct from database
     */
    private Recording constructRecording(Cursor cursor) {
        Recording recording = new Recording();
        Gson gson = new Gson();
        recording.setRecordingID(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_RECORDING_ID)));
        recording.setTitle(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_TITLE)));
        recording.setDuration(cursor.getLong(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_DURATION)));
        recording.setLongitude(cursor.getDouble(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_LONGITUDE)));
        recording.setLatitude(cursor.getDouble(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_LATITUDE)));
        recording.setLocation(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_LOCATION)));
        recording.setDate(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_DATE)));
        recording.setDescription(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_DESCRIPTION)));
        recording.setFilePath(cursor.getString(
                cursor.getColumnIndex(RecordingTableContract.COLUMN_FILEPATH)));

        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String languageJsonString =
                cursor.getString(cursor.getColumnIndex(RecordingTableContract.COLUMN_LANGUAGE));
        ArrayList<String> language = gson.fromJson(languageJsonString, type);
        recording.setLanguage(language);

        String speakerJsonString =
                cursor.getString(cursor.getColumnIndex(RecordingTableContract.COLUMN_SPEAKER));
        ArrayList<String> speaker = gson.fromJson(speakerJsonString, type);
        recording.setSpeakers(speaker);

        type = new TypeToken<User>() {}.getType();
        String uploaderJsonString =
                cursor.getString(cursor.getColumnIndex(RecordingTableContract.COLUMN_UPLOADER));
        User uploader = gson.fromJson(uploaderJsonString, type);
        recording.setUploader(uploader);

        return recording;
    }

    /**
     * Get all recordings in the database
     * @return a List of Recordings from the database
     */
    public List<Recording> getAllRecordings() {
        List<Recording> recordings = new ArrayList<>();
        Cursor cursor = database.query(
                RecordingTableContract.TABLE_NAME,
                RecordingTableContract.ALL_COLUMNS,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            recordings.add(constructRecording(cursor));
        }
        cursor.close();

        return recordings;
    }

    /**
     * Get all the Recordings from the database that are specified by their ids
     * @param recordingIDs a List of String of the ids of the recordings we want
     * @return a List of Recording matching the id. The list size maybe smaller than the list of ids
     *         as a recording may not be found in the database.
     */
    public List<Recording> getAllRecordings(List<String> recordingIDs) {
        List<Recording> recordings = new ArrayList<>();
        Cursor cursor;
        for (String recordingID : recordingIDs) {
            String[] selectionArgs = {recordingID};
            cursor = database.query(
                    RecordingTableContract.TABLE_NAME,
                    RecordingTableContract.ALL_COLUMNS,
                    RecordingTableContract.COLUMN_RECORDING_ID + "=?",
                    selectionArgs,
                    null, null, null);
            if (cursor.moveToNext()) {
                recordings.add(constructRecording(cursor));
            }
            cursor.close();
        }
        return recordings;
    }
}
