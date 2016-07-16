package com.myapps.vincekearney.todooey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DeleteToDoDialog extends AlertDialog
{
    /* ---- Properties ---- */
    private DeleteDialogListener listener;
    private ToDoItem toDoItem;

    // The interface (or protocol in iOS for delegate) that states the methods the listener implements.
    public interface DeleteDialogListener
    {
        void DeleteToDo(ToDoItem item);
        void DeleteAllToDos();
    }

    /* ---- Constructor ---- */
    protected DeleteToDoDialog(Context context) {
        super(context);
    }

    /* ---- Setting up the dialog ---- */
    public DeleteToDoDialog setDialogToDo(ToDoItem toDoItem)
    {
        this.toDoItem = toDoItem;
        if(this.toDoItem == null)
        {
            this.setTitle(R.string.delete_all_to_do);
            this.setMessage(this.getContext().getString(R.string.delete_all_to_do_message));
            setButton(BUTTON_POSITIVE, this.getContext().getString(R.string.yes), onPositive);
        }
        else
        {
            this.setTitle(R.string.delete_to_do);
            this.setMessage(this.toDoItem.getTodotext());
            setButton(BUTTON_POSITIVE, this.getContext().getString(R.string.delete), onPositive);
        }
        setButton(BUTTON_NEGATIVE, this.getContext().getString(R.string.cancel), (OnClickListener)null);

        return this;
    }

    public void setListener(DeleteDialogListener listener) {
        this.listener = listener;
    }

    private OnClickListener onPositive = new OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if(listener != null)
            {
                if(toDoItem == null)
                    listener.DeleteAllToDos();
                else
                    listener.DeleteToDo(toDoItem);
            }
        }
    };

    /* ===============END OF CLASS=============== */
}
