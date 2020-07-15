package com.rnheartbeat;

import android.content.Intent;
import android.content.Context;

import android.os.PowerManager;
import android.os.Build;
import android.app.KeyguardManager;

import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import javax.annotation.Nonnull;


public class LockDetectorModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "LockDetector";
    private static ReactApplicationContext reactContext;

    private static final String E_LAYOUT_ERROR = "LOCK_ERROR";

    public LockDetectorModule(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Nonnull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
  public void measureLayout(Promise promise) {

    boolean isLocked = false;

    // First we check the locked state
    KeyguardManager keyguardManager = (KeyguardManager) getReactApplicationContext().getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
    boolean inKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode();

    if (inKeyguardRestrictedInputMode) {
        isLocked = true;
    } else {
        // If password is not set in the settings, the inKeyguardRestrictedInputMode() returns false,
        // so we need to check if screen on for this case

        PowerManager powerManager = (PowerManager)getReactApplicationContext().getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isLocked = !powerManager.isInteractive();
        } else {
            //noinspection deprecation
            isLocked = !powerManager.isScreenOn();
        }
    }

    promise.resolve(isLocked);

//     try {
//       KeyguardManager myKM = (KeyguardManager)  getReactApplicationContext().getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);

//       boolean isPhoneLocked = myKM.inKeyguardRestrictedInputMode();
        
//       promise.resolve(isPhoneLocked);
//     } catch (IllegalViewOperationException e) {
//       promise.reject(E_LAYOUT_ERROR, e);
//     }
  }
}
