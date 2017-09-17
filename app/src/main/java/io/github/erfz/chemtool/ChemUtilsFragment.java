package io.github.erfz.chemtool;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChemUtilsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChemUtilsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemUtilsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_KEYBOARD_STATE = "keyboard_state";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] savedEquation = new String[2];
    private boolean mKeyboardState;
    private boolean buttonStateChange;

    private ConstraintLayout layout;
    private CoordinatorLayout coordinatorLayout;
    private EditText LHSEqn;
    private EditText RHSEqn;
    private Button pasteClearButton;
    private Button balanceButton;
    private ClipboardManager clipboard;
    private InputMethodManager imm;
    private final TextWatcher eqnTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!LHSEqn.getText().toString().isEmpty() || !RHSEqn.getText().toString().isEmpty()) {
                pasteClearButton.setText(R.string.clear_button);
                buttonStateChange = true;
            } else {
                pasteClearButton.setText(R.string.equation_paste_button);
                buttonStateChange = false;
            }

            if (!LHSEqn.getText().toString().replaceAll("\\s+","").isEmpty()
                    && !RHSEqn.getText().toString().replaceAll("\\s+","").isEmpty()) {
                balanceButton.setEnabled(true);
            } else {
                balanceButton.setEnabled(false);
            }

            for (int i = 0; i < s.length(); ++i) {
                if (i == 0 && Character.isLowerCase(s.charAt(i))) {
                    String str = s.subSequence(i, i + 1).toString().toUpperCase();
                    s.replace(i, i + 1, str);
                } else if (i > 0) {
                    if (Character.isLowerCase(s.charAt(i)) && (s.charAt(i - 1) == '(' || s.charAt(i - 1) == ')'
                            || s.charAt(i - 1) == '[' || s.charAt(i - 1) == ']' || s.charAt(i - 1) == '.'
                            || s.charAt(i - 1) == '•' || s.charAt(i - 1) == '⋅' || s.charAt(i - 1) == '·'
                            || s.charAt(i - 1) == ' ' || s.charAt(i - 1) == '+'
                            || Character.isLowerCase(s.charAt(i - 1)) || Character.isDigit(s.charAt(i - 1)))) {
                        String str = s.subSequence(i, i + 1).toString().toUpperCase();
                        s.replace(i, i + 1, str);
                    }
                }
            }
        }
    };

    private OnFragmentInteractionListener mListener;

    public ChemUtilsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChemUtilsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChemUtilsFragment newInstance(String param1, String param2) {
        ChemUtilsFragment fragment = new ChemUtilsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (savedInstanceState != null) {
            mKeyboardState = savedInstanceState.getBoolean(ARG_KEYBOARD_STATE, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean keyboardState = imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        outState.putBoolean(ARG_KEYBOARD_STATE, keyboardState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chem_utils, container, false);

        layout = (ConstraintLayout) rootView.findViewById(R.id.balance_equation_layout);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        LHSEqn = (EditText) rootView.findViewById(R.id.left_equation_edittext);
        RHSEqn = (EditText) rootView.findViewById(R.id.right_equation_edittext);
        pasteClearButton = (Button) rootView.findViewById(R.id.paste_clear_button);
        balanceButton = (Button) rootView.findViewById(R.id.balance_button);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        buttonStateChange = false;

        balanceButton.setOnClickListener(this);
        pasteClearButton.setOnClickListener(this);

        balanceButton.setEnabled(false);

        if (mKeyboardState) {
            imm.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }
        LHSEqn.addTextChangedListener(eqnTextWatcher);
        RHSEqn.addTextChangedListener(eqnTextWatcher);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    layout.requestFocus();
                    imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        return rootView;
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
        String equation = null;
        switch (id) {
            case R.id.balance_button:
                equation = LHSEqn.getText().toString() + "=" + RHSEqn.getText().toString();
                new BalanceEquationTask().execute(equation);
                break;
            case R.id.paste_clear_button:
                if (buttonStateChange) {
                    savedEquation[0] = LHSEqn.getText().toString();
                    savedEquation[1] = RHSEqn.getText().toString();
                    LHSEqn.setText("");
                    RHSEqn.setText("");
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.snackbar_equation_cleared, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.snackbar_undo_clear, new UndoClearEquationListener());
                    snackbar.show();
                    break;
                }
                if (!(clipboard.hasPrimaryClip())) {
                    Snackbar.make(coordinatorLayout, R.string.snackbar_clipboard_no_text, Snackbar.LENGTH_SHORT).show();
                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                        clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                    Snackbar.make(coordinatorLayout, R.string.snackbar_clipboard_no_parseable_text, Snackbar.LENGTH_SHORT).show();
                } else {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    equation = item.coerceToText(getActivity()).toString();
                    equation = equation.replaceAll("-+|<+|>+|→+|←+|↔+|⇄+|⇌+", "=")
                            .replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\]=.•⋅· ]", "")
                            .replaceAll("=+", "=");
                    String[] eqnHS = equation.split("=");
                    if (eqnHS.length != 2) {
                        Snackbar.make(coordinatorLayout, R.string.snackbar_invalid_equation, Snackbar.LENGTH_SHORT).show();
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

    private class UndoClearEquationListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            LHSEqn.setText(savedEquation[0]);
            RHSEqn.setText(savedEquation[1]);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static class EquationDialogFragment extends DialogFragment {
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

    private class BalanceEquationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String equation = strings[0];
            try {
                equation = EquationBalance.balanceEquation(equation);
            } catch (Exception e) {
                return null;
            }
            return equation;
        }

        @Override
        protected void onPostExecute(String equation) {
            if (equation == null){
                Snackbar.make(coordinatorLayout, R.string.snackbar_invalid_equation, Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (getActivity().getCurrentFocus() != null) {
                getActivity().getCurrentFocus().clearFocus();
            }
            EquationDialogFragment f = EquationDialogFragment.newInstance(equation);
            f.show(getFragmentManager(), "equation dialog");
        }
    }
}
