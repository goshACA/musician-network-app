package com.musapp.musicapp.adapters.viewholders;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.musapp.musicapp.R;
import com.musapp.musicapp.utils.StringUtils;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView descriptionText;
    private TextView bodyText;
    private TextView date;
    private ImageView userImage;
    private ImageOnClickListener imageOnClickListener;
    private ItemClickPerformance itemClickPerformance;

    private View.OnClickListener  clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageOnClickListener.onImagePerformClick(getAdapterPosition());
        }
    };

    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemClickPerformance.onItemClickPerformance(getAdapterPosition());
        }
    };
    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        descriptionText = itemView.findViewById(R.id.text_item_user_notification_description);
        bodyText = itemView.findViewById(R.id.text_item_user_notification_body);
        date = itemView.findViewById(R.id.text_item_user_notification_date);
        userImage = itemView.findViewById(R.id.image_item_user_notification_others_pic);
        userImage.setOnClickListener(clickListener);
        itemView.setOnClickListener(itemClickListener);

    }

    public void setDescriptionText(String text){
        descriptionText.setText(text);
    }

    public String getDescriptionText(){
        return descriptionText.getText().toString();
    }



    public void setUserImage(int  source) {
        userImage.setImageResource(source);
    }

    public ImageView getUserImage(){ return userImage;}

    public String getBodyText() {
        return bodyText.getText().toString();
    }

    public void setBodyText(String bodyText) {
        this.bodyText.setText(bodyText);
    }

    public String getDate() {
        return date.getText().toString();
    }

    public void setDate(long date) {
        this.date.setText(StringUtils.millisecondsToDateString(date));
    }

    public void setImageOnClickListener(ImageOnClickListener imageOnClickListener) {
        this.imageOnClickListener = imageOnClickListener;
    }

    public void setItemClickPerformance(ItemClickPerformance itemClickPerformance) {
        this.itemClickPerformance = itemClickPerformance;
    }

    public interface ImageOnClickListener{
        void onImagePerformClick(int position);
    }

    public interface ItemClickPerformance{
        void onItemClickPerformance(int position);
    }
}
