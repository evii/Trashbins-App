package cz.optimization.odpadky;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import static android.R.attr.duration;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by evi on 18. 12. 2017.
 */

public class SelectTrashbinDialogFragment extends DialogFragment {
    private DialogInterface.OnClickListener onTrashbinSelected;
    private static final String TAG = "SelectTrashbin";

    public void setCallBack(DialogInterface.OnClickListener onTrashbin) {
        onTrashbinSelected = onTrashbin;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getText(R.string.filter_litter_title_dialog));

        // add a radio button list
        String[] trashBins = { getText(R.string.filter_litter_all_label).toString(),
                getText(R.string.filter_litter_colour_glass_label).toString(),
                getText(R.string.filter_litter_white_glass_label).toString(),
                getText(R.string.filter_litter_metal_label).toString(),
                getText(R.string.filter_litter_plastic_label).toString(),
                getText(R.string.filter_litter_paper_label).toString(),
                getText(R.string.filter_litter_carton_label).toString()
        };
        int checkedItem = 4; // plastic default
        builder.setSingleChoiceItems(trashBins, -1, onTrashbinSelected);


        // add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                Toast toast = Toast.makeText(getActivity(), "vybrano OKOK", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
