package com.pennapps.instadubb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;

import at.maui.cardar.R;
import at.maui.cardar.ui.activity.ArActivity;

/**
 * Created by Carlos on 1/24/2016.
 */
public class LanguageSelectorActivity extends Activity{
    public static final String KEY_LANG = "key_lang";
    public static final String KEY_TEXT = "key_text";

    private static final String[] ITEMS = {"Deutsche", "Español", "Français", "हिंदी", "中文"};
    private static final String[] KEYS = {"de", "es", "fr", "hi", "zh-CN"};
    private static final HashMap<String, String> ITEM_TO_KEY;
    static {
        HashMap<String, String> map = new HashMap<String, String>();
        for(int i = 0; i < ITEMS.length; i++){
            map.put(ITEMS[i], KEYS[i]);
        }

        ITEM_TO_KEY = map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        final Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        final EditText text = (EditText) findViewById(R.id.editText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS);
        dropdown.setAdapter(adapter);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LanguageSelectorActivity.this, ArActivity.class);
                i.putExtra(KEY_LANG, ITEM_TO_KEY.get(dropdown.getSelectedItem().toString()));
                i.putExtra(KEY_TEXT, text.getText().toString());

                startActivity(i);
            }
        });
    }
}
