package com.example.moviereview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Movies extends AppCompatActivity {

    private ListView moviesList;
    private ArrayList<String> moviesArrayList = new ArrayList<>();
    private ArrayAdapter<String> moviesArrayAdapter;

    private Button logoutBT;
    private DatabaseReference mDatabaseReference;

    private String[] movieListID = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        moviesList = findViewById(R.id.moviesList);
        logoutBT = findViewById(R.id.logoutBT);

        logoutBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Movies.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(Movies.this, "logged out", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        });

        showMoviesOnList();

        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String movieID = movieListID[position].toString();

                Intent intent = new Intent(Movies.this, MovieDetails.class);
                intent.putExtra("movieID", movieID);
                startActivity(intent);
            }
        });
    }

    public void showMoviesOnList(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child("Cars").child("name");
        moviesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesArrayList);
        moviesList.setAdapter(moviesArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String movieName = dataSnapshot.getValue(String.class);
                    moviesArrayList.add(movieName);
                    moviesArrayAdapter.notifyDataSetChanged();
                    movieListID[moviesArrayList.indexOf(movieName)] = "Cars";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child("Frozen").child("name");
        moviesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesArrayList);
        moviesList.setAdapter(moviesArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String movieName = dataSnapshot.getValue(String.class);
                    moviesArrayList.add(movieName);
                    moviesArrayAdapter.notifyDataSetChanged();
                    movieListID[moviesArrayList.indexOf(movieName)] = "Frozen";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child("LOTR").child("name");
        moviesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesArrayList);
        moviesList.setAdapter(moviesArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String movieName = dataSnapshot.getValue(String.class);
                    moviesArrayList.add(movieName);
                    moviesArrayAdapter.notifyDataSetChanged();
                    movieListID[moviesArrayList.indexOf(movieName)] = "LOTR";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child("TDK").child("name");
        moviesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesArrayList);
        moviesList.setAdapter(moviesArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String movieName = dataSnapshot.getValue(String.class);
                    moviesArrayList.add(movieName);
                    moviesArrayAdapter.notifyDataSetChanged();
                    movieListID[moviesArrayList.indexOf(movieName)] = "TDK";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Movies").child("Inception").child("name");
        moviesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moviesArrayList);
        moviesList.setAdapter(moviesArrayAdapter);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String movieName = dataSnapshot.getValue(String.class);
                    moviesArrayList.add(movieName);
                    moviesArrayAdapter.notifyDataSetChanged();
                    movieListID[moviesArrayList.indexOf(movieName)] = "Inception";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
