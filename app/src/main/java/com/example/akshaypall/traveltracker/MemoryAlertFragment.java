package com.example.akshaypall.traveltracker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by akshaypall on 2015-07-16.
 */
public class MemoryAlertFragment extends DialogFragment {

    static final String MEMORY_KEY = "MEMORY";
    private Memory mMemory;
    private Listener mListener;
    private View mDialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMemory = (Memory)args.getSerializable(MEMORY_KEY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialogView = getActivity().getLayoutInflater().inflate(R.layout.memory_dialog_fragment, null);
        TextView city = (TextView)mDialogView.findViewById(R.id.city);
        city.setText(mMemory.city);
        TextView country = (TextView)mDialogView.findViewById(R.id.country);
        country.setText(mMemory.country);

        final Listener listener = (Listener)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mDialogView)
                .setTitle(getString(R.string.dialog_title))
                .setPositiveButton(getString(R.string.dialog_positive_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //If Okay Pressed
                        EditText notes = (EditText) mDialogView.findViewById(R.id.note_entry);
                        mMemory.note = notes.getText().toString();
                        listener.onSaveClicked(mMemory);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if Cancel Pressed
                        listener.onCancelClicked(mMemory);
                    }
                });

        return builder.create();
    }

    public static MemoryAlertFragment newInstance(Memory memory) {
        MemoryAlertFragment memoryAlertFragment = new MemoryAlertFragment();

        Bundle args = new Bundle();
        args.putSerializable(MEMORY_KEY, memory);
        memoryAlertFragment.setArguments(args);

        return memoryAlertFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener)getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Error");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface Listener{
        public void onSaveClicked(Memory memory);
        public void onCancelClicked(Memory memory);
    }
}
