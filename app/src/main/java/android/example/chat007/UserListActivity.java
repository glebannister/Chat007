package android.example.chat007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private ArrayList<Users> usersArrayList;
    private RecyclerView userRecyclerView, allCallSignsRecyclerView;
    private UserAdapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        auth = FirebaseAuth.getInstance();
        agentsReference = FirebaseDatabase.getInstance().getReference().child("Agents");


        setWelcomeTitle();

        Intent intent = getIntent();
        if (intent!= null){
            userName =  intent.getStringExtra("Name");
            setTitle("Welcome "+ intent.getStringExtra("Name"));
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

    private void setWelcomeTitle(){
        agentsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users users = dataSnapshot.getValue(Users.class);
                if (users.getId().equals(auth.getCurrentUser().getUid())){
                    setTitle("Welcome " +  users.getName());
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
                    user.setAvatarResource(R.drawable.ic_person_black_24dp);
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
        userLayoutManager = new LinearLayoutManager(this);

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
