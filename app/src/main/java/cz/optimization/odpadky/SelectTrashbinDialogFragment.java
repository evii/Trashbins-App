package cz.optimization.odpadky;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;


/**
 * Created by evi on 18. 12. 2017.
 */

public class SelectTrashbinDialogFragment extends DialogFragment {

    //Declaring the interface, to invoke a callback function in the implementing activity class
    AlertPositiveListener alertPositiveListener;

    //An interface to be implemented in the hosting activity for "OK" button click listener
    interface AlertPositiveListener {
        public void onPositiveClick(int position);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            alertPositiveListener = (AlertPositiveListener) context;
        } catch (ClassCastException e) {
            // The hosting activity does not implemented the interface AlertPositiveListener
            throw new ClassCastException(context.toString() + " must implement AlertPositiveListener");
        }
    }
    // This is the OK button listener for the alert dialog,
    // which in turn invokes the method onPositiveClick(position)
    //of the hosting activity which is supposed to implement it

    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog alert = (AlertDialog) dialog;
            int position = alert.getListView().getCheckedItemPosition();
            alertPositiveListener.onPositiveClick(position);
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /** Getting the arguments passed to this fragment */
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getText(R.string.filter_litter_title_dialog));

        // add a radio button list
        String[] trashBins = {getText(R.string.filter_litter_all_label).toString(),
                getText(R.string.filter_litter_colour_glass_label).toString(),
                getText(R.string.filter_litter_white_glass_label).toString(),
                getText(R.string.filter_litter_metal_label).toString(),
                getText(R.string.filter_litter_plastic_label).toString(),
                getText(R.string.filter_litter_paper_label).toString(),
                getText(R.string.filter_litter_carton_label).toString()
        };
        builder.setSingleChoiceItems(trashBins, position, null);

        // add OK and Cancel buttons
        builder.setPositiveButton("OK", positiveListener);
        builder.setNegativeButton("Cancel", null);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
