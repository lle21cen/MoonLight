package org.techtown.ideaconcert;

import android.app.ProgressDialog;
import android.content.Context;


public class ShowProgressDialog {
    static ProgressDialog dialog;

    public static void showProgressDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Checking...");
        dialog.show();
    }

    public static void dismissProgressDialog() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
