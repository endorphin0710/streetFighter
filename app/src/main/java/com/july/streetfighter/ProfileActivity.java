package com.july.streetfighter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "AuthActivity";

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();
        id = getIntent().getStringExtra(getString(R.string.user_id));

        ConstraintLayout menuLogout = findViewById(R.id.menu_logout);
        ConstraintLayout menuFighter = findViewById(R.id.btn_fighter);
        ImageView backButton = findViewById(R.id.btn_back);


        menuLogout.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.menu_logout:
                requestLogout();
                break;
            case R.id.btn_fighter:

                break;
            case R.id.btn_back:
                onBackPressed();
            default:
                break;
        }
    }

    //    private void updateAuth(int auth_tobe) {
//        Log.d(TAG, "auth : " + auth_tobe);
//        Log.d(TAG, "id : " + auth_tobe);
//        /** Firebase **/
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("user").child(id).child("auth").setValue(auth_tobe);
//        /** SharedPreference **/
//        MySharedPreferences.setUserAuth(getApplicationContext(), auth_tobe);
//    }

    private void redirectLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Session.getCurrentSession().close();
                MySharedPreferences.clearUserInfo(getApplicationContext());
                redirectLoginActivity();
            }
        });
    }

}
