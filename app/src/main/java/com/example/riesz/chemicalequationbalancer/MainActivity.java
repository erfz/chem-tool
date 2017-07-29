package com.example.riesz.chemicalequationbalancer;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.riesz.chemicalequationbalancer.MESSAGE";
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        final EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        final Button balanceButton = (Button) findViewById(R.id.balance_button);

        LHSEqn.addTextChangedListener(new TextWatcher() {
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
        });

        RHSEqn.addTextChangedListener(new TextWatcher() {
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
        });

        RHSEqn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    balanceButton.performClick();
                    handled = true;
                }
                return handled;
            }
        });
    }
    public void getBalancedEquation(View view) {
        Intent intent = new Intent(this, BalanceEquationActivity.class);
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        String equation = LHSEqn.getText().toString() + "=" + RHSEqn.getText().toString(); // string operations in separate class
        try {
            equation = EquationBalance.balanceEquation(equation);
        } catch (Exception e) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Invalid Equation!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        intent.putExtra(EXTRA_MESSAGE, equation);
        startActivity(intent);
    }
    public void clearEqn(View view) {
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        LHSEqn.setText("");
        RHSEqn.setText("");
        LHSEqn.requestFocus();
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(this, "Equation Cleared", Toast.LENGTH_SHORT);
        toast.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(LHSEqn, InputMethodManager.SHOW_IMPLICIT);
    }
    public void addPlus(View view) {
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        if (LHSEqn.hasFocus()){
            LHSEqn.getText().insert(LHSEqn.getSelectionStart(), "+");
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(LHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
        if (RHSEqn.hasFocus()){
            RHSEqn.getText().insert(RHSEqn.getSelectionStart(), "+");
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(RHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void addParentheses(View view) {
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        if (LHSEqn.hasFocus()){
            LHSEqn.getText().insert(LHSEqn.getSelectionStart(), "()");
            LHSEqn.setSelection(LHSEqn.getSelectionStart() - 1);
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(LHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
        if (RHSEqn.hasFocus()){
            RHSEqn.getText().insert(RHSEqn.getSelectionStart(), "()");
            RHSEqn.setSelection(RHSEqn.getSelectionStart() - 1);
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(RHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void pasteLeft(View view){
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            LHSEqn.setText(item.coerceToText(this).toString().replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\] ]", ""));
            LHSEqn.setSelection(LHSEqn.getText().length());
            LHSEqn.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(LHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void pasteRight(View view){
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            RHSEqn.setText(item.coerceToText(this).toString().replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\] ]", ""));
            RHSEqn.setSelection(RHSEqn.getText().length());
            RHSEqn.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(RHSEqn, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void pasteEquation(View view){
        EditText LHSEqn = (EditText) findViewById(R.id.leftEquation);
        EditText RHSEqn = (EditText) findViewById(R.id.rightEquation);
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (!(clipboard.hasPrimaryClip())) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain anything!", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) ||
                clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML))) {
            if (toast != null){
                toast.cancel();
            }
            toast = Toast.makeText(this, "Clipboard doesn't contain usable text!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String equation = item.coerceToText(this).toString().replaceAll("[^A-Za-z0-9\\+\\(\\)\\[\\] ]", "");
            String[] eqnHS = equation.replaceAll("-+", "-").replaceAll("<+",">").replaceAll(">+",">").replaceAll("->", "=").replaceAll("→", "=").replaceAll("=+","=").split("=");
            if (eqnHS.length != 2) {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(this, "Invalid equation!", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            LHSEqn.setText(eqnHS[0]);
            RHSEqn.setText(eqnHS[1]);
        }
    }
}