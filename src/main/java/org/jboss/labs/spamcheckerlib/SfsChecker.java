/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.jboss.labs.spamcheckerlib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Check IP address using <a hef="http://www.stopforumspam.com">Stop Forum Spam</a> lists. *
 * <p>
 * Class is thread safe.
 *
 * 
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class SfsChecker {

    private static final String URL = "http://api.stopforumspam.org/api?f=json&ip=";
    private static final DateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SfsCheckResponse check(String ip) throws IOException {
        URL url = new URL(URL + ip);
        String sfs = IOUtils.toString((InputStream) url.getContent(), "UTF-8");
        return parseResponse(sfs);
    }

    protected static SfsCheckResponse parseResponse(String response) {

        
        if (!getResponseFieldValueBoolean(response, "success")) {
            return new SfsCheckResponse();
        }
        
        String lastseenS = StringUtils.trimToNull(getResponseFieldValue(response, "lastseen"));
        Date lastseen = null;
        if(lastseenS != null){
            try{
            lastseen = SDF.parse(lastseenS);
            } catch (ParseException e){
                throw new RuntimeException("lastseen date format is invalid" + e.getMessage(), e);
            }
        }
        boolean appears = getResponseFieldValueBoolean(response, "appears");
        
        String confidenceS = StringUtils.trimToNull(getResponseFieldValue(response, "confidence"));
        float confidence = 0;
        if(confidenceS != null){
            confidence = Float.parseFloat(confidenceS);
        }
        int frequency = 0;
        String frequencyS = StringUtils.trimToNull(getResponseFieldValue(response, "frequency"));
        if(frequencyS != null){
            frequency = Integer.parseInt(frequencyS);
        }
        
        return new SfsCheckResponse(appears, confidence, lastseen!=null?lastseen.getTime():0,frequency); 
    }

    
    protected static boolean getResponseFieldValueBoolean(String source, String field) {
        String success = getResponseFieldValue(source, field);
        return ("1".equals(success) || "true".equalsIgnoreCase(success));
    }
    
    protected static String getResponseFieldValue(String source, String field) {
        
        if(source==null)
            return null;

        // find begin of field
        int i = source.indexOf(field);
        if (i == -1)
            return null;
        source = source.substring(i);

        // find end of field
        int s1 = source.indexOf(",");
        int s2 = source.indexOf("}");

        i = s1;
        if (i == -1 || i > s2)
            i = s2;

        // no field end
        if (i == -1) {
            return null;
        }
        source = source.substring(0, i);

        // find : and get value part
        i = source.indexOf(":");
        if (i == -1)
            return null;
        source = source.substring(i + 1);

        source = StringUtils.trimToEmpty(source);

        // remove " from value part
        source = source.replaceAll("\"", "");

        return source;

    }

}
