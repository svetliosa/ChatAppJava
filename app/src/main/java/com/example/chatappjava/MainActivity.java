package com.example.chatappjava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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
    private String userId;
    private int counter = 0;
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("IdAccount");
        textview = findViewById(R.id.textView);

        try {
            Connection conn = DatabaseConnection.createDatabaseConnection();
            Statement statement = conn.createStatement();

            ResultSet resultat = statement.executeQuery("select b.FULLNAME, b.IMAGE, a.USER_ID, a.FRIEND_ID from FRIENDSLIST a join USERS b on b.ID = a.FRIEND_ID where USER_ID = '" + userId + "'");
            while (resultat.next()) {
                arrayListUserData.add(new UserData(resultat.getString("FULLNAME"), resultat.getString("IMAGE"), resultat.getString("USER_ID"), resultat.getString("FRIEND_ID")));
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecycleView();
        if(counter==0) {
            textview.setText(R.string.noContacts);
        }
    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ContactsAdapter adapter = new ContactsAdapter(arrayListUserData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==R.id.contacts){
            Intent intent = new Intent(getApplicationContext(), RemoveContact.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.friendRequests){
            Intent intent = new Intent(getApplicationContext(), FriendRequests.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        if (item.getItemId()==R.id.searchFriends){
            Intent intent = new Intent(getApplicationContext(), SendFriendRequests.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}