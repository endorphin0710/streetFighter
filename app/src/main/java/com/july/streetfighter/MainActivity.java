package com.july.streetfighter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import net.daum.mf.map.api.MapView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btn_auth;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /** Action Bar Hide **/
        getSupportActionBar().hide();

        id = MySharedPreferences.getUserId(getApplicationContext());

        btn_auth = findViewById(R.id.fighter_auth);
        btn_auth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(MySharedPreferences.getUserAuth(getApplicationContext()) == 0){
                    redirectAuthAcitivity();
                }else{
                    /** Firebase **/
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    ref.child("user").child(id).child("auth").setValue(0);
                    /** SharedPreference **/
                    MySharedPreferences.setUserAuth(getApplicationContext(), 0);
                }
            }
        });


        /** KakaoMap **/
        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }

    private void redirectLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectAuthAcitivity(){
        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
        startActivity(intent);
    }

    private void requestLogout(){
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Session.getCurrentSession().close();
                redirectLoginActivity();
            }
        });
    }

}
