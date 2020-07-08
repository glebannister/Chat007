package android.example.chat007.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.chat007.R;
import android.example.chat007.auth.SignInActivity;
import android.example.chat007.data.Agencies;
import android.example.chat007.adapters.UserAdapter;
import android.example.chat007.data.Users;
import android.example.chat007.info.AgenciesActivity;
import android.example.chat007.profile.AgentProfileActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {



    public class UserProfilesTask extends AsyncTask <DatabaseReference, Void, String>{

        @Override
        protected String doInBackground(DatabaseReference... databaseReferences) {
            attachUserDatabaseReferenceListener();
            return userName;
        }
    }

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference agentsReference;
    private ChildEventListener agentsChildEventListener;
    private FirebaseStorage avatarStorage;
    private StorageReference avatarStorageReference;

    private ArrayList<Users> usersArrayList;
    private RecyclerView userRecyclerView, allCallSignsRecyclerView;
    private UserAdapter userAdapter;
    private GridLayoutManager userLayoutManager;
    private String userName;
    private TextView nameTextView;
    private int columnCount;

    private ArrayList<Agencies> agenciesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        auth = FirebaseAuth.getInstance();
        agentsReference = FirebaseDatabase.getInstance().getReference().child("Agents");
        avatarStorage = FirebaseStorage.getInstance();
        avatarStorageReference = avatarStorage.getReference().child("agents_avatars");

        columnCount = getResources().getInteger(R.integer.column_count);

        nameTextView = findViewById(R.id.text01);

        setAgentName();

        Intent intent = getIntent();
        if (intent!= null){
            userName =  intent.getStringExtra("Name");
            nameTextView.setText("Welcome "+ userName);
        }

        usersArrayList = new ArrayList<>();
        new UserProfilesTask().execute();
        buildRecyclerView();
        userAdapter.setOnClickUserListenr(new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int position) {
                goToChat(position);
            }
        });
    }

    private void goToChat(int position) {
        Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
        intent.putExtra("recipientUserId", usersArrayList.get(position).getId());
        intent.putExtra("recipientUserName", usersArrayList.get(position).getName());
        intent.putExtra("Name", userName);
        startActivity(intent);
    }

    private void setAgentName(){
        agentsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users users = dataSnapshot.getValue(Users.class);
                assert users != null;
                if (users.getId().equals(auth.getCurrentUser().getUid())){
                    nameTextView.setText("Welcome " +  users.getName());
                    userName = users.getName();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; agentsReference.addChildEventListener(agentsChildEventListener);
    }

    private void attachUserDatabaseReferenceListener() {

        agentsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users user = dataSnapshot.getValue(Users.class);

                if (!user.getId().equals(auth.getCurrentUser().getUid())){
                    user.getAvatarResource();
                    usersArrayList.add(user);
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }; agentsReference.addChildEventListener(agentsChildEventListener);
    }

    private void buildRecyclerView() {
        userRecyclerView = findViewById(R.id.recyclerView1);
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new GridLayoutManager(this, columnCount);

        userAdapter = new UserAdapter(usersArrayList);
        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sing_out_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signOutMenu:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserListActivity.this, SignInActivity.class));
                return true;
            case  R.id.agentProfile:
                startActivity(new Intent(UserListActivity.this, AgentProfileActivity.class));
                return true;
                case R.id.about:
                    startActivity(new Intent(UserListActivity.this, AgenciesActivity.class));
                   default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToAllChat(View view) {
        Intent intent = new Intent(UserListActivity.this, AllChatActivity.class);
        intent.putExtra("Name", userName);
        startActivity(intent);
    }
}
