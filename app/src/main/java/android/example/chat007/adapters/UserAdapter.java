package android.example.chat007.adapters;

import android.example.chat007.R;
import android.example.chat007.data.Users;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private ArrayList<Users> usersArrayList;
    private OnUserClickListener listener;

    public interface OnUserClickListener{
        void onUserClick(int position);
    }

    public void setOnClickUserListenr(OnUserClickListener listener){
        this.listener = listener;
    }

    public UserAdapter(ArrayList<Users> usersArrayList){
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.agent_item1, viewGroup
        ,false);
        UserViewHolder userViewHolder = new UserViewHolder(view, listener);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        Users currentUser = usersArrayList.get(position);
        userViewHolder.avatarImageView.setImageResource(R.drawable.ic_person_black_24dp);
        userViewHolder.nameTextViewItem.setText(currentUser.getName());
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public ImageButton avatarImageView;
        public TextView nameTextViewItem;

        public UserViewHolder(@NonNull View itemView, final OnUserClickListener listener) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextViewItem = itemView.findViewById(R.id.nameTextViewItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }

}
