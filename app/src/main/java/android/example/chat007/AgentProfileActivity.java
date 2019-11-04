package android.example.chat007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgentProfileActivity extends AppCompatActivity {
    private ImageButton changeAvatarImageButton;
    private TextView currentAgentName;
    private String agentName;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference agentReference;
    private ChildEventListener agentChildEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        agentReference = database.getReference().child("Agents");

        changeAvatarImageButton = findViewById(R.id.changeAvatarImageButton);
        currentAgentName = findViewById(R.id.currentAgentName);

        getAgentName();
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
}
