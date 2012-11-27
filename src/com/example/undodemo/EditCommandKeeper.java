package com.example.undodemo;

import java.util.LinkedList;

import android.content.Context;
import android.widget.EditText;

public class EditCommandKeeper {
    private LinkedList<EditCommand> histories = new LinkedList<EditCommand>();

    void cut(Context context, EditText edit) {
        EditCommand command = new CutCommand();
        command.execute(context, edit);
        histories.add(command);
    }

    void copy(Context context, EditText edit) {
        EditCommand command = new CopyCommand();
        command.execute(context, edit);
        histories.add(command);
    }

    void paste(Context context, EditText edit) {
        EditCommand command = new PasteCommand();
        command.execute(context, edit);
        histories.add(command);
    }

    void undo(Context context, EditText edit) {
        if (histories.isEmpty()) {
            return;
        }
        EditCommand command = histories.removeLast();
        command.undo(context, edit);
    }
}
