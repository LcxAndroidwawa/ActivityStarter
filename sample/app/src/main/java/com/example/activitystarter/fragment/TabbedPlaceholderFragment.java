package com.example.activitystarter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.activitystarter.R;

import activitystarter.ActivityStarter;
import activitystarter.Arg;

public class TabbedPlaceholderFragment extends Fragment {

    @Arg int sectionNumber;

    public TabbedPlaceholderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabbed, container, false);
        ActivityStarter.fill(this);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, sectionNumber));
        return rootView;
    }
}
