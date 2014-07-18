package net.dsite.sunshine.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.dsite.sunshine.R;

/**
 * Created by daniel on 7/15/14.
 */
public class FragmentDetails extends Fragment {

	private View mRootView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.layout_fragment_details, container, false);

		Bundle extras = getActivity().getIntent().getExtras();
		String stringFromIntent = extras.getString(Intent.EXTRA_TEXT);

		TextView tvText = (TextView) mRootView.findViewById(R.id.id_detail_text);
		tvText.setText(stringFromIntent);

		return mRootView;
	}


}
