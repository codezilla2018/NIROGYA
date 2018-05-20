package codezilla.foss.nirogya.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NirogyaDataSource {
    private static final String TAG = "NirogyaDataSource";
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;
    private Context context;

    public NirogyaDataSource(Context context) {
        this.context = context;
        sqLiteOpenHelper = new NirogyaOpenDbHelper(context);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        Log.d(TAG, "Database opened.");
    }

    public void close() {
        sqLiteOpenHelper.close();
        Log.d(TAG, "Database closed.");
    }

    //Login Table
    public void insertDataUserDetails(String gender, String height, String weight, String status) {
        ContentValues newValues = new ContentValues();
        newValues.put("gender", gender);
        newValues.put("height", height);
        newValues.put("weight", weight);
        newValues.put("status", status);
        sqLiteDatabase.insertOrThrow("user_details", null, newValues);
    }
    //Login Table
    public void insertDataTwitterData(String id, String subscribedTime,String caption,String description) {
        ContentValues newValues = new ContentValues();
        newValues.put("_id", id);
        newValues.put("subscribed_time", subscribedTime);
        newValues.put("caption", caption);
        newValues.put("description", description);
        sqLiteDatabase.insertOrThrow("twitter_tips", null, newValues);
    }
    public String[] getAllDataFromTwitterData(String index) {
        Cursor cursor = sqLiteDatabase.query("twitter_tips", null, " _id=?",
                new String[]{index}, null, null, null);
        String[] response = new String[2];
        response[0]="NOT EXIST";
        if (cursor.getCount() < 1) {
            cursor.close();
            return response;
        }
        String[] arr = new String[4];
        cursor.moveToFirst();
        String subscribed_time = cursor.getString(cursor.getColumnIndex("subscribed_time"));
        String caption = cursor.getString(cursor.getColumnIndex("caption"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        arr[0]=subscribed_time;
        arr[1]=caption;
        arr[2]=description;
        cursor.close();
        return arr;
    }
    public void updateDataTwitterData(String index, String subscribedTime,String caption, String description ) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("_id", index);
        updatedValues.put("subscribed_time", subscribedTime);
        updatedValues.put("caption", caption);
        updatedValues.put("description", description);

        String where = "_id = ?";
        sqLiteDatabase.update("twitter_tips", updatedValues, where, new String[]{index});
    }


    public void insertDataPedometer(String index, String status, String step_goal, String steps, String miles, String calories, String time, String sensitivity) {
        ContentValues newValues = new ContentValues();
        newValues.put("_id", index);
        newValues.put("status", status);
        newValues.put("step_goal", step_goal);
        newValues.put("steps", steps);
        newValues.put("miles", miles);
        newValues.put("calories", calories);
        newValues.put("time", time);
        newValues.put("sensitivity", sensitivity);
        sqLiteDatabase.insertOrThrow("pedometer", null, newValues);
    }

    public String[] getDataUserDetails(String index) {
        Cursor cursor = sqLiteDatabase.query("user_details", null, " _id=?",
                new String[]{index}, null, null, null);

        cursor.moveToFirst();
        String[] arr = new String[3];
        String gender = cursor.getString(cursor.getColumnIndex("gender"));
        String height = cursor.getString(cursor.getColumnIndex("height"));
        String weight = cursor.getString(cursor.getColumnIndex("weight"));
        arr[0] = gender;
        arr[1] = height;
        arr[2] = weight;
        cursor.close();
        return arr;
    }

    public String getStatusFromUserDetails(String index) {
        Cursor cursor = sqLiteDatabase.query("user_details", null, " _id=?",
                new String[]{index}, null, null, null);
        String response = "NOT EXIST";
        if (cursor.getCount() < 1) {
            cursor.close();
            return response;
        }
        cursor.moveToFirst();
        String status = cursor.getString(cursor.getColumnIndex("status"));
        cursor.close();
        return status;
    }

    public String[] getAllDataFromPedometer(String index) {
        Cursor cursor = sqLiteDatabase.query("pedometer", null, " _id=?",
                new String[]{index}, null, null, null);
//        String[] response = new String[2];
//        response[0]= "NOT EXIST";
        String[] arr = new String[7];
//        if (cursor.getCount() < 1) {
//            cursor.close();
//            return response;
//        }
        cursor.moveToFirst();
        String status = cursor.getString(cursor.getColumnIndex("status"));
        String steps = cursor.getString(cursor.getColumnIndex("steps"));
        String miles = cursor.getString(cursor.getColumnIndex("miles"));
        String calories = cursor.getString(cursor.getColumnIndex("calories"));
        String step_goal = cursor.getString(cursor.getColumnIndex("step_goal"));
        String sensitivity = cursor.getString(cursor.getColumnIndex("sensitivity"));
        arr[0] = status;
        arr[1] = steps;
        arr[2] = miles;
        arr[3] = calories;
        arr[4] = step_goal;
        arr[5] = sensitivity;
        cursor.close();
        return arr;
    }
    public void updatePedomter(String index, String status, String steps, String miles, String calories) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("_id", index);
        updatedValues.put("status", status);
        updatedValues.put("steps", steps);
        updatedValues.put("miles", miles);
        updatedValues.put("calories", calories);
        String where = "_id = ?";
        sqLiteDatabase.update("pedometer", updatedValues, where, new String[]{index});
    }
    public void resetPedomter(String index, String status, String step_goal, String steps, String miles,String calories,String time,String sensitivity) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("_id", index);
        updatedValues.put("status", status);
        updatedValues.put("step_goal", step_goal);
        updatedValues.put("steps", steps);
        updatedValues.put("miles", miles);
        updatedValues.put("calories", calories);
        updatedValues.put("time", time);
        updatedValues.put("sensitivity", sensitivity);
        String where = "_id = ?";
        sqLiteDatabase.update("pedometer", updatedValues, where, new String[]{index});
    }

    public void updateStatusOfPedomter(String index, String status) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("status", status);
        String where = "_id = ?";
        sqLiteDatabase.update("pedometer", updatedValues, where, new String[]{index});
    }

    public void updateStepGoalPedomter(String index, String stepGoal) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("step_goal", stepGoal);
        String where = "_id = ?";
        sqLiteDatabase.update("pedometer", updatedValues, where, new String[]{index});
    }

    public void updateSensitivityPedomter(String index, String sensitivity) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("sensitivity", sensitivity);
        String where = "_id = ?";
        sqLiteDatabase.update("pedometer", updatedValues, where, new String[]{index});
    }

    public void updateWeightUserDetails(String index, String weight) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("weight", weight);
        String where = "_id = ?";
        sqLiteDatabase.update("user_details", updatedValues, where, new String[]{index});
    }

    public void updateHeightUserDetails(String index, String height) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("height", height);
        String where = "_id = ?";
        sqLiteDatabase.update("user_details", updatedValues, where, new String[]{index});
    }

    public void updateGenderUserDetails(String index, String gender) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("gender", gender);
        String where = "_id = ?";
        sqLiteDatabase.update("user_details", updatedValues, where, new String[]{index});
    }

    public void deleteTableTwitterTips() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS 'twitter_tips'; ");
        sqLiteDatabase.execSQL(NirogyaOpenDbHelper.DATABASE_TWITTER_TIPS_TABLE_CREATE);
    }
    public void deleteTablePedometer() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS 'pedometer'; ");
        sqLiteDatabase.execSQL(NirogyaOpenDbHelper.DATABASE_PEDOMETER_TABLE_CREATE);
    }
}
