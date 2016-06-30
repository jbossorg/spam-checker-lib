/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.jboss.labs.spamcheckerlib;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;

/**
 * Check IP address using Project Honey Pot <a href="https://www.projecthoneypot.org/services_overview.php">http:BL
 * service</a>.
 * <p>
 * Class is thread safe.
 *
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class HpChecker {

    private static String CHECK_URL = "dnsbl.httpbl.org";

    private String accessKey = "";

    /**
     * Create checker.
     * 
     * @param accessKey to use
     */
    public HpChecker(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * Check IP address.
     * 
     * @param ip address of client to check
     * @return response object
     */
    public HpCheckResponse check(String ip) {
        HpCheckResponse r = new HpCheckResponse();
        if (StringUtils.isNotEmpty(ip)) {
            String[] ia = ip.split("\\.");
            String revIp = ia[3] + "." + ia[2] + "." + ia[1] + "." + ia[0];
            String url = accessKey + "." + revIp + "." + CHECK_URL;
            try {
                InetAddress ria = InetAddress.getByName(url);
                String responseString = ria.toString().split("/")[1];
                String[] responseArray = responseString.split("\\.");
                r.setDaysLastActive(Integer.parseInt(responseArray[1]));
                r.setThreatScore(Integer.parseInt(responseArray[2]));
                r.setVisitorTypeCode(Integer.parseInt(responseArray[3]));
            } catch (UnknownHostException e) {
                // not listed by Project HoneyPot
            }
        }
        return r;
    }

}