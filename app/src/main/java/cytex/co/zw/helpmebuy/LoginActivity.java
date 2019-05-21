package cytex.co.zw.helpmebuy;

import android.content.Intent;
import android.nfc.tech.NfcV;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import cytex.co.zw.helpmebuy.util.MessageToast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText email,password;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NavMain.class));
            finish();
        }
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogin=(Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temail=email.getText().toString().trim();
               final String tpassword=password.getText().toString().trim();

                if(!TextUtils.isEmpty(temail)){
                    if(!TextUtils.isEmpty(tpassword)){
                        progressBar.setVisibility(View.VISIBLE);

                        //authenticate user
                        auth.signInWithEmailAndPassword(temail, tpassword)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        progressBar.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            // there was an error
                                            if (tpassword.length() < 6) {
                                                password.setError("Password too short");
                                            } else {
                                                MessageToast.show(LoginActivity.this,"Authentiction failed, please try differently");
                                                //startActivity(new Intent(LoginActivity.this,NavMain.class));
                                            }
                                        } else {
                                            MessageToast.show(LoginActivity.this,"Welcome to OK App");
                                            Intent intent = new Intent(LoginActivity.this, NavMain.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else{
                        password.setFocusable(true);
                        password.setError("Password is required");
                        MessageToast.show(LoginActivity.this,"Password is required");
                    }
                }
                else{
                    email.setFocusable(true);
                    email.setError("Email is required");
                    MessageToast.show(LoginActivity.this,"Email is required");
                }



                //startActivity(new Intent(LoginActivity.this,NavMain.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
