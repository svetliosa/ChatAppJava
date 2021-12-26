package com.example.chatappjava;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FriendRequests extends AppCompatActivity {

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("IdAccount");

        try {
            Connection conn = DatabaseConnection.createDatabaseConnection();
            Statement statement = conn.createStatement();

            ResultSet resultat = statement.executeQuery(" select a.RECEIVER_ID, b.FULLNAME, b.IMAGE, a.SENDER_ID from FRIEND_REQUESTS a join USERS b on b.ID = a.SENDER_ID where RECEIVER_ID = " + userId + " and STATUS = 0");
            while (resultat.next()) {
                arrayListUserData.add(new UserData(resultat.getString("FULLNAME"), resultat.getString("IMAGE"), resultat.getString("RECEIVER_ID"), resultat.getString("SENDER_ID")));
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
        FriendRequestsAdapter adapter = new FriendRequestsAdapter(arrayListUserData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
