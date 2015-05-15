package chris_boich.weathernow.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import chris_boich.weathernow.R;

/**
 * Created by ctboi_000 on 5/12/2015.
 */
public class AlertDialogFragmentNetworkIssue extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.network_error_title))
                .setMessage(context.getString(R.string.network_unavailable_message))
                .setPositiveButton(context.getString(R.string.error_ok_button_text), null);
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
