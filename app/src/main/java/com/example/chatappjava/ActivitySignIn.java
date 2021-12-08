package com.example.chatappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ActivitySignIn extends AppCompatActivity {
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignUp;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        edtUsername = findViewById(R.id.inputUsername);
        edtPassword = findViewById(R.id.inputPassword);
        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignIn = findViewById(R.id.buttonSignIn);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), ActivitySignUp.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if (TextUtils.isEmpty(edtUsername.getText())) {
                    showToast(getString(R.string.incorrectUsername));
                    return;
                } else if (TextUtils.isEmpty(edtPassword.getText())) {
                    showToast(getString(R.string.incorrectPassword));
                    return;
                }


                try {
                    Connection conn = DatabaseConnection.createDatabaseConnection();
                    Statement statement = conn.createStatement();
                    EncryptPasswords ep = new EncryptPasswords();
                    String strPasswordUser = ep.encrypt(edtPassword.getText().toString());
                    strPasswordUser = strPasswordUser.replace("\n", "").replace("\r", "");
                    ResultSet resultat = statement.executeQuery("select * from USERS where USERNAME = '" + edtUsername.getText().toString() + "' and PASSWORD = '" + strPasswordUser + "'");
                    String IdAccount = "";
                    if (resultat.next()) {
                        IdAccount = resultat.getString("ID");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("IdAccount", IdAccount);
                        startActivity(intent);

                    } else {
                        showToast(getString(R.string.noUser));
                    }
                    resultat.close();
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}