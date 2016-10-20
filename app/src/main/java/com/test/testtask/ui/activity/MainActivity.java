package com.test.testtask.ui.activity;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.test.testtask.R;
import com.test.testtask.ui.fragments.DetailsFragment;
import com.test.testtask.ui.fragments.GridFragment;
import com.test.testtask.ui.fragments.ListFragment;
import com.test.testtask.utils.Constants;

public class MainActivity extends AppCompatActivity implements
        GridFragment.OnGridFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, ListFragment.newInstance());
        fragmentTransaction.commit();


    }

    @Override
    public void onGridItemPressed(int postId, int userId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, DetailsFragment.newInstance(postId, userId)).addToBackStack(null);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(Constants.TITLE_CONTACT + userId);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            getSupportActionBar().setTitle(R.string.app_name);
        } else {
            super.onBackPressed();
        }
    }
}
