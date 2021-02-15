package com.example.setourn.Organizer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.setourn.MainActivity;
import com.example.setourn.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrganizerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user;
    private DrawerLayout drawerLayout;
    private static final String TAG = "OrgHomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_home_page);

        Toolbar toolbar = findViewById(R.id.org_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.org_drawer_layout);
        NavigationView navigationView = findViewById(R.id.organizer_nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.organizer_fragment_layout, new OrganizerSetTournament()).commit();
            navigationView.setCheckedItem(R.id.organizer_set_tournament);
        }

        final TextView greetingView = findViewById(R.id.organizer_greeting);

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
            case R.id.organizer_set_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.organizer_fragment_layout,new OrganizerSetTournament()).commit();
                break;
            case R.id.organizer_past_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.organizer_fragment_layout,new OrganizerPastTournament()).commit();
                break;
            case R.id.organizer_live_tournament:
                getSupportFragmentManager().beginTransaction().replace(R.id.organizer_fragment_layout,new OrganizerLiveTournament()).commit();
                break;
            case R.id.organizer_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to Logout?");
                builder.setTitle("Logout?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(OrganizerHomePage.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(OrganizerHomePage.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
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
            }else if(getCurrentFragment().equals("OrganizerSetTournament")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to Logout?");
                builder.setTitle("Logout?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(OrganizerHomePage.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(OrganizerHomePage.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
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
        return getSupportFragmentManager().findFragmentById(R.id.organizer_fragment_layout).getClass().getSimpleName();
    }
}