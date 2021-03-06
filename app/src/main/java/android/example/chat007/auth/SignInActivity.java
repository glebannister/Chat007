package android.example.chat007.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.chat007.R;
import android.example.chat007.chat.UserListActivity;
import android.example.chat007.data.Users;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nameEditText;
    private boolean isLogInActive;
    private boolean isBondActive;
    private Button singInButton;
    private TextView toggleTextView;

    private DatabaseReference agentsReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        agentsReference = database.getReference().child("Agents");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        singInButton = findViewById(R.id.singInButton);
        toggleTextView = findViewById(R.id.toggleTextView);

        singInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
            //intent.putExtra("Name", nameEditText.getText().toString().trim());
            startActivity(intent);
        }

    }

    private void singUpUser(String email, String password) {
        if (isLogInActive)
            if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password must be numbers 6 or more, agent", Toast.LENGTH_LONG).show();
            } else if (passwordEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Input password, agent", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().equals("")) {
                Toast.makeText(this, "Input email, agent", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        else if (!confirmPasswordEditText.getText().toString().trim().equals(passwordEditText.getText().toString().trim())) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        } else if (passwordEditText.getText().toString().trim().length() < 6) {
            Toast.makeText(this, "Password must be 6 numbers or more, agent", Toast.LENGTH_LONG).show();
        } else if (passwordEditText.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Input password, agent", Toast.LENGTH_SHORT).show();
        } else if (emailEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Input email, agent", Toast.LENGTH_SHORT).show();
        } else if (nameEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Input your Name, agent", Toast.LENGTH_SHORT).show();
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                createUser(user);
                                Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                intent.putExtra("Name", nameEditText.getText().toString().trim());
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
    }


    private void createUser(FirebaseUser firebaseUser) {
        Users users = new Users();
        users.setId(firebaseUser.getUid());
        users.setName(nameEditText.getText().toString().trim());
        users.setEmail(firebaseUser.getEmail());

        agentsReference.push().setValue(users);
    }

    public void changeSingInUp(View view) {
        if (isLogInActive) {
            isLogInActive = false;
            singInButton.setText(R.string.Sign_Up);
            toggleTextView.setText(R.string.Click);
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            nameEditText.setVisibility(View.VISIBLE);
        } else {
            isLogInActive = true;
            singInButton.setText(R.string.Sign_In);
            toggleTextView.setText(R.string.ClickUp);
            confirmPasswordEditText.setVisibility(View.GONE);
            nameEditText.setVisibility(View.GONE);
        }
    }

    public void changeAgent(View view) {
        if (isBondActive) {
            ImageView bondImageView = findViewById(R.id.singInImageView);
            bondImageView.animate().alpha(0).setDuration(2000);

            ImageView cavilImageView = findViewById(R.id.singInImageView2);
            cavilImageView.animate().alpha(1).setDuration(2000);

            isBondActive = false;
        } else {
            ImageView bondImageView = findViewById(R.id.singInImageView);
            bondImageView.animate().alpha(1).setDuration(2000);

            ImageView cavilImageView = findViewById(R.id.singInImageView2);
            cavilImageView.animate().alpha(0).setDuration(2000);
            isBondActive = true;
        }
    }
}
