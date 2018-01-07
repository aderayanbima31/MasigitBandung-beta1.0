package com.aderayanbima31.masigit;

import android.app.Fragment;
import android.app.FragmentManager;

import com.aderayanbima31.masigit.MasjidFragment;
import android.support.v4.app.FragmentTransaction;
//import android.v4.appFragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Created by Bima.
 */
public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_masjid:
                    MasjidFragment masjidFragment = new MasjidFragment();
                    FragmentTransaction fragmentMasjidTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentMasjidTransaction.replace(R.id.container, masjidFragment);
                    fragmentMasjidTransaction.commit();
                    return true;

                case R.id.navigation_shalat:
                    JadwalShalatFragment jadwalShalatFragment = new JadwalShalatFragment();
                    FragmentTransaction fragmentShalatTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentShalatTransaction.replace(R.id.container, jadwalShalatFragment);
                    fragmentShalatTransaction.commit();
                    return true;

                case R.id.navigation_kiblat:
                    KiblatFragment kiblatFragment = new KiblatFragment();
                    FragmentTransaction fragmentKiblatTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentKiblatTransaction.replace(R.id.container, kiblatFragment);
                    fragmentKiblatTransaction.commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextMessage = (TextView) findViewById(R.id.message);
        MasjidFragment masjidFragment = new MasjidFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, masjidFragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
