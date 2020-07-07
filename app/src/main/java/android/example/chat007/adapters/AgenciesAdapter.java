package android.example.chat007.adapters;

import android.content.Context;
import android.content.Intent;
import android.example.chat007.info.InfoActivity;
import android.example.chat007.R;
import android.example.chat007.data.Agencies;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AgenciesAdapter extends RecyclerView.Adapter<AgenciesAdapter.AgenciesViewHolder> {

    private ArrayList<Agencies> agenciesArrayList;
    Context context;

    public AgenciesAdapter(ArrayList<Agencies> agenciesArrayList, Context context) {
       this.agenciesArrayList = agenciesArrayList;
       this.context = context;
    }

    @NonNull
    @Override
    public AgenciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agencies_item, parent, false);
        AgenciesViewHolder agenciesViewHolder = new AgenciesViewHolder(view);
        return agenciesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AgenciesViewHolder holder, int position) {
        Agencies agencies = agenciesArrayList.get(position);
        holder.agenciesImageView.setImageResource(agencies.getImageResource());
        holder.agenciesTextView.setText(agencies.getName());
    }

    @Override
    public int getItemCount() {
        return agenciesArrayList.size();
    }

    public class AgenciesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView agenciesImageView;
        private TextView agenciesTextView;

        public AgenciesViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            agenciesImageView = itemView.findViewById(R.id.agenciesImageView);
            agenciesTextView = itemView.findViewById(R.id.agenciesTextView);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Agencies agencies = agenciesArrayList.get(position);
            Intent intent2 = new Intent(context, InfoActivity.class);
            intent2.putExtra("Name", agencies.getName());
            intent2.putExtra("descritpion", agencies.getDescription());
            context.startActivity(intent2);
        }
    }
}
