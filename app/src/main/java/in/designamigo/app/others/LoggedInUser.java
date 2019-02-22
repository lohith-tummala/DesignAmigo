package in.designamigo.app.others;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoggedInUser {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    String uid;
    String name,email,image;

    public LoggedInUser(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        uid = firebaseAuth.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                   name = documentSnapshot.getString("username");
                   email = documentSnapshot.getString("email");
                   String tag = documentSnapshot.getString("purpose");
                   UserInformation.username = name;
                   UserInformation.email = email;
                   UserInformation.tag = tag;
                   Log.e("tag",tag);
                }
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/"+uid+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                image = uri.toString();
                UserInformation.image_url = image;
            }
        });
    }

    public String getName(){
        Log.e("name",name);
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getImage(){
        return image;
    }


}
