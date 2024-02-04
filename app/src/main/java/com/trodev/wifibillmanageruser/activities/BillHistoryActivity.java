package com.trodev.wifibillmanageruser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trodev.wifibillmanageruser.adapters.BillAdapter;
import com.trodev.wifibillmanageruser.R;
import com.trodev.wifibillmanageruser.models.BillModels;

import java.util.ArrayList;

public class BillHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference reference;
    ArrayList<BillModels> list;
    BillAdapter adapter;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);

        getSupportActionBar().setTitle("Bill History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reference = FirebaseDatabase.getInstance().getReference("user_bill");

        /*init views*/
        recyclerView = findViewById(R.id.productRv);
        animationView = findViewById(R.id.animationView);
        animationView.loop(true);

        /*create methods*/
        load_data();
    }

    private void load_data() {

        Query query = reference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                if (!dataSnapshot.exists()) {

                    animationView.setVisibility(View.VISIBLE);
                    Toast.makeText(BillHistoryActivity.this, "no data found", Toast.LENGTH_SHORT).show();

                } else {

                    recyclerView.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        BillModels data = snapshot.getValue(BillModels.class);
                        list.add(0, data);

                    }
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BillHistoryActivity.this));
                    adapter = new BillAdapter(BillHistoryActivity.this, list);
                    recyclerView.setAdapter(adapter);
                    Toast.makeText(BillHistoryActivity.this, "bill history found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}