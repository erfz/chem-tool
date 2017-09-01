package com.example.riesz.chemicalequationbalancer;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChemUtilsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChemUtilsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChemUtilsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_KEYBOARD_STATE = "keyboard_state";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean mKeyboardState;

    private Toast toast = null;
    private ConstraintLayout layout;
    private EditText LHSEqn;
    private EditText RHSEqn;
    private Button pasteEquationButton;
    private Button clearButton;
    private Button balanceButton;
    private Button plusButton;
    private Button parenthesesButton;
    private Button pasteLeftButton;
    private Button pasteRightButton;
    private ClipboardManager clipboard;
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
            if (!LHSEqn.getText().toString().isEmpty() || !RHSEqn.getText().toString().isEmpty()){
                pasteEquationButton.setVisibility(View.GONE);
                clearButton.setVisibility(View.VISIBLE);
            }
            else{
                clearButton.setVisibility(View.GONE);
                pasteEquationButton.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < s.length(); ++i){
                if (i == 0 && Character.isLowerCase(s.charAt(i))){
                    String str = s.subSequence(i, i + 1).toString().toUpperCase();
                    s.replace(i, i + 1, str);
                }
                else if (i > 0) {
                    if (Character.isLowerCase(s.charAt(i)) && (s.charAt(i - 1) == '(' || s.charAt(i - 1) == ')' || s.charAt(i - 1) == ' '
                            || s.charAt(i - 1) == '+' || Character.isLowerCase(s.charAt(i - 1))
                            || Character.isDigit(s.charAt(i - 1)))) {
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
        if (savedInstanceState != null){
            mKeyboardState = savedInstanceState.getBoolean(ARG_KEYBOARD_STATE, false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean keyboardState = imm.hideSoftInputFromWindow(layout.getWindowToken(), 0);
        outState.putBoolean(ARG_KEYBOARD_STATE, keyboardState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chem_utils, container, false);

        layout = (ConstraintLayout) rootView.findViewById(R.id.balanceequation_layout);
        LHSEqn = (EditText) rootView.findViewById(R.id.leftEquation);
        RHSEqn = (EditText) rootView.findViewById(R.id.rightEquation);
        pasteEquationButton = (Button) rootView.findViewById(R.id.equationpaste_button);
        clearButton = (Button) rootView.findViewById(R.id.clear_button);
        balanceButton = (Button) rootView.findViewById(R.id.balance_button);
        plusButton = (Button) rootView.findViewById(R.id.plus_button);
        parenthesesButton = (Button) rootView.findViewById(R.id.parentheses_button);
        pasteLeftButton = (Button) rootView.findViewById(R.id.leftpaste_button);
        pasteRightButton = (Button) rootView.findViewById(R.id.rightpaste_button);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if (mKeyboardState){
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_FORCED);
        }
        clearButton.setVisibility(View.GONE);
        LHSEqn.addTextChangedListener(eqnTextWatcher);
        RHSEqn.addTextChangedListener(eqnTextWatcher);

        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String equation = LHSEqn.getText().toString() + "=" + RHSEqn.getText().toString();
                try {
                    equation = EquationBalance.balanceEquation(equation);
                } catch (Exception e) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Invalid Equation!", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (getActivity().getCurrentFocus() != null){
                    getActivity().getCurrentFocus().clearFocus();
                }
                EquationDialogFragment f = EquationDialogFragment.newInstance(equation);
                f.show(getFragmentManager(), "equation dialog");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LHSEqn.setText("");
                RHSEqn.setText("");
                if (toast != null){
                    toast.cancel();
                }
                toast = Toast.makeText(getActivity(), "Equation Cleared", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LHSEqn.hasFocus()){
                    LHSEqn.getText().insert(LHSEqn.getSelectionStart(), "+");
                }
                if (RHSEqn.hasFocus()){
                    RHSEqn.getText().insert(RHSEqn.getSelectionStart(), "+");
                }
            }
        });

        parenthesesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LHSEqn.hasFocus()){
                    LHSEqn.getText().insert(LHSEqn.getSelectionStart(), "()");
                    LHSEqn.setSelection(LHSEqn.getSelectionStart() - 1);
                }
                if (RHSEqn.hasFocus()){
                    RHSEqn.getText().insert(RHSEqn.getSelectionStart(), "()");
                    RHSEqn.setSelection(RHSEqn.getSelectionStart() - 1);
                }
            }
        });

        pasteLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(clipboard.hasPrimaryClip())) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                        clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    LHSEqn.setText(item.coerceToText(getActivity()).toString().replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\] ]", ""));
                }
            }
        });

        pasteRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(clipboard.hasPrimaryClip())) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                        clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    RHSEqn.setText(item.coerceToText(getActivity()).toString().replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\] ]", ""));
                }
            }
        });

        pasteEquationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(clipboard.hasPrimaryClip())) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                        clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getActivity(), "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    String equation = item.coerceToText(getActivity()).toString();
                    System.out.println(equation);
                    equation = equation.replaceAll("-+", "=").replaceAll("<+","=").replaceAll(">+","=")
                            .replaceAll("→+", "=").replaceAll("←+", "=")
                            .replaceAll("↔+", "=").replaceAll("⇄+", "=")
                            .replaceAll("⇌+", "=");
                    System.out.println(equation);
                    equation = equation.replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\]=]", "");
                    System.out.println(equation);
                    equation = equation.replaceAll("=+","=");
                    String[] eqnHS = equation.split("=");
                    if (eqnHS.length != 2) {
                        if (toast != null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(getActivity(), "Invalid equation!", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    LHSEqn.setText(eqnHS[0]);
                    RHSEqn.setText(eqnHS[1]);
                }
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent event){
                if (event.getAction() == MotionEvent.ACTION_UP){
                    layout.requestFocus();
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
}
