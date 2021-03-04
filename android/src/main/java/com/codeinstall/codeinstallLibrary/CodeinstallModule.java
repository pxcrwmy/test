package com.codeinstall.codeinstallLibrary;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.project.codeinstallsdk.CodeInstall;
import com.project.codeinstallsdk.inter.ConfigCallBack;
import com.project.codeinstallsdk.inter.ReceiptCallBack;
import com.project.codeinstallsdk.model.ConfigInfo;
import com.project.codeinstallsdk.model.Transfer;

public class CodeinstallModule extends ReactContextBaseJavaModule {

    private static String TAG = "CodeinstallModule";

    ReactContext context;

    public CodeinstallModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
        reactContext.addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

            }

            @Override
            public void onNewIntent(Intent intent) {
                Log.d(CodeinstallModule.TAG, "onNewIntent");
                getWakeUp(intent, null);
            }
        });
    }

    @ReactMethod
    public void getWakeUp(final Callback successBack) {
        Log.d(CodeinstallModule.TAG, "getWakeUp");
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            Intent intent = currentActivity.getIntent();
            getWakeUp(intent, successBack);
        }

    }

    private void getWakeUp(Intent intent, final Callback callback) {

        //一键唤起相关
        CodeInstall.getWakeUp(intent, new ReceiptCallBack() {
            @Override
            public void onResponse(Transfer transfer) {

                String channelNo= "";
                String data = "";

                if(transfer != null) {
                    try {
                        Log.d(CodeinstallModule.TAG, "getWakeUp : wakeupData = " + transfer.toString());

                        WritableMap params = Arguments.createMap();
                        params.putString("channel", transfer.getChannelNo());
                        params.putString("data", transfer.getPbData());

                        channelNo = transfer.getChannelNo();
                        data =  transfer.getPbData();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                WritableMap params = Arguments.createMap();
                params.putString("channel", channelNo);
                params.putString("data", data);

                if (callback == null) {
                    getReactApplicationContext()
                            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                            .emit("CodeinstallWakeupCallBack", params);
                } else {
                    callback.invoke(params);
                }
            }
        });
    }

    @Override
    public void initialize() {
        super.initialize();
        CodeInstall.init(context);
        CodeInstall.statistics();
    }

    @Override
    public String getName() {
        return "CodeinstallModule";
    }

    @ReactMethod
    public void getInstall(final Callback callback) {
        Log.d(CodeinstallModule.TAG, "getInstall");

        CodeInstall.getInstall(new ReceiptCallBack() {
            @Override
            public void onResponse(Transfer transfer) {

                String channelNo= "";
                String data = "";

                if(transfer != null) {
                    try {

                        Log.d(CodeinstallModule.TAG, "getInstall : data = " + transfer.toString());

                        channelNo = transfer.getChannelNo();
                        data =  transfer.getPbData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                WritableMap params = Arguments.createMap();
                params.putString("channel", channelNo);
                params.putString("data", data);

                if(callback != null) {
                    callback.invoke(params);
                }
            }
        });
    }

    @ReactMethod
    public void reportRegister() {
        Log.d(CodeinstallModule.TAG, "reportRegister");
        CodeInstall.register();
    }
}
