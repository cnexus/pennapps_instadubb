package at.maui.cardar.ui.activity;

import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import at.maui.cardar.R;
import at.maui.cardar.ui.ar.Renderer;
import at.maui.cardar.ui.widget.CardboardOverlayView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hound.android.fd.HoundSearchResult;
import com.hound.android.fd.Houndify;
import com.hound.android.fd.HoundifyButton;
import com.hound.android.sdk.VoiceSearchInfo;
import com.hound.core.model.sdk.HoundResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by maui on 02.07.2014.
 */
public class ArActivity extends CardboardActivity {
    @InjectView(R.id.overlay)
    CardboardOverlayView overlayView;

    @InjectView(R.id.cardboard_view)
    CardboardView cardboardView;

    private Renderer mRenderer;
    private Recorder mRecorder;
    private int mRecordingNum = 0;
    private HoundifyButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);

        button = (HoundifyButton) findViewById(R.id.button);

        // Inject views
        ButterKnife.inject(this);

        // Associate a CardboardView.StereoRenderer with cardboardView.
        mRenderer = new Renderer(this);
        cardboardView.setRenderer(mRenderer);

        // Associate the cardboardView with this activity.
        setCardboardView(cardboardView);

        //overlayView.show3DToast("Welcome to CardAR.");

        // Normally you'd only have to do this once in your Application#onCreate
        Houndify.get(this).setClientId( Constants.CLIENT_ID );
        Houndify.get(this).setClientKey( Constants.CLIENT_KEY );
        Houndify.get(this).setRequestInfoFactory(StatefulRequestInfoFactory.get(this));
    }

    @Override
    public void onCardboardTrigger() {
        // Toggle sound recording
        mRecordingNum++;

        if(mRecordingNum % 2 == 0){ // Recording was stopped
            mRecorder.startRecording();
        }else{ // Recording was started
            mRecorder.stopRecording();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Houndify.REQUEST_CODE) {
            final HoundSearchResult result = Houndify.get(this).fromActivityResult(resultCode, data);
            if (result.hasResult()) {
                onResponse(result.getResponse());
            }
            else if (result.getErrorType() != null) {
                onError(result.getException(), result.getErrorType());
            }
            else {
                textView.setText("Aborted search");
            }
        }
    }

    private void onResponse(final HoundResponse response) {
        if (response.getResults().size() > 0) {
            // Required for conversational support
            StatefulRequestInfoFactory.get(this).setConversationState(response.getResults().get(0).getConversationState());

            textView.setText("Written response:\n" + response.getResults().get(0).getWrittenResponse() +
                    "\nWritten response long:\n" + response.getResults().get(0).getWrittenResponseLong() +
                    "\nTranscription:\n" + response.getDisambiguation().getChoiceData().get(0).getTranscription() +
                    "\nClass:\n" + response.getDisambiguation().getChoiceData().get(0).getClass());

        }
        else {
            textView.setText("Received empty response!");
        }
    }

    private void onError(final Exception ex, final VoiceSearchInfo.ErrorType errorType) {
        textView.setText(errorType.name() + "\n\n" + exceptionToString(ex));
    }

    private static String exceptionToString(final Exception ex) {
        try {
            final StringWriter sw = new StringWriter(1024);
            final PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.close();
            return sw.toString();
        }
        catch (final Exception e) {
            return "";
        }
    }
}
