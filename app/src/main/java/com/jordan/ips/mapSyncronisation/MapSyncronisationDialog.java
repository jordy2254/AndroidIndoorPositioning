package com.jordan.ips.mapSyncronisation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ips.R;

public class MapSyncronisationDialog extends DialogFragment {

    MapSyncronisationDialogConfirmListener mapSyncronisationDialogConfirmListener;

    private String mapName;
    private String mapPassword;
    private boolean success = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_map, null);
        final EditText txtMapName = view.findViewById(R.id.txtMapId);
        final EditText txtMapPass = view.findViewById(R.id.txtMapPass);


        builder.setView(view)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mapName = txtMapName.getText().toString();
                        mapPassword = txtMapPass.getText().toString();
                        success = true;
                        dismiss();
                        if(mapSyncronisationDialogConfirmListener != null){
                            mapSyncronisationDialogConfirmListener.onMapInputDialogSuccessListener(mapName, mapPassword);
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        success = false;
                        dismiss();
                    }
                });
        return builder.create();
    }

    public void setMapSyncronisationDialogConfirmListener(MapSyncronisationDialogConfirmListener mapSyncronisationDialogConfirmListener) {
        this.mapSyncronisationDialogConfirmListener = mapSyncronisationDialogConfirmListener;
    }

    public String getMapName() {
        return mapName;
    }

    public String getMapPassword() {
        return mapPassword;
    }

    public boolean isSuccess() {
        return success;
    }
}
