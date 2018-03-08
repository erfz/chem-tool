package io.github.erfz.chemtool;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by zfrs on 3/8/2018.
 */

public class EquationDialogFragment extends AppCompatDialogFragment {
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
        mEquation = getArguments().getString("equation", "error");
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
    public void onResume() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }
}