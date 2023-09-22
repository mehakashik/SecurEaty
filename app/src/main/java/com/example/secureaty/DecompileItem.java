package com.example.secureaty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

enum Attack {
    Overlay,
    Doa,
    Metasploit
}

public class DecompileItem {
    public static int lastId = 10000;

    private int progress = 0;
    private Attack attack;
    private ProgressBar pb;
    private TextView pt;
    private Context context;
    public LinearLayout layoutItem;
    private SpannableString Danger = new SpannableString("Danger");

    public DecompileItem(Context context, String appName) {
        this.context = context;
        layoutItem = new LinearLayout(context);
        layoutItem.setOrientation(LinearLayout.VERTICAL);

        TextView appText = new TextView(context);
        appText.setText(appName);

        ConstraintLayout progressLayout = new ConstraintLayout(context);
//        progressLayout.setOrientation(LinearLayout.HORIZONTAL);
        pb = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        pb.setId(lastId++);
        ConstraintLayout.LayoutParams pbParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        pbParams.setMargins(30, 30, 30, 15);
        pb.setLayoutParams(pbParams);

        pt = new TextView(context);
        pt.setId(lastId++);
        pt.setText("Scanning...");

        progressLayout.addView(pb);
        progressLayout.addView(pt);

        ConstraintSet set = new ConstraintSet();
        set.clone(progressLayout);
        int[] chainIds = {pb.getId(), pt.getId()};
        float[] chainWeights = {5, 1};

        set.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                chainIds, chainWeights, ConstraintSet.CHAIN_SPREAD_INSIDE
        );

        set.applyTo(progressLayout);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //context.startActivity(new Intent(context, PopupInfo.class));
                popUp();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.RED);
            }
        };
        Danger.setSpan(clickableSpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        layoutItem.addView(appText);
        layoutItem.addView(progressLayout);
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
        updateViews();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        updateViews();
    }

    private void updateViews() {
        pb.setProgress(progress);
        if (progress == 100) {
            if (attack == null) {
                pt.setText("Clean");
                pt.setTextColor(Color.GREEN);
            } else {
                pt.setText(Danger);
                pt.setTextColor(Color.RED);
                pt.setMovementMethod(LinkMovementMethod.getInstance());
                pt.setHighlightColor(Color.TRANSPARENT);
            }
        }
    }

    private void popUp () {

        AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Details");
        ad.setMessage(getAttackDetails());
        ad.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    private String getAttackDetails() {
        switch (attack) {
            case Doa:
                return "Application has functionality to create and install other applications. This can potentially be a repacking attack (application forgery), or Denial of App attack.";
            case Metasploit:
                return "This application provides a backdoor for MSFVenom tool, which gives attacker full access to your activities on this device.";
            case Overlay:
                return "This application has functionality, that might track your actions, or trick you to accept dangerous permissions. This can potentially be an overlay attack (Cloak and Dagger)";
            default:
                return "";
        }
    }

//    public void onButtonShowPopupWindowClick(Context context, View view, String text) {
//        // inflate the layout of the popup window
//
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup, null);
//
//        // create the popup window
//        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        boolean focusable = true; // lets taps outside the popup also dismiss it
//        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//        TextView textView = (TextView)findViewById(R.id.popuptext);
//
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        // dismiss the popup window when touched
//        popupView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popupWindow.dismiss();
//                return true;
//            }
//        });
//    }

}

