package org.openobservatory.ooniprobe.common;

import android.content.Context;

import com.google.android.gms.common.util.ArrayUtils;

import org.openobservatory.engine.Engine;
import org.openobservatory.ooniprobe.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ly.count.android.sdk.Countly;
import ly.count.android.sdk.CountlyConfig;
import ly.count.android.sdk.DeviceId;
import ly.count.android.sdk.messaging.CountlyPush;

public class CountlyManager {
    private static String[] basicFeatures = new String[]{
            ly.count.android.sdk.Countly.CountlyFeatureNames.location,
    };

    private static String[] analyticsFeatures = new String[]{
            ly.count.android.sdk.Countly.CountlyFeatureNames.sessions,
            ly.count.android.sdk.Countly.CountlyFeatureNames.views,
            ly.count.android.sdk.Countly.CountlyFeatureNames.events
            //TODO evaluate scrolls, clicks, forms, attribution
    };

    private static String[] crashFeatures = new String[]{
            ly.count.android.sdk.Countly.CountlyFeatureNames.crashes,
    };

    private static String[] pushFeatures = new String[]{
            ly.count.android.sdk.Countly.CountlyFeatureNames.push,
    };

    public static void register(Context ctx, PreferenceManager preferenceManager){
        CountlyConfig config = new CountlyConfig()
                .setAppKey("146836f41172f9e3287cab6f2cc347de3f5ddf3b")
                .setContext(ctx)
                .setDeviceId(preferenceManager.getOrGenerateUUID4())
                .setRequiresConsent(true)
                .setConsentEnabled(getConsentsEnabled(preferenceManager))
                .setServerURL(BuildConfig.NOTIFICATION_SERVER)
                .setLoggingEnabled(!BuildConfig.DEBUG)
                .setViewTracking(true)
                .setHttpPostForced(true)
                .enableCrashReporting();
        Countly.sharedInstance().init(config);
    }

    public static String[] getConsentsEnabled(PreferenceManager preferenceManager) {
        List<String> consents = new ArrayList(Arrays.asList(basicFeatures));
        if (preferenceManager.isSendCrash())
            consents.addAll(Arrays.asList(crashFeatures));
        if (preferenceManager.isSendAnalytics())
            consents.addAll(Arrays.asList(analyticsFeatures));
        if (preferenceManager.isNotifications())
            consents.addAll(Arrays.asList(pushFeatures));
        return consents.toArray((new String[0]));
    }

    public static void reloadConsent(PreferenceManager preferenceManager){
        Countly.sharedInstance().consent().setConsent(crashFeatures, preferenceManager.isSendCrash());
        Countly.sharedInstance().consent().setConsent(analyticsFeatures, preferenceManager.isSendAnalytics());
        Countly.sharedInstance().consent().setConsent(pushFeatures, preferenceManager.isNotifications());
    }

    public static void recordEvent(String title) {
        Countly.sharedInstance().events().recordEvent(title);
    }

    public static void recordEvent(String title, HashMap<String, Object> segmentation) {
        Countly.sharedInstance().events().recordEvent(title, segmentation);
    }
}