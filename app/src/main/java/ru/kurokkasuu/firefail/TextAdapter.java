package ru.kurokkasuu.firefail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TextAdapter extends BaseAdapter {
    Context ctx;
    DatabaseReference dbRef;
    public TextAdapter(Context ctx, int hits) {
        this.ctx = ctx;
        dbRef = FirebaseDatabase.getInstance().getReference();
        //hits = dbRef.child("myplace2").getCh; //Дописать получение кол-ва записей
    }
    int cnt = Intent.getIntent().getExtras("chCount");

    @Override
    public int getCount() {
        int cnt = Intent();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(ctx).inflate(R.layout.items, parent, false);

        TextView lctnText = convertView.findViewById(R.id.location_name);
        TextView ltText = convertView.findViewById(R.id.location_lat);
        TextView lnText = convertView.findViewById(R.id.location_lon);
/*
        ImageView iv = convertView.findViewById(R.id.picture);
        TextView tvid = convertView.findViewById(R.id.id);

        tvid.setText("id: "+hits);

        Place place = snapshot.getValue(Place.class);
        for (DataSnapshot s:snapshot.getChildren()) {
            Place p = s.getValue(Place.class);
                    *//*lctnText.setText(p.name.toString());
                    lnText.setText(String.valueOf(p.lon));
                    ltText.setText(String.valueOf(p.lat));*//*
            Log.d("mytag", "place: " + p);
        }*/

        return convertView;
    }
}
