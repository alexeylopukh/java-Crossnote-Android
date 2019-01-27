package com.crossnote.crossnote;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SignUp_Activity extends AppCompatActivity {

    private EditText emailInput, passInput, passRepeatInput;
    //private Button signUpBtn;
    private TextInputLayout emailLayout, passLayout, passRepeatLayout;
    private FirebaseAuth mAuth;
    CircularProgressButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Sign Up");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        passRepeatInput = findViewById(R.id.passRepeatInput);
        //signUpBtn = findViewById(R.id.signUpBtn);
        emailLayout = findViewById(R.id.emailLayout);
        passLayout = findViewById(R.id.passLayout);
        passRepeatLayout = findViewById(R.id.passRepeatLayout);
        btn = findViewById(R.id.btn_signUp);

        mAuth = FirebaseAuth.getInstance();

//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hideErr();
//                startSignIn();
//            }
//        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startAnimation();
                hideErr();
               startSignIn();
            }
        });
}

    private void startSignIn(){
        String email = emailInput.getText().toString();
        String pass = passInput.getText().toString();
        String passRepeat = passRepeatInput.getText().toString();
        if(!isValidEmail(email)){
            emailLayout.setError("Email is not valid");
            if(TextUtils.isEmpty(pass)){
                passLayout.setError("Field is empty");
            }
            if(TextUtils.isEmpty(passRepeat)){
                passRepeatLayout.setError("Field is empty");
            }
            btn.revertAnimation();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            passLayout.setError("Field is empty");
            if(TextUtils.isEmpty(passRepeat)){
                passRepeatLayout.setError("Field is empty");
            }
            btn.revertAnimation();
            return;
        }
        if(TextUtils.isEmpty(passRepeat)){
            passRepeatLayout.setError("Field is empty");
        }
        if(!pass.equals(passRepeat)){
            passLayout.setError("Passwords do not match");
            passRepeatLayout.setError("Passwords do not match");
            btn.revertAnimation();
            return;
        }
        if(pass.length()<6){
            passLayout.setError("The minimum password length is 6 characters");
            btn.revertAnimation();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    emailLayout.setError("Email already in use");
                    btn.revertAnimation();
                }

            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void hideErr(){
        emailLayout.setError(null);
        passLayout.setError(null);
        passRepeatLayout.setError(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
