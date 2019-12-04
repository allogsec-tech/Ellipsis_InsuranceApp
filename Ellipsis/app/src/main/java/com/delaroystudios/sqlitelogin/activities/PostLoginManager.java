package com.delaroystudios.sqlitelogin.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.sqlitelogin.R;
import com.delaroystudios.sqlitelogin.model.Customer;
import com.delaroystudios.sqlitelogin.model.User;
import com.delaroystudios.sqlitelogin.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PostLoginManager extends AppCompatActivity {

    private final AppCompatActivity activity = PostLoginManager.this;

    private DatabaseHelper databaseHelper;
    private Cursor cursor;
    private Customer customer;
    private User user;
    TextView appID;
    TextView name;
    TextView document;
    TextView tv2;
    Button signOut;

    private String managerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login_manager);

//        appID = (TextView)findViewById(R.id.txtApplicationID1);
//        name = (TextView)findViewById(R.id.txtName1);
//        document = (TextView)findViewById(R.id.Document1);
          tv2 = (TextView)findViewById(R.id.tv2);
        signOut = (Button)findViewById(R.id.signOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signingOut();
            }
        });

        System.out.println(managerEmail);
        initObjects();
        abc();


}
private void initObjects(){
    databaseHelper = new DatabaseHelper(activity);
    customer = new Customer();
    user = new User();
}
private void abc(){
    Bundle extras = getIntent().getExtras();


    String userEmail = extras.getString("EMAIL");
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==============================");
    System.out.println(userEmail);
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==============================");
    tv2.setText("Welcome "+userEmail);

    List<String> staffIDs = new ArrayList<>();
    staffIDs = databaseHelper.staffIDs(userEmail);
    System.out.println(staffIDs);
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==============================");

    List<String> list=new ArrayList<>();

    list = databaseHelper.getCustomerDetails(staffIDs);
    System.out.println(customer.getApp());
    System.out.println("========================================");

//    for (int i=0; i<list.size(); i=i+3) {
//
//        appID.setText(list.get(i));
//        name.setText(list.get(i+1));
//        document.setText(list.get(i+2));
//    }


    TableLayout stk = (TableLayout) findViewById(R.id.table_main);
    TableRow tbrow0 = new TableRow(this);
//    TextView tv0 = new TextView(this);
//    tv0.setText(" Sl.No ");
//    tv0.setTextColor(Color.WHITE);
//    tbrow0.addView(tv0);
    TextView tv1 = new TextView(this);
    tv1.setText(" Application ID ");
    tv1.setTextColor(Color.WHITE);
    tbrow0.addView(tv1);
    TextView tv2 = new TextView(this);
    tv2.setText(" Customer Name ");
    tv2.setTextColor(Color.WHITE);
    tbrow0.addView(tv2);
    TextView tv3 = new TextView(this);
    tv3.setText(" Document ");
    tv3.setTextColor(Color.WHITE);
    tbrow0.addView(tv3);
    stk.addView(tbrow0);
    for (int i = 0; i < list.size(); i=i+3) {
        TableRow tbrow = new TableRow(this);
//        TextView t1v = new TextView(this);
//        t1v.setText("" + i);
//        t1v.setTextColor(Color.WHITE);
//        t1v.setGravity(Gravity.CENTER);
//        tbrow.addView(t1v);
        TextView t2v = new TextView(this);
        t2v.setText(list.get(i));
        t2v.setTextColor(Color.WHITE);
        t2v.setGravity(Gravity.CENTER);
        tbrow.addView(t2v);
        TextView t3v = new TextView(this);
        t3v.setText(list.get(i+1));
        t3v.setTextColor(Color.WHITE);
        t3v.setGravity(Gravity.CENTER);
        tbrow.addView(t3v);
        TextView t4v = new TextView(this);
        t4v.setText(list.get(i+2));
        t4v.setTextColor(Color.WHITE);
        t4v.setGravity(Gravity.CENTER);
        tbrow.addView(t4v);
        stk.addView(tbrow);
        tbrow.setBackgroundResource(R.drawable.row_border);
    }

}


private void signingOut(){
    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
    startActivity(i);
    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
}

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you really want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();


                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}
