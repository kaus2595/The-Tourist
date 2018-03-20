package com.dexter.tourist.MainTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexter.tourist.Guide.navigation_activity;
import com.dexter.tourist.R;

public class GuideFragment extends Fragment {
    View mMainView;

    CardView card1,card2,card3,card4,card5;
    public GuideFragment() {
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
        mMainView=inflater.inflate(R.layout.fragment_guide, container, false);

        card1=(CardView)mMainView.findViewById(R.id.card1);
        card2=(CardView)mMainView.findViewById(R.id.card2);
        card3=(CardView)mMainView.findViewById(R.id.card3);
        card4=(CardView)mMainView.findViewById(R.id.card4);
        card5=(CardView)mMainView.findViewById(R.id.card5);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtonav();
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtonav();
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtonav();
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtonav();
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtonav();
            }
        });

        return mMainView;
    }

    private void sendtonav() {
        Intent navIntent=new Intent(getContext(),navigation_activity.class);
        startActivity(navIntent);
    }

}
