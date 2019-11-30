package com.delaroystudios.sqlitelogin.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
    Button signOut;

    private String managerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login_manager);

        appID = (TextView)findViewById(R.id.txtApplicationID1);
        name = (TextView)findViewById(R.id.txtName1);
        document = (TextView)findViewById(R.id.Document1);
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

    List<String> staffIDs = new ArrayList<>();
    staffIDs = databaseHelper.staffIDs(userEmail);
    System.out.println(staffIDs);
    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++==============================");

    List<String> list=new ArrayList<>();

    list = databaseHelper.getCustomerDetails(staffIDs);
    System.out.println(customer.getApp());
    System.out.println("========================================");

    for (int i=0; i<list.size(); i=i+3) {

        appID.setText(list.get(i));
        name.setText(list.get(i+1));
        document.setText(list.get(i+2));
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
