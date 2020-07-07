package android.example.chat007.info;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.chat007.R;
import android.example.chat007.data.Agencies;
import android.example.chat007.adapters.AgenciesAdapter;
import android.example.chat007.data.UTILS;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class AgenciesActivity extends AppCompatActivity {

    private RecyclerView agenciesRecyclerView;
    private GridLayoutManager agenciesLayoutManager;
    private RecyclerView.Adapter agenciesAdapter;
    private ArrayList<Agencies> agenciesArrayList;
    private int columnCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agencies);
        agenciesArrayList = new ArrayList<>();
        agenciesArrayList.add(new Agencies(UTILS.NAME1, UTILS.DESCRIPTION1, R.drawable.cia));
        agenciesArrayList.add(new Agencies(UTILS.NAME2, UTILS.DESCRIPTION2, R.drawable.mi6));
        agenciesArrayList.add(new Agencies(UTILS.NAME3, UTILS.DESCRIPTION3, R.drawable.silver));
        columnCount = getResources().getInteger(R.integer.column_count);

        buildRecyclerView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

   private void buildRecyclerView(){
        agenciesRecyclerView = findViewById(R.id.agenciesRecyclerView);
        agenciesRecyclerView.setHasFixedSize(true);
        agenciesLayoutManager = new GridLayoutManager(this, columnCount);
        agenciesAdapter = new AgenciesAdapter(agenciesArrayList, this);
        agenciesRecyclerView.setAdapter(agenciesAdapter);
        agenciesRecyclerView.setLayoutManager(agenciesLayoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
