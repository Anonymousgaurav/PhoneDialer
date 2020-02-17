package customphonedialer.abror96.customphonedialer.helperClass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

import customphonedialer.abror96.customphonedialer.Model.Interested_Model;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserManagerData.db";

    // User table name
    private static final String TABLE_USER = "user";


    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_MOBILE = "user_name";



    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_MOBILE + " TEXT" + ")";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //if exist
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        onCreate(sqLiteDatabase);
    }


    //add user
    public void addUser(Interested_Model Interested_Model) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TABLE_USER + "(" + COLUMN_USER_MOBILE  + ") VALUES (" + "'" + Interested_Model.getImobilenum() + "')";
        db.execSQL(query);
        db.close();
    }

    //get All user
    public List<Interested_Model> getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);
        List<Interested_Model> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Interested_Model interested_model = new Interested_Model();
            interested_model.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            interested_model.setImobilenum(cursor.getString(cursor.getColumnIndex(COLUMN_USER_MOBILE)));
            list.add(interested_model);
        }
        return list;
    }
}
