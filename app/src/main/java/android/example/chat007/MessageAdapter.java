package android.example.chat007;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter {
    public MessageAdapter(Context context, int resource, List<SecretMessage> secretMessages) {
        super(context, resource, secretMessages);
    }

    public View getView(int position, View convertView,  ViewGroup parent) {

        convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item, parent, false);

        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView textTextView = convertView.findViewById(R.id.textTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextTextView);

        SecretMessage secretMessage = (SecretMessage) getItem(position);

        boolean isText = secretMessage.getMessageUrl() == null;

        if (isText){
            textTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            textTextView.setText(secretMessage.getText());
        } else {
            textTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext()).load(secretMessage.getMessageUrl()).into(photoImageView);
        }

        nameTextView.setVisibility(View.VISIBLE);
        nameTextView.setText(secretMessage.getName());

        return convertView;
    }
}
