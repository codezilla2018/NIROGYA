package codezilla.foss.nirogya.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class NirogyaOpenDbHelper extends SQLiteOpenHelper implements BaseColumns {
    private static final String DATABASE_NAME = "nirogya";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "NirogyaOpenDbHelper";

    public NirogyaOpenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //create user_details table
    private static final String DATABASE_USER_DETAILS_TABLE_CREATE = "create table " + "user_details" + "( "
            + _ID + " integer primary key autoincrement,"
            + "gender  text,height text,weight text,status text); ";
    //create pedometer table
    public static final String DATABASE_PEDOMETER_TABLE_CREATE = "create table " + "pedometer" + "( "
            + _ID + " integer primary key autoincrement,"
            + "status  text,step_goal text,steps text,miles text,calories text,time text,sensitivity text); ";

    public static final String DATABASE_TWITTER_TIPS_TABLE_CREATE = "create table " + "twitter_tips" + "( "
            + _ID + " integer primary key autoincrement,"
            + "subscribed_time  text, caption text,description text); ";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_USER_DETAILS_TABLE_CREATE);
        Log.d(TAG, DATABASE_USER_DETAILS_TABLE_CREATE);
        //------------------------
        db.execSQL(DATABASE_PEDOMETER_TABLE_CREATE);
        Log.d(TAG, DATABASE_PEDOMETER_TABLE_CREATE);

        db.execSQL(DATABASE_TWITTER_TIPS_TABLE_CREATE);
        Log.d(TAG, DATABASE_TWITTER_TIPS_TABLE_CREATE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'user_details'; ");
        Log.d(TAG, "DROP TABLE IF EXISTS 'user_details'; ");
        onCreate(db);
    }
}
