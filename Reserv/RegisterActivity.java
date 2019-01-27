package com.example.lopuk.registr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button btn_reg;
    EditText mailText;
    EditText fpassText;
    EditText spassText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btn_reg = (Button) findViewById(R.id.btn_reg);
        mailText = (EditText) findViewById(R.id.mailText);
        fpassText = (EditText) findViewById(R.id.fpassText);
        spassText = (EditText) findViewById(R.id.spassText);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fpassText.getText().toString().equals(spassText.getText().toString())){
                    if (!isValidEmail(mailText.getText().toString())){
                        Toast.makeText(RegisterActivity.this, "No valid email", Toast.LENGTH_LONG).show();
                        return;
                    }
                    startSignIn();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void startSignIn(){

        String email = mailText.getText().toString();
        String password = spassText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Sign In problem!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
