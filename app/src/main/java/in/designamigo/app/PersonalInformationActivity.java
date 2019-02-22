package in.designamigo.app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.designamigo.app.Activities.MainActivity;
import in.designamigo.app.others.UserInformation;

public class PersonalInformationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    EditText dob;
    CircleImageView circleImageView;
    Spinner gender,purpose;
    Button button;
    int mYear,mMonth,mDay;
    String mDate,mGender,mPurpose,picUrl;
    Uri imageUri;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        imageUri = null;
        dob = (EditText) findViewById(R.id.dob);
        circleImageView = (CircleImageView) findViewById(R.id.profile_pic);
        gender = (Spinner) findViewById(R.id.gender);
        purpose = (Spinner) findViewById(R.id.purpose);
        button = (Button) findViewById(R.id.submit);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference().child("profile");
        firebaseAuth = FirebaseAuth.getInstance();

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(PersonalInformationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                mDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
            }
        });


        mGender = gender.getSelectedItem().toString();
        mPurpose = purpose.getSelectedItem().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null&&dob!=null){
                    final ProgressDialog dialog = ProgressDialog.show(PersonalInformationActivity.this,"","Please Wait",true);
                    firebaseAuth.createUserWithEmailAndPassword(UserInformation.email,UserInformation.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final StorageReference profile_pic = storageReference.child(firebaseAuth.getUid()+".jpg");
                                profile_pic.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            profile_pic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                   picUrl = uri.toString();
                                                }

                                            });

                                            Map<String,String> user =  new HashMap<>();
                                            user.put("username",UserInformation.username);
                                            user.put("email",UserInformation.email);
                                            user.put("mobile",UserInformation.mobile);
                                            user.put("gender",mGender);
                                            user.put("dob",mDate);
                                            user.put("purpose",mPurpose);
                                            user.put("profile_pic",picUrl);

                                            firebaseFirestore.collection("users").document(firebaseAuth.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    Toast.makeText(PersonalInformationActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(PersonalInformationActivity.this,MainActivity.class));
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                        }
                    });
                            }
                        }
                    });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            imageUri = data.getData();
            circleImageView.setImageURI(imageUri);
        }
    }
}
