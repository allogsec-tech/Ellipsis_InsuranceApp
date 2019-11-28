package com.delaroystudios.sqlitelogin.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.delaroystudios.sqlitelogin.R;
import com.delaroystudios.sqlitelogin.helper.InputValidation;
import com.delaroystudios.sqlitelogin.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    private String username;
    private String userPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        initViews();
        System.out.println(textInputEditTextEmail);
        System.out.println(textInputEditTextEmail.getText());
        System.out.println(textInputEditTextEmail.getText().toString());
//        initListeners();
        initObjects();
    }
    private void initViews(){
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);

        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });

        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }

//    private void initListeners(){
//        appCompatButtonLogin.setOnClickListener(this);
//        textViewLinkRegister.setOnClickListener(this);
//    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);
    }

//    @Override
//    public void onClick(View v){
//        switch (v.getId()){
//            case R.id.appCompatButtonLogin:
//                verifyFromSQLite();
//
//                break;
//            case R.id.textViewLinkRegister:
//                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
//                startActivity(intentRegister);
//                break;
//        }
//    }

    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {
            username = textInputEditTextEmail.getText().toString().trim();
            System.out.println(username);

            userPost = databaseHelper.checkPost(username);

            System.out.println(userPost+"in login");
            String postStaff = "Staff";
            String postManager = "Manager";
            if (userPost.equals(postStaff)){
            Intent accountsIntent = new Intent(activity, PostLogin.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            }
            else if (userPost.equals(postManager)){
                Intent managerLogin = new Intent(activity, PostLoginManager.class);
                startActivity(managerLogin);
//                Toast.makeText(activity, "Sorry You are Manager", Toast.LENGTH_SHORT).show();

            }
        } else {
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
    public String sendEmail(){
        String useremail = null;
        useremail = textInputEditTextEmail.getText().toString().trim();
        return useremail;
    }
}
