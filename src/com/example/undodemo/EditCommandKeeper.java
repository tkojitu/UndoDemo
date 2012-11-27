package com.example.undodemo;

import java.util.LinkedList;

import android.content.Context;
import android.widget.EditText;

public class EditCommandKeeper {
    private LinkedList<EditCommand> undos = new LinkedList<EditCommand>();
    private LinkedList<EditCommand> redos = new LinkedList<EditCommand>();

    private void execute(Context context, EditText edit, EditCommand command) {
        command.execute(context, edit);
        undos.add(command);
        clearRedos();
    }

    private void clearRedos() {
        while (!redos.isEmpty()) {
            redos.removeFirst();
        }
    }

    void cut(Context context, EditText edit) {
        EditCommand command = new CutCommand();
        execute(context, edit, command);
    }

    void copy(Context context, EditText edit) {
        EditCommand command = new CopyCommand();
        execute(context, edit, command);
    }

    void paste(Context context, EditText edit) {
        EditCommand command = new PasteCommand();
        execute(context, edit, command);
    }

    void undo(Context context, EditText edit) {
        if (undos.isEmpty()) {
            return;
        }
        EditCommand command = undos.removeLast();
        redos.add(command);
        command.undo(context, edit);
    }

    void redo(Context context, EditText edit) {
        if (redos.isEmpty()) {
            return;
        }
        EditCommand command = redos.removeLast();
        undos.add(command);
        command.execute(context, edit);
    }
}
