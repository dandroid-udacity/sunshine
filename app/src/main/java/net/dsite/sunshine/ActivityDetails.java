package net.dsite.sunshine;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.dsite.sunshine.Fragments.FragmentDetails;
import net.dsite.sunshine.Fragments.FragmentMain;


public class ActivityDetails extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FragmentDetails())
                    .commit();
        }
    }

}
