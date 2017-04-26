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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import georgia.languagelandscape.data.Project;
import georgia.languagelandscape.data.Recording;
import georgia.languagelandscape.data.User;

/**
 * Project data provider that encapsulates creating, deleting projects and get all the associated
 * recordings with the Project object.
 * To use:
 *      <pre>
 *      {@code
 *      ProjectDataSource datasource = new ProjectDataSource(context);
 *      datasource.open();
 *      // do something with data
 *      datasource.close();}
 *      </pre>
 */
public class ProjectDataSource {

    private Context context;
    private SQLiteDatabase database = null;
    private SQLiteOpenHelper dbHelper = null;

    public ProjectDataSource(Context context) {
        this.context = context;
        dbHelper = new ProjectReaderDBHelper(context);
    }

    private static class ProjectTableContract implements BaseColumns {

        private ProjectTableContract() {
        }

        private static final String TABLE_NAME = "MyProjects";
        private static final String COLUMN_ID = "id";
        private static final String COLUMN_FULLNAME = "fullName";
        private static final String COLUMN_SHORTNAME = "shortName";
        private static final String COLUMN_DESCRIPTION = "description";
        private static final String COLUMN_OWNER = "owner";
        private static final String COLUMN_CONTRIBUTORS = "contributors";
        private static final String COLUMN_LANGUAGES = "languages";
        private static final String COLUMN_RECORDINGIDS = "recordingIDs";

        private static final String[] ALL_COLUMNS =
                {COLUMN_ID, COLUMN_FULLNAME, COLUMN_SHORTNAME, COLUMN_DESCRIPTION, COLUMN_OWNER,
                        COLUMN_CONTRIBUTORS, COLUMN_LANGUAGES, COLUMN_RECORDINGIDS};

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + COLUMN_ID + " TEXT PRIMARY KEY,"
                        + COLUMN_FULLNAME + " TEXT,"
                        + COLUMN_SHORTNAME + " TEXT,"
                        + COLUMN_DESCRIPTION + " TEXT,"
                        + COLUMN_OWNER + " TEXT,"
                        + COLUMN_CONTRIBUTORS + " TEXT,"
                        + COLUMN_LANGUAGES + " TEXT,"
                        + COLUMN_RECORDINGIDS + " TEXT" + ");";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static class ProjectReaderDBHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "LL_project.db";
        private static final int DATABASE_VERSION = 1;

        private ProjectReaderDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ProjectTableContract.SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(ProjectTableContract.SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    /**
     * Helper function to create a ContentValues object from Project
     * @param project Project object to be inserted into database
     * @return ContentValues object that represent the Project
     */
    private ContentValues toValues(Project project) {
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(ProjectTableContract.COLUMN_ID, project.getProjectID());
        values.put(ProjectTableContract.COLUMN_FULLNAME, project.getFullName());
        values.put(ProjectTableContract.COLUMN_SHORTNAME, project.getShortName());
        values.put(ProjectTableContract.COLUMN_DESCRIPTION, project.getDescription());

        String ownerString = gson.toJson(project.getOwner());
        values.put(ProjectTableContract.COLUMN_OWNER, ownerString);

        String contributorString = gson.toJson(project.getContributors());
        values.put(ProjectTableContract.COLUMN_CONTRIBUTORS, contributorString);

        String languagesString = gson.toJson(project.getLanguages());
        values.put(ProjectTableContract.COLUMN_LANGUAGES, languagesString);

        // insert the recordings ids instead of recordings themselves to avoid overhead
        if (project.getRecordingIDs().isEmpty()) {
            values.put(ProjectTableContract.COLUMN_RECORDINGIDS, "none");
        } else {
            String recordingIDString = gson.toJson(project.getRecordingIDs());
            values.put(ProjectTableContract.COLUMN_RECORDINGIDS, recordingIDString);
        }

        return values;
    }

    /**
     * Insert the a Project to database
     * @param project Project to be inserted
     * @return Project object inserted
     */
    public Project createProject(Project project) {
        ContentValues values = toValues(project);
        database.insert(ProjectTableContract.TABLE_NAME, null, values);
        return project;
    }

    /**
     * Given a project's id, delete the project from database
     * @param projectID project's id representing the Project to be deleted
     * @return true if the project is deleted;
     *         false otherwise
     */
    public boolean deleteProject(String projectID) {
        String whereClause = ProjectTableContract.COLUMN_ID + "=?";
        String[] whereArgs = new String[]{projectID};
        return database.delete(ProjectTableContract.TABLE_NAME, whereClause, whereArgs) > 0;
    }

    /**
     * Callback when a recording is added/associated to a project
     * @param project the project to which recordings are added
     */
    public void notifyRecordingAdded(Project project) {
        deleteProject(project.getProjectID());
        createProject(project);
    }

    /**
     * Get all the Projects that the user has.
     * TODO: take a user id as parameter and return the user's projects
     * @return a List of Project the user has
     */
    public List<Project> getOwnedProjects() {
        List<Project> projects = new ArrayList<>();
        Cursor cursor = database.query(
                ProjectTableContract.TABLE_NAME,
                ProjectTableContract.ALL_COLUMNS,
                null, null, null, null, null);
        Gson gson = new Gson();
        RecordingDataSource recordingDataSource = new RecordingDataSource(context);
        recordingDataSource.open();

        while (cursor.moveToNext()) {
            Project project = new Project();
            project.setProjectID(
                    cursor.getString(cursor.getColumnIndex(ProjectTableContract.COLUMN_ID)));
            project.setFullName(
                    cursor.getString(cursor.getColumnIndex(ProjectTableContract.COLUMN_FULLNAME)));
            project.setShortName(
                    cursor.getString(cursor.getColumnIndex(ProjectTableContract.COLUMN_SHORTNAME)));
            project.setDescription(
                    cursor.getString(cursor.getColumnIndex(ProjectTableContract.COLUMN_DESCRIPTION)));

            Type type = new TypeToken<User>() {}.getType();
            User owner = gson.fromJson(
                    cursor.getString(
                            cursor.getColumnIndex(ProjectTableContract.COLUMN_OWNER)), type);
            project.setOwner(owner);

            type = new TypeToken<List<User>>() {}.getType();
            ArrayList<User> contributors = gson.fromJson(
                    cursor.getString(
                            cursor.getColumnIndex(ProjectTableContract.COLUMN_CONTRIBUTORS)), type);
            project.setContributors(contributors);

            type = new TypeToken<ArrayList<String>>() {}.getType();
            ArrayList<String> languages = gson.fromJson(
                    cursor.getString(
                            cursor.getColumnIndex(ProjectTableContract.COLUMN_LANGUAGES)), type);
            project.setLanguages(languages);

            // get all the recording ids first
            // then call the RecordingDataSource to get all recordings
            ArrayList<String> recordingIDs;
            String idString = cursor.getString(
                    cursor.getColumnIndex(ProjectTableContract.COLUMN_RECORDINGIDS));
            if (idString.equals("none")) {
                recordingIDs = new ArrayList<>();
            } else {
                recordingIDs = gson.fromJson(idString, type);
            }
            List<Recording> recordings = recordingDataSource.getAllRecordings(recordingIDs);
            project.setRecordings(recordings);

            projects.add(project);
        }
        recordingDataSource.close();
        cursor.close();
        return projects;
    }
}
