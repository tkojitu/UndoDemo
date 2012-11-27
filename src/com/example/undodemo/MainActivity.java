package com.example.undodemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditCommandKeeper keeper = new EditCommandKeeper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        keeper.cut(this, getEdit());
    }

    public void onCopy(View view) {
        keeper.copy(this, getEdit());
    }

    public void onPaste(View view) {
        keeper.paste(this, getEdit());
    }

    public void onUndo(View view) {
        keeper.undo(this, getEdit());
    }

    public void onRedo(View view) {
        keeper.redo(this, getEdit());
    }
}
