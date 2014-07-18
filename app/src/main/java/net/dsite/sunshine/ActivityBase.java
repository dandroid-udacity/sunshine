package net.dsite.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.dsite.sunshine.Fragments.FragmentMain;


public class ActivityBase extends ActionBarActivity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_activity_settings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == R.id.action_settings){
			startActivity(
				new Intent(this, ActivitySettings.class)
			);
		}

		return super.onOptionsItemSelected(item);
	}
}
