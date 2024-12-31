package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;

public class SettingsDialog extends Dialog {
    public static final String TAG = SettingsDialog.class.getName();
    public SettingsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);

        TextView appVersionTV = findViewById(R.id.tv_app_version);
        ImageButton closeIB = findViewById(R.id.ib_close);
        String appVersion;

        try {
            final PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
            appVersion = "unavailable";
        }
        appVersionTV.setText(appVersion);

        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
