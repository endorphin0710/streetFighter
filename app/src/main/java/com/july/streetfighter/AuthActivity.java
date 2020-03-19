package com.july.streetfighter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    private String id;

    private Button btn_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        id = MySharedPreferences.getUserId(getApplicationContext());

        btn_auth = findViewById(R.id.btn_auth);
        btn_auth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateAuth(1);
            }
        });
    }

    private void updateAuth(int auth_tobe){
        Log.d(TAG, "updateAuth in : " + auth_tobe);
        /** Firebase **/
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user").child(id).child("auth").setValue(auth_tobe);
        /** SharedPreference **/
        MySharedPreferences.setUserAuth(getApplicationContext(), auth_tobe);
    }
}
