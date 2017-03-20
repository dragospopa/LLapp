package georgia.languagelandscape.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordingReaderDBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "LL.db";
    public static final int DATABASE_VERSION = 1;

    public RecordingReaderDBHelper(Context context) {
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
