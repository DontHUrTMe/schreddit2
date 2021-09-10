package com.gyuri.reddit.schreddit;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class ApproveUser {
    @Value( "${schreddit.token.auth}" )
    private String tokenAuth="djZWWnI3OEs2R0xDWmhpREhRY2x1ZzpsT1hUUFdSWks0MzQ4Sy13RnczZGpDRUxwTEVZUkE=";
    @Value( "${schreddit.bot.password}" )
    private String botpass="schbot2021";
    @Value( "${schreddit.bot.uname}" )
    private String botuname="SCH_BoT2";
    public String approve(String uname) throws UnirestException {

        Unirest.setTimeouts(2000, 1500);
        HttpResponse<JsonNode> token = Unirest.post("https://www.reddit.com/api/v1/access_token")
                .header("user-agent", "ChangeMeClient/0.1 by SCH_BoT2")
                .header("Authorization", "Basic "+tokenAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("username", botuname)
                .field("password", botpass).asJson();

        if (token.getStatus()!=200)
        {
            //Bot authorization failed. Please contact a site administrator.
            return "Bot authorization failed. Please contact a site administrator.";
        }
        HttpResponse<JsonNode> response = Unirest.post("https://oauth.reddit.com/r/schonherz/api/friend?api_type=json&container=r/schonherz&name="+uname+"&type=contributor")
                .header("user-agent", "ChangeMeClient/0.1 by SCH_BoT2")
                .header("Authorization", "bearer "+token.getBody().getObject().getString("access_token")).asJson();

        if (response.getStatus()!=200)
        {
            //Error posting approve request. Please contact a site administrator.
            return "Error posting approve request. Please contact a site administrator.";
        }

        JSONArray errors = response.getBody().getObject().getJSONObject("json").getJSONArray("errors");

        if (errors.length()>0)
        {
            //Failed to approve user. +errorok
            return "Failed to approve user.";
        }
        else{
            return "Success!";
        }
    }
}
