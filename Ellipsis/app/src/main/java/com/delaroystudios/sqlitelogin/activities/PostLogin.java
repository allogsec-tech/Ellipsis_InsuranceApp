package com.delaroystudios.sqlitelogin.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.delaroystudios.sqlitelogin.R;
import com.delaroystudios.sqlitelogin.model.Customer;
import com.delaroystudios.sqlitelogin.model.User;
import com.delaroystudios.sqlitelogin.sql.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.content.FileProvider.getUriForFile;

public class PostLogin extends AppCompatActivity {

    private final AppCompatActivity activity = PostLogin.this;
    Button signout;
    ImageView imageView;
    Button btnCamera, btnupload;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int requestCode;
    private int resultCode;
    private Intent data;
    String PathHolder;
    Uri URI;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private File f;
    EditText appIDET, custNameET, docET;
    private String appID, custName, doc;

    private DatabaseHelper databaseHelper;
    private Customer customer;
    private User user;
    private LoginActivity mlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initObjects();


        appIDET = (EditText)findViewById(R.id.appID);
        custNameET = (EditText)findViewById(R.id.custName);
        docET = (EditText)findViewById(R.id.doc);



        btnCamera = (Button) findViewById(R.id.btnCam);
        btnupload = (Button) findViewById(R.id.upload);



        Button btnfile = (Button) findViewById(R.id.btn_picker);

        signout = (Button) findViewById(R.id.button_sign_out);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraIntent();
            }
        });

        btnfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileIntent();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetails();
                email();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void email() {
        Bundle extras = getIntent().getExtras();

        appID = appIDET.getText().toString();
        custName = custNameET.getText().toString();
        doc = docET.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);

        String staffEmail = extras.getString("EMAIL");
        String managerEmail = databaseHelper.getManagerEmail(staffEmail);

        intent.setType("image/png");
        String to[] = {managerEmail};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, appID + "-" + custName);
        intent.putExtra(Intent.EXTRA_TEXT, "This " + doc + " belongs to " + custName + " whose application ID is " + appID);
        intent.putExtra(Intent.EXTRA_STREAM, URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void addDetails(){
        Bundle extras = getIntent().getExtras();

        appID = appIDET.getText().toString();
        custName = custNameET.getText().toString();
        doc = docET.getText().toString();
        customer.setApp(appID);
        customer.setName(custName);
        customer.setDocument(doc);
        String userEmail = extras.getString("EMAIL");
        System.out.println(userEmail);
        System.out.println("++++++++++++++++++++++++++++++++++++==================================================");

//        String useremail = mlogin.sendEmail();
//        System.out.println(useremail);
//        System.out.println("++++++++++++++++++++++++++++++++++++==================================================");
//        user.setEmail(useremail);


        System.out.println(appID);
        System.out.println(custName);
        System.out.println(doc);

        databaseHelper.addCustomerDetails(customer, userEmail);
    }

    private void fileIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, 7);
    }



    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        f = new File(android.os.Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void initObjects(){
        databaseHelper = new DatabaseHelper(activity);
        customer = new Customer();
        user = new User();
        mlogin = new LoginActivity();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 7) {
                URI = data.getData();
                Toast.makeText(this, URI.toString(), Toast.LENGTH_LONG).show();
            }
            else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

                URI = Uri.fromFile(f);

                Toast.makeText(this, URI.toString(), Toast.LENGTH_LONG).show();

            }

        }
    }





    private void signOut(){

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
    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {
       signOut();
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);

    }
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//
//        }
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                URI = FileProvider.getUriForFile(this, "com.example.km.fileprovider", photoFile);
//                Toast.makeText(this, URI.toString(), Toast.LENGTH_LONG).show();
                URI = FileProvider.getUriForFile(this,
                        "com.example.km.fileprovider",
                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, URI);
                takePictureIntent.putExtra("return-data", true);
//

//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
//        File imagePath = new File(Context.getFilesDir(), "images");
//        File newFile = new File(imagePath, "default_image.jpg");
        URI = getUriForFile(PostLogin.this, "com.example.km.fileprovider", destination);
//        URI = Uri.fromFile(destination);
        Toast.makeText(this, URI.toString(), Toast.LENGTH_LONG).show();


        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void emptyInputEditText(){
        appIDET.setText(null);
        custNameET.setText(null);
        docET.setText(null);

    }

}
