package com.dexter.tourist.MainTab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dexter.tourist.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIListener;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class BotFragment extends Fragment implements AIListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = BotFragment.class.getName();

    private AIService aiService;
    private ProgressBar progressBar;
    private ImageView recIndicator;
    private TextView t;
    private EditText contextEditText;
    Button listen;
    View bot_view;

    private Gson gson = GsonFactory.getGson();

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
        t = (TextView)bot_view.findViewById(R.id.resultTextView);
        progressBar = (ProgressBar)bot_view.findViewById(R.id.progressBar);
        contextEditText = (EditText)bot_view.findViewById(R.id.contextEditText);
        listen = (Button)bot_view.findViewById(R.id.buttonListen);

//        Spinner spinner = (Spinner)bot_view.findViewById(R.id.selectLanguageSpinner);
//        final ArrayAdapter<LanguageConfig> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Config.languages);
//        spinner.setAdapter(languagesAdapter);
//        spinner.setOnItemSelectedListener(this);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognition(v);

            }
        });

        final AIConfiguration config = new AIConfiguration("62e6f6b827d04e51b19660a051f95e4a",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        AIService aiService = AIService.getService(getContext(),config);
        aiService.setListener(this);

        return bot_view;
    }
//    @Override
//    public void onResult(AIResponse result) {
//        Log.d("Tweek",result.toString());
//        Result result1 = result.getResult();
//        t.setText(result1.getResolvedQuery() + "action : " + result1.getAction());
//    }

    private void initService(final LanguageConfig selectedLanguage) {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(selectedLanguage.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);

        if (aiService != null) {
            aiService.pause();
        }

        aiService = AIService.getService(getContext(), config);
        aiService.setListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_aiservice_sample, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item) {
//        final int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(AISettingsActivity.class);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



    public void stopRecognition(final View view) {
        aiService.stopListening();
    }

    public void cancelRecognition(final View view) {
        aiService.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        if (aiService != null) {
            aiService.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // use this method to reinit connection to recognition service
        if (aiService != null) {
            aiService.resume();
        }
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

                t.setText(gson.toJson(response));

                Log.i(TAG, "Received success response");

                // this is example how to get different parts of result object
                final Status status = response.getStatus();
                Log.i(TAG, "Status code: " + status.getCode());
                Log.i(TAG, "Status type: " + status.getErrorType());

                final Result result = response.getResult();
                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());

                Log.i(TAG, "Action: " + result.getAction());

                final String speech = result.getFulfillment().getSpeech();
                Log.i(TAG, "Speech: " + speech);
                TTS.speak(speech);

                final Metadata metadata = result.getMetadata();
                if (metadata != null) {
                    Log.i(TAG, "Intent id: " + metadata.getIntentId());
                    Log.i(TAG, "Intent name: " + metadata.getIntentName());
                }

                final HashMap<String, JsonElement> params = result.getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
                    }
                }
            }

        });
}

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                t.setText(error.toString());
            }
        });
    }

    public void startRecognition(final View view) {
        final String contextString = String.valueOf(contextEditText.getText());
        if (TextUtils.isEmpty(contextString)) {
            aiService.startListening();
        } else {
            final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
            final RequestExtras requestExtras = new RequestExtras(contexts, null);
            aiService.startListening(requestExtras);
        }

    }

    @Override
    public void onAudioLevel(final float level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float positiveLevel = Math.abs(level);

                if (positiveLevel > 100) {
                    positiveLevel = 100;
                }
                progressBar.setProgress((int) positiveLevel);
            }
        });
    }

    @Override
    public void onListeningStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onListeningCanceled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.INVISIBLE);
                t.setText("");
            }
        });
    }

    @Override
    public void onListeningFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final LanguageConfig selectedLanguage = (LanguageConfig) parent.getItemAtPosition(position);
        initService(selectedLanguage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

//    private void startActivity(Class<?> cls) {
//        final Intent intent = new Intent(this, cls);
//        startActivity(intent);
//    }
}

