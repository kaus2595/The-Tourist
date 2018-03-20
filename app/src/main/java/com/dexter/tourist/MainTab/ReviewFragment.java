package com.dexter.tourist.MainTab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dexter.tourist.R;
import com.dexter.tourist.ReviewActivity;


public class ReviewFragment extends Fragment {

    Button button;
    View myview;
    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myview=inflater.inflate(R.layout.fragment_review, container, false);
    button=(Button)myview.findViewById(R.id.settings_image_btn);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent reviewIntent=new Intent(getContext(), ReviewActivity.class);
            startActivity(reviewIntent);
        }
    });
    return myview;
    }
}
