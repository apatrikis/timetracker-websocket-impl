/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prodyna.pac.timetracker.websocket;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author apatrikis
 */
public class JsonObjectTest {

    @Test
    public void testJsonObjectBuilder() {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();

        job.add("timestamp", new Date().getTime()).add("message", "the message");
        ret.add("event", "my custom event").add("messageBody", job);

        String str = ret.build().toString();
        Assert.assertTrue(str.contains("event"));
        Assert.assertTrue(str.contains("messageBody"));
    }

    @Test
    public void testJsonObjectReader() {
        String j = "{\"event\":\"my custom event\",\"messageBody\":{\"timestamp\":" + new Date().getTime() + ",\"message\":\"the message\"}}";
        JsonObject jo = Json.createReader(new StringReader(j)).readObject();

        Assert.assertTrue(jo.getString("event").equals("my custom event"));
        Assert.assertTrue(jo.getJsonObject("messageBody").getString("message").equals("the message"));
    }

    @Test
    public void testConvertToJSONDateFormat() {
        LocalDateTime now = LocalDateTime.now();
        String dtf = DateTimeFormatter.ISO_DATE_TIME.format(now);
        Assert.assertTrue(dtf.contains("T"));
    }
}
