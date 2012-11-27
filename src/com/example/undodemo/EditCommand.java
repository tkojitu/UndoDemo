package com.example.undodemo;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

abstract class EditCommand {
    abstract void execute(Context context, EditText edit);

    abstract void undo(Context context, EditText edit);

    protected ClipboardManager getClipboard(Context context) {
        return (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
    }
}

class ComposedCommand extends EditCommand {
    private ArrayList<EditCommand> commands = new ArrayList<EditCommand>();

    protected void addCommand(EditCommand command) {
        commands.add(command);
    }

    @Override
    void execute(Context context, EditText edit) {
        for (EditCommand command : commands) {
            command.execute(context, edit);
        }
    }

    @Override
    void undo(Context context, EditText edit) {
        for (int i = commands.size() - 1; i >= 0; --i) {
            commands.get(i).undo(context, edit);
        }
    }
}

class SelectCommand extends EditCommand {
    private int start = -1;
    private int stop = -1;

    @Override
    void execute(Context context, EditText edit) {
        start = edit.getSelectionStart();
        stop = edit.getSelectionEnd();
    }

    @Override
    void undo(Context context, EditText edit) {
        edit.setSelection(start, stop);
    }
}

class DeleteCommand extends EditCommand {
    private int position = -1;
    private CharSequence text = "";

    @Override
    void execute(Context context, EditText edit) {
        int st = edit.getSelectionStart();
        int ed = edit.getSelectionEnd();
        if (st == ed) {
            return;
        }
        Editable editable = edit.getEditableText();
        text = editable.subSequence(st, ed);
        position = st;
        editable.delete(st, ed);
    }

    @Override
    void undo(Context context, EditText edit) {
        if (text.length() == 0) {
            return;
        }
        Editable editable = edit.getEditableText();
        editable.insert(position, text);
    }
}

class InsertClipCommand extends EditCommand {
    private int position = -1;
    private CharSequence text = "";

    @Override
    void execute(Context context, EditText edit) {
        int st = edit.getSelectionStart();
        ClipData data = getClipboard(context).getPrimaryClip();
        Editable editable = edit.getEditableText();
        text = data.getItemAt(0).getText();
        position = st;
        editable.insert(st, text);
    }

    @Override
    void undo(Context context, EditText edit) {
        if (text.length() == 0) {
            return;
        }
        Editable editable = edit.getEditableText();
        editable.delete(position, position + text.length());
    }
}

class CopyCommand extends EditCommand {
    @Override
    void execute(Context context, EditText edit) {
        ClipData data = newSelectedData(edit);
        if (data == null) {
            return;
        }
        getClipboard(context).setPrimaryClip(data);
    }

    private ClipData newSelectedData(EditText edit) {
        CharSequence seq = getSelectedText(edit);
        if (seq.length() == 0) {
            return null;
        }
        return ClipData.newPlainText("text", seq);
    }

    private CharSequence getSelectedText(EditText edit) {
        CharSequence seq = edit.getText();
        int st = edit.getSelectionStart();
        int ed = edit.getSelectionEnd();
        return seq.subSequence(st, ed);
    }

    @Override
    void undo(Context context, EditText edit) {
    }
}

class CutCommand extends ComposedCommand {
    CutCommand() {
        addCommand(new CopyCommand());
        addCommand(new SelectCommand());
        addCommand(new DeleteCommand());
    }
}

class PasteCommand extends ComposedCommand {
    PasteCommand() {
        addCommand(new SelectCommand());
        addCommand(new DeleteCommand());
        addCommand(new InsertClipCommand());
    }
}
