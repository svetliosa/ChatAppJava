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
import java.sql.SQLException;
import java.util.ArrayList;

public class SendFriendRequestAdapter extends RecyclerView.Adapter<SendFriendRequestAdapter.ViewHolder> {

    private static final String TAG = "ContactsAdapter";

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();
    private Context context;

    public SendFriendRequestAdapter(ArrayList<UserData> arrayListUserData, Context context) {
        this.arrayListUserData = arrayListUserData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_send_friend_request_item, parent, false);
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

        holder.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Connection conn = DatabaseConnection.createDatabaseConnection();

                    PreparedStatement st1 = conn.prepareStatement(
                            " insert into FRIEND_REQUESTS values (?,?,?)");
                    st1.setString(1, arrayListUserData.get(position).friendId);
                    st1.setString(2, arrayListUserData.get(position).userId);
                    st1.setInt(3, 0);
                    st1.execute();

                    showToast(context.getResources().getString(R.string.sendFriendRequest));

                    Intent intent = new Intent(context, SendFriendRequests.class);
                    intent.putExtra("IdAccount", arrayListUserData.get(position).friendId);
                    context.startActivity(intent);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), specificchat.class);
                intent.putExtra("userId", arrayListUserData.get(position).userId);
                intent.putExtra("friendId", arrayListUserData.get(position).friendId);
                intent.putExtra("friendName", arrayListUserData.get(position).name);
                intent.putExtra("friendImage", arrayListUserData.get(position).image);
                context.startActivity(intent);
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

    public void filterList(ArrayList<UserData> filteredList) {
        arrayListUserData = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView userProfileImage;
        TextView userFullName;
        RelativeLayout parentLayout;
        Button buttonAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.imageProfile);
            userFullName = itemView.findViewById(R.id.userFullName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
        }
    }
}
