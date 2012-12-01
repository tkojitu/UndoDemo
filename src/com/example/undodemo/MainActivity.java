package com.example.undodemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditHistorian historian = new EditHistorian();
    private EditControl control = new EditControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getEdit().addTextChangedListener(historian);
    }

    private EditText getEdit() {
        return (EditText)findViewById(R.id.edit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void onCut(View view) {
        control.cut(this, getEdit());
    }

    public void onCopy(View view) {
        control.copy(this, getEdit());
    }

    public void onPaste(View view) {
        control.paste(this, getEdit());
    }

    public void onUndo(View view) {
        historian.undo(getEdit().getText());
    }

    public void onRedo(View view) {
        historian.redo(getEdit().getText());
    }
}
