package com.example.chatappjava;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SendFriendRequests extends AppCompatActivity {

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();
    private Button searchButton;
    private EditText searchUsername;
    private String userId;
    private SendFriendRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userId = intent.getStringExtra("IdAccount");
        setContentView(R.layout.activity_send_friend_request);
        searchUsername = findViewById(R.id.searchUsername);

        try {
            Connection conn = DatabaseConnection.createDatabaseConnection();
            Statement statement = conn.createStatement();

            ResultSet resultat = statement.executeQuery("select ID, FULLNAME, IMAGE from USERS where ID not in (select FRIEND_ID from FRIENDSLIST where USER_ID = " + userId  + ") and FULLNAME like '%" + searchUsername.getText().toString() + "%' and ID not in (select RECEIVER_ID from FRIEND_REQUESTS where STATUS <> 3 and SENDER_ID = " + userId  + ") and ID <>" + userId);
            while (resultat.next()) {
                arrayListUserData.add(new UserData(resultat.getString("FULLNAME"), resultat.getString("IMAGE"), resultat.getString("ID"), userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initRecycleView();

        searchUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


    }

    private void filter(String text) {
        ArrayList<UserData> filteredList = new ArrayList<>();

        for(UserData item : arrayListUserData) {
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }

        }
        adapter.filterList(filteredList);
    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new SendFriendRequestAdapter(arrayListUserData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);
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

        if (item.getItemId()==R.id.home){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("IdAccount", userId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
