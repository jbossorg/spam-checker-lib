/*
 * JBoss, Home of Professional Open Source
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 */
package org.jboss.labs.spamcheckerlib;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link SfsChecker}
 *
 * @author Vlastimil Elias (velias at redhat dot com)
 */
public class SfsCheckerTest {

    private static final String RESP_1 = "{\"success\":1,\"ip\":{\"lastseen\":\"2016-06-22 08:50:12\",\"frequency\" : 1,\"appears\" :true ,\"confidence\": 2.93}}";
    private static final String RESP_2 = "{\"success\":0}";;

    @Test
    public void getResponseFieldValue() {
        Assert.assertNull(SfsChecker.getResponseFieldValue(RESP_1, "unknown"));
        Assert.assertEquals("1", SfsChecker.getResponseFieldValue(RESP_1, "success"));
        Assert.assertEquals("2016-06-22 08:50:12", SfsChecker.getResponseFieldValue(RESP_1, "lastseen"));
        Assert.assertEquals("1", SfsChecker.getResponseFieldValue(RESP_1, "frequency"));
        Assert.assertEquals("true", SfsChecker.getResponseFieldValue(RESP_1, "appears"));
        Assert.assertEquals("2.93", SfsChecker.getResponseFieldValue(RESP_1, "confidence"));
    }

    @Test
    public void getResponseFieldValueBoolean() {
        Assert.assertEquals(false, SfsChecker.getResponseFieldValueBoolean(RESP_1, "inknown"));
        Assert.assertEquals(true, SfsChecker.getResponseFieldValueBoolean(RESP_1, "success"));
        Assert.assertEquals(true, SfsChecker.getResponseFieldValueBoolean(RESP_1, "appears"));

        Assert.assertEquals(false, SfsChecker.getResponseFieldValueBoolean(RESP_2, "success"));
    }

    @Test
    public void parseResponse_1() throws ParseException {
        SfsCheckResponse res = SfsChecker.parseResponse(RESP_1);
        Assert.assertEquals(true, res.isAppears());
        Assert.assertEquals(1466578212000L, res.getLastSeenDate());
        Assert.assertEquals(1, res.getFrequency());
        Assert.assertEquals(2.93f, res.getConfidence(), 0.5);
    }
    
    @Test
    public void parseResponse_2() throws ParseException {
        SfsCheckResponse res = SfsChecker.parseResponse(RESP_2);
        Assert.assertEquals(false, res.isAppears());
        Assert.assertEquals(0L, res.getLastSeenDate());
        Assert.assertEquals(0, res.getFrequency());
        Assert.assertEquals(0f, res.getConfidence(), 0.5);
    }

}

