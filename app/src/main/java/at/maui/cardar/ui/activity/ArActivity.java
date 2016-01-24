package at.maui.cardar.ui.activity;

import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import at.maui.cardar.R;
import at.maui.cardar.ui.ar.Renderer;
import at.maui.cardar.ui.widget.CardboardOverlayView;
import butterknife.ButterKnife;
import butterknife.InjectView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);

        // Inject views
        ButterKnife.inject(this);

        // Associate a CardboardView.StereoRenderer with cardboardView.
        mRenderer = new Renderer(this);
        cardboardView.setRenderer(mRenderer);

        // Associate the cardboardView with this activity.
        setCardboardView(cardboardView);

        //overlayView.show3DToast("Welcome to CardAR.");
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
}
