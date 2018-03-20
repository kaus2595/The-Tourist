package com.dexter.tourist.MainTab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.CheckBox;
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
import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class BotFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    public static final String TAG = BotFragment.class.getName();

    private Gson gson = GsonFactory.getGson();

    private TextView resultTextView;
    private EditText contextEditText;
    private EditText queryEditText;
    private CheckBox eventCheckBox;
    private Spinner eventSpinner;
    private AIDataService aiDataService;
    Button listen;
    View bot_view;


    public BotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bot_view = inflater.inflate(R.layout.fragment_bot, container, false);
        contextEditText = (EditText)bot_view.findViewById(R.id.contextEditText);

        resultTextView = (TextView)bot_view.findViewById(R.id.resultTextView);
        contextEditText = (EditText)bot_view.findViewById(R.id.contextEditText);
        queryEditText = (EditText)bot_view.findViewById(R.id.textQuery);

        bot_view.findViewById(R.id.buttonSend).setOnClickListener(this);
        bot_view.findViewById(R.id.buttonClear).setOnClickListener(this);

        eventSpinner = (Spinner)bot_view.findViewById(R.id.selectEventSpinner);
        final ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>( getContext(),android.R.layout.simple_spinner_dropdown_item, Config.events);
        eventSpinner.setAdapter(eventAdapter);

//        Spinner spinner = (Spinner)bot_view.findViewById(R.id.selectLanguageSpinner);
//        final ArrayAdapter<LanguageConfig> languagesAdapter = new ArrayAdapter<String>(getContext() ,android.R.layout.simple_spinner_dropdown_item, Config.languages);
//        spinner.setAdapter(languagesAdapter);
//        spinner.setOnItemSelectedListener(this);
       return bot_view;
    }

//        Spinner spinner = (Spinner)bot_view.findViewById(R.id.selectLanguageSpinner);
//        final ArrayAdapter<LanguageConfig> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Config.languages);
//        spinner.setAdapter(languagesAdapter);
//        spinner.setOnItemSelectedListener(this);

    private void initService(final LanguageConfig selectedLanguage) {
        final AIConfiguration.SupportedLanguages lang = AIConfiguration.SupportedLanguages.fromLanguageTag(selectedLanguage.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(selectedLanguage.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(getContext(),config);
    }
private void clearEditText() {
    queryEditText.setText("");
}

    /*
    * AIRequest should have query OR event
    */
    private void sendRequest() {

        final String queryString = !eventSpinner.isEnabled() ? String.valueOf(queryEditText.getText()) : null;
        final String eventString = eventSpinner.isEnabled() ? String.valueOf(String.valueOf(eventSpinner.getSelectedItem())) : null;
        final String contextString = String.valueOf(contextEditText.getText());

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        final AsyncTask<String, Void, AIResponse> task = new AsyncTask<String, Void, AIResponse>() {

            private AIError aiError;

            @Override
            protected AIResponse doInBackground(final String... params) {
                final AIRequest request = new AIRequest();
                String query = params[0];
                String event = params[1];

                if (!TextUtils.isEmpty(query))
                    request.setQuery(query);
                if (!TextUtils.isEmpty(event))
                    request.setEvent(new AIEvent(event));
                final String contextString = params[2];
                RequestExtras requestExtras = null;
                if (!TextUtils.isEmpty(contextString)) {
                    final List<AIContext> contexts = Collections.singletonList(new AIContext(contextString));
                    requestExtras = new RequestExtras(contexts, null);
                }
                try {
                    return aiDataService.request(request, requestExtras);
                } catch (AIServiceException e1) {
                    e1.printStackTrace();
                    return null;
                }
            }

//            @Override
//            protected void onPostExecute(final AIResponse response) {
//                if (response != null) {
//                    onResult(response);
//                } else {
//                    onError(aiError);
//                }
//            }
        };

        task.execute(queryString, eventString, contextString);
    }

    public void checkBoxClicked() {
        eventSpinner.setEnabled(eventCheckBox.isChecked());
        queryEditText.setVisibility(!eventCheckBox.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

                resultTextView.setText(gson.toJson(response));

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

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.toString());
            }
        });
    }


    private void startActivity(Class<?> cls) {
        final Intent intent = new Intent(getContext(),cls);
        startActivity(intent);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final LanguageConfig selectedLanguage = (LanguageConfig) parent.getItemAtPosition(position);
        initService(selectedLanguage);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonClear:
                clearEditText();
                break;
            case R.id.buttonSend:
                sendRequest();
                break;
            case R.id.eventsCheckBox:
                checkBoxClicked();
                break;
        }
    }
}

