package com.dexter.tourist.MainTab;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dexter.tourist.R;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class BotFragment extends Fragment implements AIListener {

     AIService aiService;
     TextView t;
     Button listen;
     View bot_view;
    public BotFragment() {
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
        bot_view = inflater.inflate(R.layout.fragment_bot, container, false);
        t = (TextView)bot_view.findViewById(R.id.textView);
        listen = (Button)bot_view.findViewById(R.id.listen);
        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aiService.startListening();

            }
        });
        final AIConfiguration config = new AIConfiguration("62e6f6b827d04e51b19660a051f95e4a",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        AIService aiService = AIService.getService(getContext(),config);
        aiService.setListener(this);

        return bot_view;
    }
    @Override
    public void onResult(AIResponse result) {
        Log.d("Tweek",result.toString());
        Result result1 = result.getResult();
        t.setText(result1.getResolvedQuery() + "action : " + result1.getAction());
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

}
