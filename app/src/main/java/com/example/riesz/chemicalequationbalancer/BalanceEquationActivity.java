package com.example.riesz.chemicalequationbalancer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BalanceEquationActivity extends AppCompatActivity {

    private Toast copyToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_equation);

        Intent intent = getIntent();
        String equation = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.balanced_equation);
        textView.setText(equation);
        textView.setTextIsSelectable(true);
        textView.setTextColor(Color.BLACK);
    }

    public void copyEqn(View view) {
        String equation = ((TextView) findViewById(R.id.balanced_equation)).getText().toString();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("balanced equation", equation);
        clipboard.setPrimaryClip(clip);
        if (copyToast != null){
            copyToast.cancel();
        }
        copyToast = Toast.makeText(this, "Equation Copied", Toast.LENGTH_SHORT);
        copyToast.show();
    }
}
