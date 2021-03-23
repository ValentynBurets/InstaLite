package eu.valentyn.instalite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private EditText registerUser;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        /*
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerUser = findViewById(R.id.register_user);

        mAuth = FindbaseAuth.getInstance();

        registerUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
        */

    }
}