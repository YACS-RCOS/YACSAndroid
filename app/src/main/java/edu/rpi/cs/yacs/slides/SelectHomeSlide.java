package edu.rpi.cs.yacs.slides;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.paolorotolo.appintro.ISlidePolicy;

import edu.rpi.cs.yacs.R;

/**
 * Created by Mark Robinson on 9/26/16.
 */

public final class SelectHomeSlide extends Fragment implements ISlidePolicy {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.select_home_slide, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String houseOfYACS = preferences.getString(getString(R.string.houseOfYACS), "unkown");

        if (houseOfYACS.equals("unkown")) {
            makeYACSHomeDialog();
        }

        return view;
    }

        @Override
    public boolean isPolicyRespected() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String houseOfYACS = preferences.getString(getString(R.string.houseOfYACS), "unkown");

            return !(houseOfYACS.equals("unkown")); // If user should be allowed to leave this slide
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        makeYACSHomeDialog();
    }

    public void makeYACSHomeDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.houseOfYACS)
                .items(R.array.home_YACS_values)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        preferences.edit().putString(getString(R.string.houseOfYACS), String.valueOf(text)).apply();

                        return true;
                    }
                })
                .positiveText(R.string.Select)
                .show();
    }
}