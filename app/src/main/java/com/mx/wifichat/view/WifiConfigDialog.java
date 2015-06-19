package com.mx.wifichat.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mx.wifichat.R;
import com.mx.wifichat.util.WifiUtil;

/**
 * Created by MX on 2015/6/17.
 */
public class WifiConfigDialog extends Dialog implements View.OnClickListener {
    private ScanResult mScanResult;
    private EditText edtPassword;
    private Button btnCancel;
    private Button btnOk;

    public WifiConfigDialog(final Context context, ScanResult scanResult) {
        super(context);
        this.mScanResult = scanResult;

        setTitle(scanResult.SSID);

        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_wifi_config, null);

        edtPassword = (EditText) contentView.findViewById(R.id.editText_dialog_wifi_config);
        btnCancel = (Button) contentView.findViewById(R.id.button_dialog_wifi_config_cancel);
        btnOk = (Button) contentView.findViewById(R.id.button_dialog_wifi_config_ok);

        setContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);


        /*setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case Dialog.BUTTON_NEGATIVE:
                        break;
                    case Dialog.BUTTON_POSITIVE:
                        String password = WifiConfigDialog.this.edtPassword.getText().toString();
                        if (password.length() >= 1) {
                            WifiUtil.connect(mScanResult, password);
                        }
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_dialog_wifi_config_cancel:
                this.cancel();
                break;
            case R.id.button_dialog_wifi_config_ok:
                String password = WifiConfigDialog.this.edtPassword.getText().toString();
                if (password.length() >= 8) {
                    WifiUtil.connect(mScanResult, password);
                    this.cancel();
                } else {
                    Toast.makeText(WifiConfigDialog.this.getContext(), "密码长度为8位以上", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
