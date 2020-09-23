package com.appsnipp.homedesign2.Adapter;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appsnipp.homedesign2.Entity.User;
import com.appsnipp.homedesign2.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserRankingAdapter extends RecyclerView.Adapter {
    private static final String TAG = "UserAdapter";
    private List<User> userList;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public UserRankingAdapter(List<User> userList) {
        this.userList = userList;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ranking_item, parent, false);

        Log.d(TAG, "onCreateViewHolder: ");
        return new UserRankingViewHolder(itemView);

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        User user = userList.get(position);
        StorageReference profileRef = storageReference.child("images/" + user.getId().toString());
        View view;
        final ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.itemAvatar);
        final ImageView statusImage = holder.itemView.findViewById(R.id.isOnline);
        final TextView userNameView = holder.itemView.findViewById(R.id.itemUserName);
        final TextView userScore = holder.itemView.findViewById(R.id.itemScore);
        final TextView userLevel = holder.itemView.findViewById(R.id.itemLevel);
        if (!user.getStatus().equals("online")) {
            statusImage.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.miniDescription),
                    android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        userNameView.setText(user.getName());
        userScore.setText("Score: "+ user.getScore());
        userLevel.setText(Integer.toString(position + 1));

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView).load(uri).error(R.drawable.ic_user).circleCrop().thumbnail(0.1f)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
        UserRankingAdapter.UserRankingViewHolder userRankingViewHolder = new UserRankingViewHolder(holder.itemView);
        userRankingViewHolder.textView.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }


    public static class UserRankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View itemView;
        public ImageView imageView;
        public TextView textView;

        public UserRankingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            //   imageView = itemView.findViewById(R.id.itemAvatar);
            textView = itemView.findViewById(R.id.itemUserName);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }

}
