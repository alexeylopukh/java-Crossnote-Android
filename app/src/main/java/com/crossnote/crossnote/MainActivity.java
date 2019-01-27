package com.crossnote.crossnote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonFlat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class MainActivity extends AppCompatActivity {

    EditText emailInput, passInput;
    Button loginBtn;
    ButtonFlat signUpBtn;
    TextInputLayout emailLayout, passLayout;
    CircularProgressButton btn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Sign In");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        signUpBtn = findViewById(R.id.signUpBtn);
        emailLayout = findViewById(R.id.emailLayout);
        passLayout = findViewById(R.id.passLayout);
        btn = findViewById(R.id.btn_id);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, NoteActivity.class));
                }
            }
        };

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUp_Activity.class));
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startAnimation();
                hideErr();
                String email = emailInput.getText().toString();
                String pass = passInput.getText().toString();
                if(!isValidEmail(email)){
                    emailLayout.setError("Email is not valid");
                    if(TextUtils.isEmpty(pass)){
                        passLayout.setError("Field is empty");
                    }
                    btn.revertAnimation();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    passLayout.setError("Field is empty");
                    btn.revertAnimation();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else{
                            @SuppressLint("ResourceType") Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.SignInLayout), "Sign In problem", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            btn.revertAnimation();
                        }
                    }
                });
            }
        });
    }

    private void hideErr(){
        emailLayout.setError(null);
        passLayout.setError(null);
    }

    public boolean isValidEmail(CharSequence target) {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
    }


}
