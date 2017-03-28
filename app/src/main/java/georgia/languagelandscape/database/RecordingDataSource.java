package georgia.languagelandscape.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.data.User;

public class RecordingDataSource {

    private Context context;
    private SQLiteDatabase database = null;
    private SQLiteOpenHelper dbHelper = null;
    public static final String LOG_TAG = "RecordingDatasource";

    public RecordingDataSource(Context context) {
        this.context = context;
        dbHelper = new RecordingReaderDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public Recording insertRecording(Recording recording) {
        ContentValues values = recording.toValues();
        database.insert(RecordingTableContract.TABLE_NAME, null, values);
        return recording;
    }

    public long getRecorgindCount() {
        return DatabaseUtils.queryNumEntries(database, RecordingTableContract.TABLE_NAME);
    }

    public boolean deleteRecording(String recordingID) {
        String whereClause = RecordingTableContract.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{recordingID};
        return database.delete(RecordingTableContract.TABLE_NAME, whereClause, whereArgs) > 0;
    }

    public List<Recording> getAllRecordings() {
        List<Recording> recordings = new ArrayList<>();
        Cursor cursor = database.query(
                RecordingTableContract.TABLE_NAME,
                RecordingTableContract.ALL_COLUMNS,
                null, null, null, null, null);
        Gson gson = new Gson();

        while (cursor.moveToNext()) {
            Recording recording = new Recording();
            recording.setRecordingID(cursor.getString(
                    cursor.getColumnIndex(RecordingTableContract.COLUMN_ID)));
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

            recordings.add(recording);
        }
        cursor.close();

        return recordings;
    }
}
