package ru.kurokkasuu.firefail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChildList extends Activity {

    DatabaseReference dbRef;
    final String CHILD ="myplace2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_list);

        Button button = (Button) findViewById(R.id.button_back);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChildList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView lctnText = (TextView)findViewById(R.id.location_text);
        TextView ltText = (TextView)findViewById(R.id.location_text);
        TextView lnText = (TextView)findViewById(R.id.location_text);


        Intent intent2 = new Intent(ChildList.this, TextAdapter.class);

        dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child(CHILD).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    intent2.putExtra("chCount",task.getResult().getChildrenCount());
                    Log.d("firebase1", String.valueOf(task.getResult().getChildrenCount()));

                }
            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Place place = snapshot.getValue(Place.class);
                for (DataSnapshot s : snapshot.getChildren()) {
                    Place p = s.getValue(Place.class);
                    /*lctnText.setText(p.name.toString());
                    lnText.setText(String.valueOf(p.lon));
                    ltText.setText(String.valueOf(p.lat));*/
                    Log.d("mytag", "place: " + p);
                }
                //Log.d("mytag", "place: " + place);

            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

