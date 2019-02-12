package com.wowapp.contactslist.domain.models;
// Constants


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AnnotationStatus {
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String BUSY = "busy";
    public static final String AWAY = "away";
    public static final String CALL = "callforwarding";

    // Declare the @ StringDef for these constants:
    @StringDef({ONLINE, OFFLINE, BUSY, AWAY, CALL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
    }
}
