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
    private String DataBaseOpenedMessage = "Database opened.";
    private String DataBaseClosedMessage = "Database closed.";
    private String userDetailsIndexKey ="_id";
    private String userDetailsGenderKey ="gender";
    private String userDetailsHeightKey ="height";
    private String userDetailsWeightKey ="weight";
    private String userDetailsStatusKey ="status";
    private String pedometerIndexKey="_id";
    private String pedometerStatusKey="status";
    private String pedometerStepGoalKey="step_goal";
    private String pedometerStepsKey="steps";
    private String pedometerMilesKey="miles";
    private String pedometerCaloriesKey="calories";
    private String pedometerTimeKey="time";
    private String pedometerSensitivityKey="sensitivity";
    private String twitterTipsIndexKey ="_id";
    private String twitterTipsSubscribedTimeKey ="subscribed_time";
    private String twitterTipsCaptionKey ="caption";
    private String twitterTipsDescriptionKey ="description";
    private String userDetailsTable = "user_details";
    private String pedometerTable ="pedometer";
    private String twitterTipsTable = "twitter_tips";
    private String notExistMessage = "NOT EXIST";


    public NirogyaDataSource(Context context) {
        this.context = context;
        sqLiteOpenHelper = new NirogyaOpenDbHelper(context);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        Log.d(TAG, DataBaseOpenedMessage);
    }

    public void close() {
        sqLiteOpenHelper.close();
        Log.d(TAG, DataBaseClosedMessage);
    }

    //--------------------------InsertQuery-----------------------------
    public void insertDataUserDetails(String gender, String height, String weight, String status) {
        ContentValues newValues = new ContentValues();
        newValues.put(userDetailsGenderKey, gender);
        newValues.put(userDetailsHeightKey, height);
        newValues.put(userDetailsWeightKey, weight);
        newValues.put(userDetailsStatusKey, status);
        sqLiteDatabase.insertOrThrow(userDetailsTable, null, newValues);
    }
    public void insertDataTwitterData(String id, String subscribedTime,String caption,String description) {
        ContentValues newValues = new ContentValues();
        newValues.put(twitterTipsIndexKey, id);
        newValues.put(twitterTipsSubscribedTimeKey, subscribedTime);
        newValues.put(twitterTipsCaptionKey, caption);
        newValues.put(twitterTipsDescriptionKey, description);
        sqLiteDatabase.insertOrThrow(twitterTipsTable, null, newValues);
    }
    public void insertDataPedometer(String index, String status, String step_goal, String steps, String miles, String calories, String time, String sensitivity) {
        ContentValues newValues = new ContentValues();
        newValues.put(pedometerIndexKey, index);
        newValues.put(pedometerStatusKey, status);
        newValues.put(pedometerStepGoalKey, step_goal);
        newValues.put(pedometerStepsKey, steps);
        newValues.put(pedometerMilesKey, miles);
        newValues.put(pedometerCaloriesKey, calories);
        newValues.put(pedometerTimeKey, time);
        newValues.put(pedometerSensitivityKey, sensitivity);
        sqLiteDatabase.insertOrThrow(pedometerTable, null, newValues);
    }
    //----------------------------------------------------------------
    //------------------------RetriveQuery----------------------------

    public String[] getAllDataFromTwitterData(String index) {
        Cursor cursor = sqLiteDatabase.query(twitterTipsTable, null, " _id=?",
                new String[]{index}, null, null, null);
        String[] response = new String[2];
        response[0]=notExistMessage;
        if (cursor.getCount() < 1) {
            cursor.close();
            return response;
        }
        String[] arr = new String[4];
        cursor.moveToFirst();
        String subscribed_time = cursor.getString(cursor.getColumnIndex(twitterTipsSubscribedTimeKey));
        String caption = cursor.getString(cursor.getColumnIndex(twitterTipsCaptionKey));
        String description = cursor.getString(cursor.getColumnIndex(twitterTipsDescriptionKey));
        arr[0]=subscribed_time;
        arr[1]=caption;
        arr[2]=description;
        cursor.close();
        return arr;
    }

    public String[] getDataUserDetails(String index) {
        Cursor cursor = sqLiteDatabase.query(userDetailsTable, null, " _id=?",
                new String[]{index}, null, null, null);
        cursor.moveToFirst();
        String[] arr = new String[3];
        String gender = cursor.getString(cursor.getColumnIndex(userDetailsGenderKey));
        String height = cursor.getString(cursor.getColumnIndex(userDetailsHeightKey));
        String weight = cursor.getString(cursor.getColumnIndex(userDetailsWeightKey));
        arr[0] = gender;
        arr[1] = height;
        arr[2] = weight;
        cursor.close();
        return arr;
    }

    public String getStatusFromUserDetails(String index) {
        Cursor cursor = sqLiteDatabase.query(userDetailsTable, null, " _id=?",
                new String[]{index}, null, null, null);
        String response = notExistMessage;
        if (cursor.getCount() < 1) {
            cursor.close();
            return response;
        }
        cursor.moveToFirst();
        String status = cursor.getString(cursor.getColumnIndex(userDetailsStatusKey));
        cursor.close();
        return status;
    }

    public String[] getAllDataFromPedometer(String index) {
        Cursor cursor = sqLiteDatabase.query(pedometerTable, null, " _id=?",
                new String[]{index}, null, null, null);
        String[] arr = new String[7];
        cursor.moveToFirst();
        String status = cursor.getString(cursor.getColumnIndex(pedometerStatusKey));
        String steps = cursor.getString(cursor.getColumnIndex(pedometerStepsKey));
        String miles = cursor.getString(cursor.getColumnIndex(pedometerMilesKey));
        String calories = cursor.getString(cursor.getColumnIndex(pedometerCaloriesKey));
        String step_goal = cursor.getString(cursor.getColumnIndex(pedometerStepGoalKey));
        String sensitivity = cursor.getString(cursor.getColumnIndex(pedometerSensitivityKey));
        arr[0] = status;
        arr[1] = steps;
        arr[2] = miles;
        arr[3] = calories;
        arr[4] = step_goal;
        arr[5] = sensitivity;
        cursor.close();
        return arr;
    }

    //---------------------------------------------------------------
    //----------------------UpdateQusery-----------------------------

    public void updateDataTwitterData(String index, String subscribedTime,String caption, String description ) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(twitterTipsIndexKey, index);
        updatedValues.put(twitterTipsSubscribedTimeKey, subscribedTime);
        updatedValues.put(twitterTipsCaptionKey, caption);
        updatedValues.put(twitterTipsDescriptionKey, description);

        String where = "_id = ?";
        sqLiteDatabase.update(twitterTipsTable, updatedValues, where, new String[]{index});
    }

    public void updatePedomter(String index, String status, String steps, String miles, String calories) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(pedometerIndexKey, index);
        updatedValues.put(pedometerStatusKey, status);
        updatedValues.put(pedometerStepsKey, steps);
        updatedValues.put(pedometerMilesKey, miles);
        updatedValues.put(pedometerCaloriesKey, calories);
        String where = "_id = ?";
        sqLiteDatabase.update(pedometerTable, updatedValues, where, new String[]{index});
    }

    public void resetPedomter(String index, String status, String step_goal, String steps, String miles,String calories,String time,String sensitivity) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(pedometerIndexKey, index);
        updatedValues.put(pedometerStatusKey, status);
        updatedValues.put(pedometerStepGoalKey, step_goal);
        updatedValues.put(pedometerStepsKey, steps);
        updatedValues.put(pedometerMilesKey, miles);
        updatedValues.put(pedometerCaloriesKey, calories);
        updatedValues.put(pedometerTimeKey, time);
        updatedValues.put(pedometerSensitivityKey, sensitivity);
        String where = "_id = ?";
        sqLiteDatabase.update(pedometerTable, updatedValues, where, new String[]{index});
    }

    public void updateStatusOfPedomter(String index, String status) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(pedometerStatusKey, status);
        String where = "_id = ?";
        sqLiteDatabase.update(pedometerTable, updatedValues, where, new String[]{index});
    }

    public void updateStepGoalPedomter(String index, String stepGoal) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(pedometerStepGoalKey, stepGoal);
        String where = "_id = ?";
        sqLiteDatabase.update(pedometerTable, updatedValues, where, new String[]{index});
    }

    public void updateSensitivityPedomter(String index, String sensitivity) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(pedometerSensitivityKey, sensitivity);
        String where = "_id = ?";
        sqLiteDatabase.update(pedometerTable, updatedValues, where, new String[]{index});
    }

    public void updateWeightUserDetails(String index, String weight) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(userDetailsWeightKey, weight);
        String where = "_id = ?";
        sqLiteDatabase.update(userDetailsTable, updatedValues, where, new String[]{index});
    }

    public void updateHeightUserDetails(String index, String height) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(userDetailsHeightKey, height);
        String where = "_id = ?";
        sqLiteDatabase.update(userDetailsTable, updatedValues, where, new String[]{index});
    }

    public void updateGenderUserDetails(String index, String gender) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(userDetailsGenderKey, gender);
        String where = "_id = ?";
        sqLiteDatabase.update(userDetailsTable, updatedValues, where, new String[]{index});
    }
    //--------------------------------------------------------------
    //-------------------------DeleteQuery--------------------------
    public void deleteTableTwitterTips() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS 'twitter_tips'; ");
        sqLiteDatabase.execSQL(NirogyaOpenDbHelper.DATABASE_TWITTER_TIPS_TABLE_CREATE);
    }
    public void deleteTablePedometer() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS 'pedometer'; ");
        sqLiteDatabase.execSQL(NirogyaOpenDbHelper.DATABASE_PEDOMETER_TABLE_CREATE);
    }
}
