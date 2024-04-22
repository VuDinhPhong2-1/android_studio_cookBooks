package com.example.doanltttbdd.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.doanltttbdd.R;
import com.example.doanltttbdd.fragment.AddFragment;
import com.example.doanltttbdd.fragment.HomeFragment;
import com.example.doanltttbdd.fragment.SearchResultFragment;
import com.example.doanltttbdd.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.home_fragment);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.menu_home) {
                    loadFragment(new HomeFragment());
                } else if (itemId == R.id.menu_user) {
                    loadFragment(new UserFragment());
                }
                else if (itemId == R.id.menu_search) {
                    loadFragment(new SearchResultFragment());
                }else {
                    loadFragment(new AddFragment());
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment, fragment);
        fragmentTransaction.commit();
    }
    public void updateBottomNavigationCheckedState(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

}