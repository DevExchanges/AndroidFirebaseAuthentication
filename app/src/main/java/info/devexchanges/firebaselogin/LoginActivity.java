package info.devexchanges.firebaselogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailField;
    private TextInputLayout passwordField;
    private View btnLogin;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        emailField = (TextInputLayout) findViewById(R.id.email_field);
        passwordField = (TextInputLayout) findViewById(R.id.password_field);
        btnLogin = findViewById(R.id.login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasText(emailField)) {
                    Utils.showToast(LoginActivity.this, "Please input your email");
                } else if (!Utils.hasText(passwordField)) {
                    Utils.showToast(LoginActivity.this, "Please input your password");
                } else {
                    //requesting Firebase server
                    showProcessDialog();
                    authenticateUser(Utils.getText(emailField), Utils.getText(passwordField));
                }
            }
        });
    }

    private void authenticateUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // When login failed
                        if (!task.isSuccessful()) {
                            Utils.showToast(LoginActivity.this, "Login error!");
                        } else {
                            //When login successful, redirect user to main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                });
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Logging in Firebase server...");
        progressDialog.show();
    }
}
