package android.example.chat007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.EXTRA_LOCAL_ONLY;

public class AgentProfileActivity extends AppCompatActivity {
    private ImageButton changeAvatarImageButton;
    private TextView currentAgentName;
    private String agentName;
    private Button setAvatarButton;
    private Button requestForLocation;
    private static final int IMAGE_PICKER = 124;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference agentReference;
    private ChildEventListener agentChildEventListener;
    private FirebaseStorage avatarStorage;
    private StorageReference avatarStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        agentReference = database.getReference().child("Agents");
        avatarStorage = FirebaseStorage.getInstance();
        avatarStorageReference = avatarStorage.getReference().child("agents_avatars");

        changeAvatarImageButton = findViewById(R.id.changeAvatarImageButton);
        currentAgentName = findViewById(R.id.currentAgentName);
        setAvatarButton = findViewById(R.id.setAvatarImageButton);
        requestForLocation = findViewById(R.id.requestForLocation);

        getAgentName();

        setAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent,"Choose your image"), IMAGE_PICKER);
            }
        });

        requestForLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgentProfileActivity.this, AgentMapsActivity.class);
                intent.putExtra("Name", agentName);
                startActivity(intent);
            }
        });

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }





    private void getAgentName(){
        agentChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users agent = dataSnapshot.getValue(Users.class);
                if (agent.getId().equals(auth.getCurrentUser().getUid())){
                    agentName = agent.getName();
                    currentAgentName.setText(agentName);
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
        }; agentReference.addChildEventListener(agentChildEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER  && requestCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            final StorageReference imageReference = avatarStorageReference.child(selectedImageUri.getLastPathSegment());

        UploadTask uploadTask = imageReference.putFile(selectedImageUri);

        uploadTask = imageReference.putFile(selectedImageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Users agent = new Users();
                    agent.setAvatarResource(downloadUri.hashCode());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sing_out_menu_2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        } else if (id == R.id.signOutMenu2){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AgentProfileActivity.this, SignInActivity.class));
            return true;
        } else if (id == R.id.about2){
            startActivity(new Intent(AgentProfileActivity.this, AgenciesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
