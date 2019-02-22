package in.designamigo.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import in.designamigo.app.R;
import in.designamigo.app.others.LoggedInUser;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    String lemail, lpassword;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //LoggedInUser loggedInUser = new LoggedInUser();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }

    public void goToSignUp(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void loginUser(View view) {
        lemail = email.getText().toString().trim();
        lpassword = password.getText().toString().trim();

        if (lemail.isEmpty() || lpassword.isEmpty()) {
            Toast.makeText(this, "Please Enter the values", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(lemail, lpassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                LoggedInUser loggedInUser = new LoggedInUser();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                        finish();
                                    }
                                },1000);

                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
