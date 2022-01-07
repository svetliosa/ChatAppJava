package com.example.chatappjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class specificchat extends AppCompatActivity {

    EditText mgetmessage;
    ImageButton msendmessagebutton;

    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    ImageView mimageviewofspecificuser;
    TextView mnameofspecificuser;

    private String enteredmessage;
    String mrecievername, recievername, recieverimage, recieverid, senderid;

    ImageButton mbackbuttonofspecificchat;

    RecyclerView mmessagerecyclerview;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);

        Intent intent = getIntent();
        senderid = intent.getStringExtra("userId");
        recieverid = intent.getStringExtra("friendId");
        recievername = intent.getStringExtra("friendName");
        recieverimage = intent.getStringExtra("friendImage");

        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.carviewofsendmessage);
        msendmessagebutton=findViewById(R.id.imageviewsendmessage);
        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser=findViewById(R.id.Nameofspecificuser);
        mimageviewofspecificuser=findViewById(R.id.specificuserimageinimageview);
        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);

        Picasso.get().load(recieverimage).into(mimageviewofspecificuser);
        mnameofspecificuser.setText(recievername);


        messagesArrayList=new ArrayList<>();
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(specificchat.this,messagesArrayList, Integer.parseInt(senderid));
        mmessagerecyclerview.setAdapter(messagesAdapter);

        setSupportActionBar(mtoolbarofspecificchat);

        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        messagesArrayList.clear();
        try {
            Connection conn = DatabaseConnection.createDatabaseConnection();
            Statement statement = conn.createStatement();

            ResultSet resultat = statement.executeQuery("select SENDER_ID, RECIEVER_ID ,MESSAGE, SHORT_TIME, FULL_TIME from MESSAGES where (SENDER_ID = " +  senderid + " and RECIEVER_ID = " +  recieverid + ") or (SENDER_ID = " +  recieverid + " and RECIEVER_ID = " +  senderid + ") order by FULL_TIME");
            while (resultat.next()) {
                messagesArrayList.add(new Messages(resultat.getString("MESSAGE"), resultat.getString("SENDER_ID"), resultat.getString("RECIEVER_ID"), resultat.getString("SHORT_TIME"), resultat.getString("FULL_TIME")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagesAdapter=new MessagesAdapter(specificchat.this,messagesArrayList,Integer.parseInt(senderid));
        messagesAdapter.notifyDataSetChanged();



        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter message first",Toast.LENGTH_SHORT).show();
                } else {
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = sdf.format(c.getTime());
                    Messages messages = new Messages(enteredmessage, senderid, recieverid, strDate.substring(11,16), strDate);
                    try {
                        Connection conn = DatabaseConnection.createDatabaseConnection();

                        String sql = "INSERT INTO MESSAGES" +
                                "(SENDER_ID, RECIEVER_ID, MESSAGE, SHORT_TIME, FULL_TIME) " +
                                "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, messages.getSenderId());
                        pstmt.setString(2, messages.getRecieverId());
                        pstmt.setString(3, messages.getMessage());
                        pstmt.setString(4, messages.getShortTime());
                        pstmt.setString(5, messages.getLongTime());

                        int result = pstmt.executeUpdate();
                        messagesArrayList.add(messages);
                        messagesAdapter.notifyDataSetChanged();
                        if (result != 0) {
                            mgetmessage.setText(null);
                            messagesAdapter.notifyDataSetChanged();
                            finish();
                            startActivity(getIntent());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStop() {
        super.onStop();
        if (messagesAdapter!=null) {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}