package in.designamigo.app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import in.designamigo.app.R;
import in.designamigo.app.others.LoggedInUser;
import in.designamigo.app.others.UserInformation;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView name,email;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    String uid;
    String pname,pemail,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = (CircleImageView) findViewById(R.id.profile_set_image);
        name = (TextView) findViewById(R.id.profile_name);
        email = (TextView) findViewById(R.id.profile_email);

        LoggedInUser loggedInUser = new LoggedInUser();




        getUserDetails();

    }

    public void getUserDetails(){

        //Log.e("message","Method called");
        name.setText(UserInformation.username);
        email.setText(UserInformation.email);
        Picasso.with(this).load(UserInformation.image_url).into(profileImage);
        //Log.e("Message","Method finished");
    }

    public void goToMain(View view){
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        finish();
    }
}
