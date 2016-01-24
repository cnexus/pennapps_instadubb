package at.maui.cardar.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.pennapps.instadubb.ApiRequestor;
import com.pennapps.instadubb.LanguageSelectorActivity;

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

    private String mLangKey;
    private String mText;
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

        // Retrieve selected language
        Intent i = getIntent();
        mLangKey = i.getStringExtra(LanguageSelectorActivity.KEY_LANG);
        mText = i.getStringExtra(LanguageSelectorActivity.KEY_TEXT);

        Log.d("CardAR", "Text is " + mText);
    }

    public void onCardboardTrigger() {
        mRecordingNum++;

        if((mRecordingNum - 1) % 2 == 0){
            ApiRequestor req = new ApiRequestor(mLangKey, this);
            req.execute(mText);
        }
    }

    public void setText(String text){
        overlayView.show3DToast(text);
    }
}
