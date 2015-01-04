package com.stak.smartlock.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stak.smartlock.R;
import com.stak.smartlock.rest.SmartLockClient;

import java.util.regex.Pattern;


public class ClientActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String SERVER_IP_PREF = "server.ip";
    public static final String TOKEN_PREF = "token";

    private SmartLockClient smartLockClient;

    private TextView statusTextView;

    private AlertDialog setIpDialog;
    private AlertDialog confirmRegistrationDialog;

    private Button openButton;
    private Button closeButton;

    public void init() {
        setIpDialog = createSetIpDialog();
        confirmRegistrationDialog = createConfirmRegistrationDialog();

        statusTextView = (TextView) findViewById(R.id.statusTextView);

        smartLockClient = new SmartLockClient(getPreferences(MODE_PRIVATE).getString(SERVER_IP_PREF, ""), 7777);

        openButton = (Button) findViewById(R.id.open_button);
        openButton.setOnClickListener(this);

        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_set_ip:
                setIpDialog.show();
                return true;
            case R.id.action_register:
                confirmRegistrationDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_button:
                onOpenButtonClick();
                break;
            case R.id.close_button:
                onCloseButtonClick();
                break;
        }
    }

    public void onOpenButtonClick() {
        if (smartLockClient.open(getPreferences(MODE_PRIVATE).getString(TOKEN_PREF, ""))) {
            statusTextView.setText("Open Success");
            statusTextView.setTextColor(Color.GREEN);
        } else {
            statusTextView.setText("Open Failure");
            statusTextView.setTextColor(Color.RED);
        }
    }

    public void onCloseButtonClick() {
        if (smartLockClient.close(getPreferences(MODE_PRIVATE).getString(TOKEN_PREF, ""))) {
            statusTextView.setText("Close Success");
            statusTextView.setTextColor(Color.GREEN);
        } else {
            statusTextView.setText("Close Failure");
            statusTextView.setTextColor(Color.RED);
        }
    }

    private AlertDialog createSetIpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ustaw IP Serwera");
        final EditText ipEditText = new EditText(this);
        builder.setView(ipEditText);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {
                ipEditText.setText(getPreferences(MODE_PRIVATE).getString(SERVER_IP_PREF, ""));
                ipEditText.setTextColor(Color.BLACK);
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ip = ipEditText.getText().toString().trim();
                        if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
                            getPreferences(MODE_PRIVATE).edit().putString(SERVER_IP_PREF, ip).commit();
                            smartLockClient.release();
                            smartLockClient = new SmartLockClient(ip, 7777);
                            dialog.dismiss();
                        } else {
                            ipEditText.setTextColor(Color.RED);
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private AlertDialog createConfirmRegistrationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText usernameEditText = new EditText(this);
        usernameEditText.setHint("Username");
        layout.addView(usernameEditText);

        final EditText pinEditText = new EditText(this);
        pinEditText.setHint("Pin");
        layout.addView(pinEditText);

        builder.setView(layout);

        builder.setTitle("Potwierdź rejestrację");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface d) {
                usernameEditText.getText().clear();
                usernameEditText.setTextColor(Color.BLACK);
                pinEditText.getText().clear();
                pinEditText.setTextColor(Color.BLACK);
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = usernameEditText.getText().toString().trim();
                        String pin = pinEditText.getText().toString().trim();
                        boolean validFlag = true;
                        if (!Pattern.compile("\\w+").matcher(username).matches()) {
                            usernameEditText.setTextColor(Color.RED);
                            validFlag = false;
                        }
                        if (!Pattern.compile("\\d\\d\\d\\d").matcher(pin).matches()) {
                            pinEditText.setTextColor(Color.RED);
                            validFlag = false;
                        }
                        if (validFlag) {
                            String token = smartLockClient.confirm(username, pin);
                            if (token != null) {
                                getPreferences(MODE_PRIVATE).edit().putString(TOKEN_PREF, token).commit();
                                dialog.dismiss();
                            }
                            usernameEditText.setTextColor(Color.RED);
                            pinEditText.setTextColor(Color.RED);
                        }
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    protected void onDestroy() {
        smartLockClient.release();
        super.onDestroy();
    }
}

