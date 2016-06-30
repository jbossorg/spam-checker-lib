/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.jboss.labs.spamcheckerlib;

/**
 * Object used as response from {@link SfsChecker} check method.
 *
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class SfsCheckResponse {

    /**
     * Appears IP in database?
     */
    private boolean appears;

    /**
     * Statistical confidence it is a spammer (percent)
     */
    private float confidence;

    /**
     * Date of last seen spamming activity
     */
    private long lastSeenDate;

    /**
     * Number of times it appears in database
     */
    private int frequency;

    public SfsCheckResponse() {

    }

    public SfsCheckResponse(boolean appears, float confidence, long lastSeenDate, int frequency) {
        super();
        this.appears = appears;
        this.confidence = confidence;
        this.lastSeenDate = lastSeenDate;
        this.frequency = frequency;
    }

    public boolean isAppears() {
        return appears;
    }

    public float getConfidence() {
        return confidence;
    }

    public long getLastSeenDate() {
        return lastSeenDate;
    }

    public int getFrequency() {
        return frequency;
    }
    
    public long getDaysLastActive(){
        if(lastSeenDate == 0)
            return -1;
        else 
            return (System.currentTimeMillis() - lastSeenDate)/(24 * 60* 60* 1000L);
    }

    @Override
    public String toString() {
        return "SfsResponse [appears=" + appears + ", confidence=" + confidence + ", lastSeenDate=" + lastSeenDate + ", daysLastActive=" + getDaysLastActive() + ", frequency=" + frequency + "]";
    }

}
