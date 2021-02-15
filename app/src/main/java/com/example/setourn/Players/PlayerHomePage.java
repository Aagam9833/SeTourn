package com.example.setourn.Players;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.setourn.MainActivity;
import com.example.setourn.Organizer.OrganizerHomePage;
import com.example.setourn.Organizer.OrganizerLiveTournament;
import com.example.setourn.Organizer.OrganizerPastTournament;
import com.example.setourn.Organizer.OrganizerSetTournament;
import com.example.setourn.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseUser user;
    private DrawerLayout drawerLayout;
    private static final String TAG = "PlayHomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.player_drawer_layout);
        NavigationView navigationView = findViewById(R.id.player_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_layout, new PlayerLiveTournament()).commit();
            navigationView.setCheckedItem(R.id.player_live_tournament);
        }

        final TextView greetingView = findViewById(R.id.player_greeting);

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String USERNAME = (String) snapshot.child("username").getValue();
                greetingView.setText("Hello " + USERNAME);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.player_past_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_layout,new PlayerPastTournament()).commit();
                break;
            case R.id.player_live_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.player_fragment_layout,new PlayerLiveTournament()).commit();
                break;
            case R.id.player_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to Logout?");
                builder.setTitle("Logout?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(PlayerHomePage.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(PlayerHomePage.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if(getCurrentFragment().equals("PlayerLiveTournament")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Logout?");
            builder.setTitle("Logout?");
            builder.setCancelable(false);

            builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which){
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(PlayerHomePage.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(PlayerHomePage.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else{
            super.onBackPressed();
        }
    }
    public String getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.player_fragment_layout).getClass().getSimpleName();

    }
}