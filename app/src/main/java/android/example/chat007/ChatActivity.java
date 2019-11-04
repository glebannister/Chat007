package android.example.chat007;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final int CONSTANT = 123;

    private ListView messageListView;
    private MessageAdapter adapter;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText editText;
    private String userName;
    private String recipientUserId;
    private TextView nameWithTextview;
    private String recipientUserName;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference messagesReference, agentsReference;
    private ChildEventListener messagesChildEventListener, agentsChildEventListener;
    private FirebaseStorage storage;
    private StorageReference secretImagesStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        database = FirebaseDatabase.getInstance();
        messagesReference = database.getReference().child("secretMessages");
        agentsReference = database.getReference().child("Agents");
        messageListView = findViewById(R.id.listView);
        final List<SecretMessage> secretMessages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.message_item, secretMessages);
        messageListView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        secretImagesStorageReference = storage.getReference().child("secret_images");

        sendImageButton = findViewById(R.id.sendImageButton);
        sendMessageButton = findViewById(R.id.sendButton);
        editText = findViewById(R.id.sendEditText);
        nameWithTextview = findViewById(R.id.text01);


        agentsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Users users = dataSnapshot.getValue(Users.class);
                if (users.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
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


        Intent intent = getIntent();
        if (intent!= null){
            userName =  intent.getStringExtra("Name");
            recipientUserId = intent.getStringExtra("recipientUserId");
            recipientUserName = intent.getStringExtra("recipientUserName");
        }

        nameWithTextview.setText("Communication with " + recipientUserName);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() > 0){
                    sendMessageButton.setEnabled(true);
                } else {
                    sendMessageButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Choose a secret image"),
                        CONSTANT);
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecretMessage secretMessage = new SecretMessage();
                secretMessage.setText(editText.getText().toString());
                secretMessage.setName(userName);
                secretMessage.setSender(auth.getCurrentUser().getUid());
                secretMessage.setRecipient(recipientUserId);
                secretMessage.setMessageUrl(null);

                messagesReference.push().setValue(secretMessage);

                editText.setText("");

            }
        });

        messagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SecretMessage secretMessage = dataSnapshot.getValue(SecretMessage.class);

                if
                (secretMessage.getSender()!= null && secretMessage.getSender().equals(auth.getCurrentUser().getUid())&&
                secretMessage.getRecipient().equals(recipientUserId) || secretMessage.getRecipient()!= null
                        && secretMessage.getRecipient().equals(auth.getCurrentUser().getUid()) &&
                        secretMessage.getSender().equals(recipientUserId)){
                    adapter.add(secretMessage);
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
        }; messagesReference.addChildEventListener(messagesChildEventListener);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONSTANT && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            final StorageReference imageReference =
                    secretImagesStorageReference.child(selectedImageUri.getLastPathSegment());

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
                        Uri selectedImageUri = task.getResult();
                        SecretMessage secretMessage = new SecretMessage();
                        secretMessage.setName(userName);
                        secretMessage.setSender(auth.getCurrentUser().getUid());
                        secretMessage.setRecipient(recipientUserId);
                        secretMessage.setMessageUrl(selectedImageUri.toString());
                        messagesReference.push().setValue(secretMessage);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }


}
