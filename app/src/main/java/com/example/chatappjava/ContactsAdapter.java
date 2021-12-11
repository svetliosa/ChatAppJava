package com.example.chatappjava;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private static final String TAG = "ContactsAdapter";

    private ArrayList<UserData> arrayListUserData = new ArrayList<>();
    private Context context;

    public ContactsAdapter(ArrayList<UserData> arrayListUserData, Context context) {
        this.arrayListUserData = arrayListUserData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_contacts_list_item, parent, false);
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

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: clicked on:" + arrayListUserData.get(position));
                //Toast.makeText(context, arrayListUserData.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context.getApplicationContext(), ActivityChat.class);
                intent.putExtra("userId", arrayListUserData.get(position).userId);
                intent.putExtra("friendId", arrayListUserData.get(position).friendId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListUserData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView userProfileImage;
        TextView userFullName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.imageProfile);
            userFullName = itemView.findViewById(R.id.userFullName);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
