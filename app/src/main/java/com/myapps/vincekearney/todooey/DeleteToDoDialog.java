package com.myapps.vincekearney.todooey;

import android.app.AlertDialog;
import android.content.Context;

import com.myapps.vincekearney.todooey.Database.ToDoItem;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeleteToDoDialog extends AlertDialog {
    /* ---- Properties ---- */
    private DeleteDialogListener listener;

    // The interface (or protocol in iOS for delegate) that states the methods the listener implements.
    public interface DeleteDialogListener {
        void DeleteToDo(ToDoItem item);
        void DeleteAllToDos();
    }

    public DeleteToDoDialog(Context context) {
        super(context);
    }

    public void showAlert(final ToDoItem item) {
        String title = getContext().getString(R.string.delete_all_to_do);
        String message = getContext().getString(R.string.delete_all_to_do_message);

        if (item != null) {
            title = getContext().getString(R.string.delete_to_do);
            message = item.getTodotext();
        }

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setCancelText(getContext().getString(R.string.nope))
                .setConfirmText(getContext().getString(R.string.aye))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sDialog) {
                        if (item != null)
                            listener.DeleteToDo(item);
                        else
                            listener.DeleteAllToDos();

                        sDialog.cancel();
                    }
                }).show();
    }

    public void setListener(DeleteDialogListener listener) {
        this.listener = listener;
    }
}
