package com.example.chatappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("IdAccount");

        try {
            Connection conn = DatabaseConnection.createDatabaseConnection();
            Statement statement = conn.createStatement();

            ResultSet resultat = statement.executeQuery("select b.FULLNAME, b.IMAGE, a.USER_ID, a.FRIEND_ID from FRIENDSLIST a join USERS b on b.ID = a.FRIEND_ID where USER_ID = '" + str + "'");
            while (resultat.next()) {
                arrayListUserData.add(new UserData(resultat.getString("FULLNAME"), resultat.getString("IMAGE"), resultat.getString("USER_ID"), resultat.getString("FRIEND_ID")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecycleView();
    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ContactsAdapter adapter = new ContactsAdapter(arrayListUserData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}