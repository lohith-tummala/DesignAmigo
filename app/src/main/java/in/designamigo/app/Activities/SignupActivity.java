package in.designamigo.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import in.designamigo.app.PersonalInformationActivity;
import in.designamigo.app.R;
import in.designamigo.app.others.UserInformation;

public class SignupActivity extends AppCompatActivity {

    EditText username,email,mobile,password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String susername,semail,smobile,spassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void goToLogin(View view){
        startActivity(new Intent(this,LoginActivity.class));
    }


    public void registerUser(View view){
        susername = username.getText().toString().trim();
        semail = email.getText().toString().trim();
        smobile = mobile.getText().toString().trim();
        spassword = password.getText().toString().trim();


        if(TextUtils.isEmpty(susername)||TextUtils.isEmpty(semail)||TextUtils.isEmpty(smobile)||TextUtils.isEmpty(spassword)){
            Toast.makeText(this, "Please make sure that you entered all credentials", Toast.LENGTH_SHORT).show();
        }

        else{
            UserInformation.username = susername;
            UserInformation.email = semail;
            UserInformation.mobile = smobile;
            UserInformation.password = spassword;

            startActivity(new Intent(SignupActivity.this,PersonalInformationActivity.class));



        }



    }
}
