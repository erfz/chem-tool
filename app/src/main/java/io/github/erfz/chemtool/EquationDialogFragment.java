package io.github.erfz.chemtool;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by tensor on 8/14/2017.
 */

public class EquationDialogFragment extends DialogFragment {
    private String mEquation;

    public static EquationDialogFragment newInstance(String equation) {
        EquationDialogFragment fragment = new EquationDialogFragment();
        Bundle args = new Bundle();
        args.putString("equation", equation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEquation = getArguments().getString("equation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_equation, container, false);

        TextView equationtv = (TextView) v.findViewById(R.id.balanced_equation);
        equationtv.setText(mEquation);
        equationtv.setTextIsSelectable(true);
        equationtv.setTextColor(Color.BLACK);

        Button copyButton = (Button) v.findViewById(R.id.copy_button);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("equation", mEquation);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), R.string.snackbar_equation_copied, Snackbar.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
