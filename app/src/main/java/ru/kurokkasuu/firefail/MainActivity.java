package ru.kurokkasuu.firefail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    //Place city = new Place("Sochi", 80, 70);
    DatabaseReference dbRef;
    final int RC_SIGN_IN = 123;
    final String CHILD = "myplace2";

    private FirebaseAuth mAuth;
    // ...
    // Initialize Firebase Auth


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициалиация Аутификации
        mAuth = FirebaseAuth.getInstance();
        //Получаем ссылку на ДБ
        dbRef = FirebaseDatabase.getInstance().getReference();
        /*
        dbRef.child(CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Place place = snapshot.getValue(Place.class);
                Log.d("mytag", "place: " + place);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        TextView lctnText = (TextView)findViewById(R.id.location_name);
        TextView ltText = (TextView)findViewById(R.id.location_lat);
        TextView lnText = (TextView)findViewById(R.id.location_lon);

        dbRef.child(CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Place place = snapshot.getValue(Place.class);
                for (DataSnapshot s:snapshot.getChildren()) {
                    Place p = s.getValue(Place.class);
                    /*lctnText.setText(p.name.toString());
                    lnText.setText(String.valueOf(p.lon));
                    ltText.setText(String.valueOf(p.lat));*/
                    Log.d("mytag", "place: " + p);
                }
                //Log.d("mytag", "place: " + place);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //dbRef.child("myplace").setValue(city);


        Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText text = (EditText)findViewById(R.id.location_text);
                String location = text.getText().toString();
                Place city2 = new Place(location, 80, 70);
                dbRef.child("myplace2").push().setValue(city2);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Локация введена", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        Button buttonChildren = (Button) findViewById(R.id.button_child);
        buttonChildren.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChildList.class);
                startActivity(intent);
                Toast toast2 = Toast.makeText(getApplicationContext(),
                        "Смена активити", Toast.LENGTH_SHORT);
                toast2.show();
            }
        });


    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Place place = snapshot.getValue(Place.class);

        Log.d("mytag", "place: " + place);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    public void signIn(View v){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    public void signOut(View v){
        FirebaseAuth.getInstance().signOut();
    }


}

