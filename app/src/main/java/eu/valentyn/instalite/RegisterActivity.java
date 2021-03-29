package eu.valentyn.instalite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import eu.valentyn.instalite.Model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button register;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        loginUser = findViewById(R.id.login_user);

        this.db = FirebaseFirestore.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        loginUser.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this , LogInActivity.class)));

        register.setOnClickListener(v -> {
            String txtUsername = username.getText().toString();
            String txtName = name.getText().toString();
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                    || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                Toast.makeText(RegisterActivity.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
            } else if (txtPassword.length() < 6){
                Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(txtUsername , txtName , txtEmail , txtPassword);
            }
        });
    }

    private void registerUser(final String username, final String name, final String email, String password) {

        pd.setMessage("Please Wait!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
/*
                User newUser = new User();
                newUser.setName(name);
                newUser.setEmail(email);
                newUser.setUsername(username);
                newUser.setId(mAuth.getCurrentUser().getUid());
                newUser.setBio("");
                newUser.setImageurl("default");

                db.collection("Users")
                        .add(newUser)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(null,"DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(null, "Error adding document", e);
                    }
                });
  */


                HashMap<String , Object> map = new HashMap<>();
                map.put("name" , name);
                map.put("email", email);
                map.put("username" , username);
                map.put("id" , mAuth.getCurrentUser().getUid());
                map.put("bio" , "");
                map.put("imageurl" , "default");

                // Add a new document with a generated ID

                db.collection("Users")
                        .add(map)
                        .addOnSuccessListener(documentReference -> Log.d(null, "DocumentSnapshot added with ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w(null, "Error adding document", e));


                pd.dismiss();
                Toast.makeText(RegisterActivity.this, "Update the profile " +
                        "for better expereince", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Update the profile " +
                                    "for better expereince", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });



            }
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }
}
