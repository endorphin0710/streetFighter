package com.july.streetfighter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private SessionCallback callback;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, " login activity on create id : " + MySharedPreferences.getUserId(getApplicationContext()));
        if (MySharedPreferences.getUserId(getApplicationContext()).equals(getString(R.string.non_member))) {
            id = MySharedPreferences.getUserId(getApplicationContext());
            redirectMainActivity();
        }

        Button loginNonMember = findViewById(R.id.btn_nonmember);
        loginNonMember.setOnClickListener(this);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        /** Action Bar Hide **/
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nonmember:
                id = getString(R.string.non_member);
                MySharedPreferences.setUserId(getApplicationContext(), id);
                redirectMainActivity();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened: ");
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d(TAG, "onSessionOpenFailed: ");
            if (exception != null) {
                Log.d(TAG, "exception : " + exception);
            }
        }
    }

    protected void redirectMainActivity() {
        Log.d(TAG, "redirectMainActivity: ");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.user_id), id);
        startActivity(intent);
        finish();
    }

    private void requestMe() {
        Log.d(TAG, "requestMe: ");
        List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d(TAG, "failed to get user info. msg=" + errorResult);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Log.d(TAG, "reuqestMe -> onSuccess: ");
                id = Long.toString(response.getId());
                readUser();
            }
        });
    }

    private void readUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) {
                    saveUser();
                }
                MySharedPreferences.setUserId(getApplicationContext(), id);
                redirectMainActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveUser() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("user").child(id).setValue(new User(id, "A", 0));
    }

}
