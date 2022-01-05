package com.example.chatappjava;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.parse.Parse.getApplicationContext;

public class ContactsRemoveAdapter extends RecyclerView.Adapter<ContactsRemoveAdapter.ViewHolder> {

    private static final String TAG = "ContactsAdapter";

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();
    private Context context;

    public ContactsRemoveAdapter(ArrayList<UserData> arrayListUserData, Context context) {
        this.arrayListUserData = arrayListUserData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contacts_list_remove_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(context)
                .asBitmap()
                .load(arrayListUserData.get(position).image)
                .into(holder.userProfileImage);

        holder.userFullName.setText(arrayListUserData.get(position).name);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Connection conn = DatabaseConnection.createDatabaseConnection();

                    PreparedStatement st1 = conn.prepareStatement(
                            "Delete from FRIENDSLIST where USER_ID = ? and FRIEND_ID = ?");
                    st1.setString(1, arrayListUserData.get(position).userId);
                    st1.setString(2, arrayListUserData.get(position).friendId);
                    st1.execute();

                    PreparedStatement st2 = conn.prepareStatement(
                            "Delete from FRIENDSLIST where USER_ID = ? and FRIEND_ID = ?");
                    st2.setString(1, arrayListUserData.get(position).friendId);
                    st2.setString(2, arrayListUserData.get(position).userId);
                    st2.execute();

                    PreparedStatement st3 = conn.prepareStatement(
                            "Delete from FRIEND_REQUESTS where SENDER_ID = ? and RECEIVER_ID = ?");
                    st3.setString(1, arrayListUserData.get(position).userId);
                    st3.setString(2, arrayListUserData.get(position).friendId);
                    st3.execute();

                    PreparedStatement st4 = conn.prepareStatement(
                            "Delete from FRIEND_REQUESTS where SENDER_ID = ? and RECEIVER_ID = ?");
                    st4.setString(1, arrayListUserData.get(position).friendId);
                    st4.setString(2, arrayListUserData.get(position).userId);
                    st4.execute();

                    showToast(context.getResources().getString(R.string.removedUser));

                    Intent intent = new Intent(context, RemoveContact.class);
                    intent.putExtra("IdAccount", arrayListUserData.get(position).userId);
                    context.startActivity(intent);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return arrayListUserData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView userProfileImage;
        TextView userFullName;
        RelativeLayout parentLayout;
        Button removeButton;

        public ViewHolder(View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.imageProfile);
            userFullName = itemView.findViewById(R.id.userFullName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            removeButton = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
