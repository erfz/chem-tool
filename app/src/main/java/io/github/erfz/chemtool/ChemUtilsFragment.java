package io.github.erfz.chemtool;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import butterknife.Unbinder;

public class ChemUtilsFragment extends Fragment implements View.OnClickListener {
    private static final String CHEMICAL_DELIMITERS = "()[].•⋅·+ ";

    private Unbinder unbinder;
    private OnFragmentInteractionListener mListener;
    private String[] savedEquation = new String[2];
    private boolean buttonStateChange;
    private boolean ignoreIteration;

    @BindView(R.id.left_equation_et) EditText LHSEqn;
    @BindView(R.id.right_equation_et) EditText RHSEqn;
    @BindView(R.id.paste_clear_button) Button pasteClearButton;
    @BindView(R.id.balance_button) Button balanceButton;

    @OnTouch(R.id.chem_utils_layout)
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            view.requestFocus();
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return true;
    }

    private final TextWatcher eqnTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (ignoreIteration) return;

            boolean LHSIsEmpty = LHSEqn.getText().toString().isEmpty();
            boolean RHSIsEmpty = RHSEqn.getText().toString().isEmpty();

            if (!LHSIsEmpty || !RHSIsEmpty) {
                pasteClearButton.setText(R.string.clear_button);
                buttonStateChange = true;
            } else {
                pasteClearButton.setText(R.string.equation_paste_button);
                buttonStateChange = false;
            }

            if (!LHSIsEmpty && !RHSIsEmpty) {
                balanceButton.setEnabled(true);
            } else {
                balanceButton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (ignoreIteration) return;
            ignoreIteration = true;

            for (int i = 0; i < s.length(); ++i) {
                if (i == 0 && Character.isLowerCase(s.charAt(i))) {
                    String str = s.subSequence(i, i + 1).toString().toUpperCase();
                    s.replace(i, i + 1, str);
                } else if (i > 0) {
                    char c = s.charAt(i - 1);
                    if (Character.isLowerCase(s.charAt(i)) && (CHEMICAL_DELIMITERS.indexOf(c) != -1
                            || Character.isLowerCase(c) || Character.isDigit(c))) {
                        String str = s.subSequence(i, i + 1).toString().toUpperCase();
                        s.replace(i, i + 1, str);
                    }
                }
            }

            ignoreIteration = false;
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chem_utils, container, false);
        unbinder = ButterKnife.bind(this, view);

        buttonStateChange = false;
        balanceButton.setEnabled(false);
        balanceButton.setOnClickListener(this);
        pasteClearButton.setOnClickListener(this);
        LHSEqn.addTextChangedListener(eqnTextWatcher);
        RHSEqn.addTextChangedListener(eqnTextWatcher);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String equation;
        switch (id) {
            case R.id.balance_button:
                equation = LHSEqn.getText().toString() + "=" + RHSEqn.getText().toString();
                new BalanceEquationTask().execute(equation);
                break;
            case R.id.paste_clear_button:
                if (buttonStateChange) {
                    Editable LHSEditable = LHSEqn.getText();
                    Editable RHSEditable = RHSEqn.getText();
                    savedEquation[0] = LHSEditable.toString();
                    savedEquation[1] = RHSEditable.toString();
                    LHSEditable.clear();
                    RHSEditable.clear();
                    Snackbar.make(view, R.string.snackbar_equation_cleared, Snackbar.LENGTH_LONG)
                            .setAction(R.string.snackbar_undo_clear, new UndoClearEquationListener())
                            .show();
                    break;
                } else {
                    ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (!clipboard.hasPrimaryClip()) {
                        Snackbar.make(view, R.string.snackbar_clipboard_no_text, Snackbar.LENGTH_SHORT).show();
                    } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                            clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                        Snackbar.make(view, R.string.snackbar_clipboard_no_parseable_text, Snackbar.LENGTH_SHORT).show();
                    } else {
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        equation = item.coerceToText(getContext()).toString();
                        equation = equation.replaceAll("-+|<+|>+|→+|←+|↔+|⇄+|⇌+", "=")
                                .replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\]=.•⋅· ]", "")
                                .replaceAll("=+", "=");
                        String[] eqnHS = equation.split("=");
                        if (eqnHS.length != 2) {
                            Snackbar.make(view, R.string.snackbar_invalid_equation, Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        LHSEqn.setText(eqnHS[0].trim());
                        RHSEqn.setText(eqnHS[1].trim());
                        LHSEqn.setSelection(LHSEqn.length());
                        RHSEqn.setSelection(RHSEqn.length());
                    }
                    break;
                }
        }
    }

    private class UndoClearEquationListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            LHSEqn.setText(savedEquation[0]);
            RHSEqn.setText(savedEquation[1]);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class BalanceEquationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String equation = strings[0];
            try {
                equation = EquationBalance.balanceEquation(equation);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return equation;
        }

        @Override
        protected void onPostExecute(String equation) {
            if (equation == null) {
                Snackbar.make(getActivity().findViewById(R.id.coordinator_layout), R.string.snackbar_invalid_equation, Snackbar.LENGTH_SHORT).show();
                return;
            }
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                v.clearFocus();
            }
            EquationDialogFragment f = EquationDialogFragment.newInstance(equation);
            f.show(getChildFragmentManager(), "equation dialog");
        }
    }
}
