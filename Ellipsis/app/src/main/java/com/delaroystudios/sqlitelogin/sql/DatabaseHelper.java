package com.delaroystudios.sqlitelogin.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.delaroystudios.sqlitelogin.R;
import com.delaroystudios.sqlitelogin.activities.PostLogin;
import com.delaroystudios.sqlitelogin.model.Customer;
import com.delaroystudios.sqlitelogin.model.User;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DatabaseHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "UserManager.db";

    private static final String TABLE_USER = "user";
    private static final String TABLE_CUSTOMER = "customer";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_POST = "user_post";
    private static final String COLUMN_USER_WORKING_UNDER = "user_workingunder";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    private static final String COLUMN_CUSTOMER_ID = "customer_id";
    private static final String COLUMN_CUSTOMER_APPID = "customer_app";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";
    private static final String COLUMN_CUSTOMER_DOCUMENT = "customer_document";
    private static final String COlUMN_STAFF = "staff_id";

    private User user;

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_POST + " TEXT," + COLUMN_USER_WORKING_UNDER + " TEXT,"  + COLUMN_USER_PASSWORD + " TEXT" + ")";

    private String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + TABLE_CUSTOMER + "("
            + COLUMN_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CUSTOMER_APPID + " TEXT,"
            + COLUMN_CUSTOMER_NAME + " TEXT," + COLUMN_CUSTOMER_DOCUMENT + " TEXT," + COlUMN_STAFF + " TEXT" + ")";


    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    private String DROP_CUSTOMER_TABLE = "DROP TABLE IF EXISTS " + TABLE_CUSTOMER;


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CUSTOMER_TABLE);
        onCreate(db);
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_POST, user.getPost());
        values.put(COLUMN_USER_WORKING_UNDER, user.getWorkingUnder());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public void addCustomerDetails(Customer customer, String userEmail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println(customer.getApp());
        System.out.println(customer.getName());
        System.out.println(customer.getDocument());
        values.put(COLUMN_CUSTOMER_APPID, customer.getApp());
        values.put(COLUMN_CUSTOMER_NAME, customer.getName());
        values.put(COLUMN_CUSTOMER_DOCUMENT, customer.getDocument());


        String staffID = null;
        SQLiteDatabase dbdummy = this.getWritableDatabase();
        String query = "SELECT user_id FROM user WHERE user_email ='"+userEmail+"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {


            staffID =  cursor.getString(cursor.getColumnIndex("user_id"));

        }
        System.out.println(staffID);
        System.out.println("++++++++++++++++++++++++++++=================================");

        values.put(COlUMN_STAFF, staffID);





        long abc = db.insert(TABLE_CUSTOMER, null, values);
        if(abc!=-1){
            System.out.println("DJBBSK======================================================================");
        }
        db.close();
    }

    public boolean checkUser(String email){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    public String checkPost(String username){
        String abc = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT user_post FROM user WHERE user_email ='"+username+"'";
        System.out.println(username+"db helper");
        Cursor  cursor = db.rawQuery(query,null);
       // Cursor findEntry = db.query("user", columns , "user_email=?", new String[] { user_email }, null, null, null);
        //String str = cursor.getString(cursor.getColumnIndex("content"));
        if (cursor.moveToFirst()) {


                abc =  cursor.getString(cursor.getColumnIndex("user_post"));
                System.out.println(abc);

        }
        return abc;
    }

    public String getManagerEmail(String staffEmail){
        String managerEmail = null;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT user_workingunder FROM user WHERE user_email ='"+staffEmail+"'";

        Cursor  cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {


            managerEmail =  cursor.getString(cursor.getColumnIndex("user_workingunder"));
            System.out.println(managerEmail);

        }
        return managerEmail;

    }

    public List getCustomerDetails(List staffIDs) {

        String roundStaffIDs = staffIDs.toString().replace("[","(").replace("]",")");
        String selectQuery = "SELECT customer_app,customer_name,customer_document FROM customer WHERE staff_id IN "+roundStaffIDs ;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");

//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                String name = cursor.getString(cursor.getColumnIndex("customer_app"));
//
//                System.out.println(name);
//                cursor.moveToNext();
//            }
//        }
        List<String> list=new ArrayList<>();

//        while (cursor.moveToNext()) {
//            Customer customer = new Customer();
//            customer.setApp(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_APPID)));
//            customer.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME)));
//            customer.setDocument(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_DOCUMENT)));
//            System.out.println(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_APPID)));
//            System.out.println(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME)));
//            System.out.println(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_DOCUMENT)));
//        }
        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setApp(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_APPID)));
                customer.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME)));
                customer.setDocument(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_DOCUMENT)));
                list.add(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_APPID)));
                list.add(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME)));
                list.add(cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_DOCUMENT)));

                // cursor.moveToNext();

            } while (cursor.moveToNext());
        }
        System.out.println(list+"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        db.close();

    return list;
    }

    public List staffIDs(String managerEmail){
        List<String> staffIDs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(managerEmail);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        String query = "SELECT user_id FROM user WHERE user_workingunder ='"+managerEmail+"'";
        Cursor  cursor = db.rawQuery(query,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            staffIDs.add(cursor.getString(cursor.getColumnIndex("user_id")));
            cursor.moveToNext();
        }
        return staffIDs;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
}
