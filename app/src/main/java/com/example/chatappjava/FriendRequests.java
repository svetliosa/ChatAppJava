package com.example.chatappjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.contacts){
            Intent intent = new Intent(getApplicationContext(), RemoveContact.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.searchFriends){
            Intent intent = new Intent(getApplicationContext(), SendFriendRequests.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.home){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
