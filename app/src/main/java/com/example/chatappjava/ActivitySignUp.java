package com.example.chatappjava;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignUp extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int GALLERY_REQUEST_CODE = 123;
    private CircularImageView edtImage;
    private EditText edtFullName;
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private Button btnSignUp;
    private Button btnSignIn;
    private TextView txvAddImage;
    private String imageUri = "content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fdefaultimage.jpg";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        edtImage = findViewById(R.id.imageProfile);
        txvAddImage = findViewById(R.id.addImage);
        edtFullName = findViewById(R.id.inputName);
        edtEmail = findViewById(R.id.inputEmail);
        edtUsername = findViewById(R.id.inputUsername);
        edtPassword = findViewById(R.id.inputPassword);
        edtConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignIn = findViewById(R.id.buttonSignIn);

        edtImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Pick an image"),GALLERY_REQUEST_CODE);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ActivitySignIn.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(edtFullName.getText()) || !edtFullName.getText().toString().matches("^[a-zA-Z\\s]+")){
                    showToast(getString(R.string.incorrectFullName));
                    return;
                } else if (TextUtils.isEmpty(edtEmail.getText()) || !edtEmail.getText().toString().matches("^[А-Яа-яA-Za-z0-9+_.-]+@(.+)$")) {
                    showToast(getString(R.string.incorrectEmail));
                    return;
                } else if (TextUtils.isEmpty(edtUsername.getText())) {
                    showToast(getString(R.string.incorrectUsername));
                    return;
                } else if (TextUtils.isEmpty(edtPassword.getText()) || TextUtils.isEmpty(edtConfirmPassword.getText()) || edtPassword.getText().length() < 8) {
                    showToast(getString(R.string.incorrectPassword));
                    return;
                } else if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                    showToast(getString(R.string.notSamePassword));
                    return;
                }

                try {
                    Connection conn = DatabaseConnection.createDatabaseConnection();
                    Statement statement = conn.createStatement();
                    EncryptPasswords ep = new EncryptPasswords();
                    String encyptedPassword = ep.encrypt(edtPassword.getText().toString());
                    encyptedPassword = encyptedPassword.replace("\n", "").replace("\r", "");


                    ResultSet resultat = statement.executeQuery("select * from USERS where USERNAME = '" + edtUsername.getText().toString() + "'");
                    if (!resultat.next()) {

                        String sql = "INSERT INTO USERS" +
                                "(USERNAME, PASSWORD, EMAIL, FULLNAME, IMAGE) " +
                                "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, edtUsername.getText().toString());
                        pstmt.setString(2, encyptedPassword);
                        pstmt.setString(3, edtEmail.getText().toString());
                        pstmt.setString(4, edtFullName.getText().toString());
                        pstmt.setString(5, imageUri);

                        int result = pstmt.executeUpdate();

                        if (result != 0) {
                            Intent intent = new Intent(getApplicationContext(), ActivitySignIn.class);
                            startActivity(intent);
                        }
                    } else {
                        showToast(getString(R.string.usernameExists));
                    }
                } catch (
                SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imagedata = data.getData();
            edtImage.setImageURI(imagedata);
            imageUri = imagedata.toString();
            txvAddImage.setVisibility(View.INVISIBLE);
        }
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



}