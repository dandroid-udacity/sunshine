package net.dsite.sunshine.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.dsite.sunshine.ActivityDetails;
import net.dsite.sunshine.Helpers;
import net.dsite.sunshine.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by daniel on 7/15/14.
 */
public class FragmentMain extends Fragment {

	private View mRootView;

	private ArrayList<String> mALForecasts = new ArrayList<String>();

	private JSONObject mForecastObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.layout_fragment_main, container, false);
		new AsyncGetForecast("27616").execute();
		return mRootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment_main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == R.id.action_refresh){
			new AsyncGetForecast("27616").execute();
		}

		return super.onOptionsItemSelected(item);
	}

	private void doUpdateList(String[] forecasts){

		mALForecasts = new ArrayList<String>(Arrays.asList(forecasts));

		ArrayAdapter adapter =
			new ArrayAdapter(getActivity(), R.layout.item_list_forecast, R.id.id_text_list_forecast, mALForecasts);
		ListView listView = (ListView) mRootView.findViewById(R.id.id_list_forecasts);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(getActivity(), ActivityDetails.class).
					putExtra(Intent.EXTRA_TEXT, mALForecasts.get(i));
				startActivity(intent);
			}
		});

	}

	private class AsyncGetForecast extends AsyncTask<String, Void, String[]>{

		private Uri mTarget;

		public AsyncGetForecast(String zipCode) {


			Uri.Builder builder = new Uri.Builder().scheme("http");
			builder.authority("api.openweathermap.org");
			builder.path("data/2.5/forecast/daily");
			builder.appendQueryParameter("q", zipCode + ",USA");
			builder.appendQueryParameter("cnt","7");
			builder.appendQueryParameter("mode","json");
			builder.appendQueryParameter("units","metric");

			mTarget = builder.build();

		}

		@Override
		protected String[] doInBackground(String... strings) {

			URL url = null;
			try {
				url = new URL(mTarget.toString());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			String forecastJsonStr = null;
			// Create the request to OpenWeatherMap, and open the connection
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					// Nothing to do.
					forecastJsonStr = null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					// Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
					// But it does make debugging a *lot* easier if you print out the completed
					// buffer for debugging.
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					// Stream was empty.  No point in parsing.
					forecastJsonStr = null;
				}
				forecastJsonStr = buffer.toString();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				mForecastObject = new JSONObject(forecastJsonStr);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				return Helpers.getWeatherDataFromJson(forecastJsonStr, 7);
			} catch (JSONException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(String[] strings) {
			super.onPostExecute(strings);
			doUpdateList(strings);
		}
	}
}
