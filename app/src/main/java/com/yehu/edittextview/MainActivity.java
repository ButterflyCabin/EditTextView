package com.yehu.edittextview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import widget.EditTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditTextView editTextView = (EditTextView) findViewById(R.id.edv);
        editTextView.setOnCompleteListener(charSequence -> Toast.makeText(MainActivity.this, charSequence, Toast.LENGTH_SHORT).show());
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> editTextView.setBorderColor(Color.RED));
    }
}
