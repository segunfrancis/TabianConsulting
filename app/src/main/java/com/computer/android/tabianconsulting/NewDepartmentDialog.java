package com.computer.android.tabianconsulting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class NewDepartmentDialog extends DialogFragment {
    private static final String TAG = "NewDepartmentDialog";

    //widgets
    private EditText mNewDepartment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_add_department, container, false);
        mNewDepartment = view.findViewById(R.id.input_new_department);

        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmpty(mNewDepartment.getText().toString())) {
                    Log.d(TAG, "onClick: adding new department to the list.");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference
                            .child(getString(R.string.dbnode_departments))
                            .child(mNewDepartment.getText().toString())
                            .setValue(mNewDepartment.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(), mNewDepartment.getText().toString() + " added successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Failed\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    getDialog().dismiss();

                    ((AdminActivity) getActivity()).getDepartments();
                }
            }
        });
        return view;
    }

    /**
     * Return true if the @param is null
     *
     * @param string
     * @return
     */
    private boolean isEmpty(String string) {
        return string.equals("");
    }
}
