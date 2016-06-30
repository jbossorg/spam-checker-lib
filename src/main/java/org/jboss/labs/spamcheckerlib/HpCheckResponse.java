/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.jboss.labs.spamcheckerlib;

/**
 * Object used as response from {@link HpChecker} check method. For meaning of distinct fields see Project Honey Pot
 * <a href="https://www.projecthoneypot.org/services_overview.php">http:BL service documentation</a>.
 *
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class HpCheckResponse {

    public static enum VisitorType {
        NOT_LISTED(-1, "Not listed by Project HoneyPot"), SEARCH_ENGINE(0, "Search Engine"), SUSPICIOUS(1, "Suspicious"), HARVESTER(2, "Harvester"), SUSPICIOUS_HARVESTER(3, "Suspicious & Harvester"), COMMENT_SPAMMER(4, "Comment Spammer"),
        SUSPICIOUS_COMMENT_SPAMMER(5, "Suspicious & Comment Spammer"), HARVERTER_COMMENT_SPAMMER(6, "Harvester & Comment Spammer"), SUSPICIOUS_HARVERTER_COMMENT_SPAMMER(7, "Suspicious & Harvester & Comment Spammer"),
        UNDEFINED(8, "Indefined");

        int code;
        String name;

        private VisitorType(int code, String name) {
            this.code = code;
            this.name = name;
        }

        static VisitorType find(int visitorTypeCode) {
            for (VisitorType vt : VisitorType.values()) {
                if (vt.code == visitorTypeCode) {
                    return vt;
                }
            }
            return UNDEFINED;
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }

    }

    private int visitorTypeCode = -1;
    private VisitorType visitorType = VisitorType.NOT_LISTED;
    private int daysSinceLastActivity = -1;
    private int threatScore = -1;

    public void setDaysLastActive(int daysLastActive) {
        this.daysSinceLastActivity = daysLastActive;
    }

    public void setVisitorTypeCode(int visitorTypeCode) {
        this.visitorTypeCode = visitorTypeCode;
        visitorType = VisitorType.find(visitorTypeCode);
    }

    public void setThreatScore(int threatScore) {
        this.threatScore = threatScore;
    }

    public int getDaysLastActive() {
        return daysSinceLastActivity;
    }

    public int getVisitorTypeCode() {
        return visitorTypeCode;
    }

    public int getThreatScore() {
        return threatScore;
    }

    public VisitorType getVisitorType() {
        return visitorType;
    }

    @Override
    public String toString() {
        return "HoneyPotResponse [visitorType=" + visitorType.getName() + ", visitorTypeCode=" + visitorTypeCode + ", threatScore=" + threatScore + ", daysLastActive=" + daysSinceLastActivity + "]";
    }

}