package com.dexter.tourist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dexter.tourist.MainTab.BotFragment;
import com.dexter.tourist.MainTab.ReviewFragment;

public class ReviewActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewFragment=new Intent(ReviewActivity.this, ReviewFragment.class);
                startActivity(reviewFragment);
            }
        });
    }
}
