package com.gyuri.reddit.schreddit;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@SuppressWarnings("FieldMayBeFinal")
@Service
public class ApproveUser {
    @Value("${schreddit.token.auth:}")
    private String tokenAuth;

    @Value("${schreddit.bot.password:}")
    private String botpass;

    @Value("${schreddit.bot.uname:}")
    private String botuname;

    @Value("${schreddit.client.token}")
    private  String clientToken;

    public String approve(String uname) throws UnirestException {

        Unirest.setTimeouts(2000, 1500);

        HttpRequest request = Unirest.post("https://www.reddit.com/api/v1/access_token")
                .header("user-agent", "schreddit/0.1 by SCH_BoT2")
                .header("Authorization", "Basic " + tokenAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("username", botuname)
                .field("password", botpass).getHttpRequest();

        HttpResponse<JsonNode> token = Unirest.post("https://www.reddit.com/api/v1/access_token")
                .header("user-agent", "schreddit/0.1 by SCH_BoT2")
                .header("Authorization", "Basic " + tokenAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("username", botuname)
                .field("password", botpass).asJson();

        if (token.getStatus() != 200) {
            //Bot authorization failed. Please contact a site administrator.
            return "Bot authorization failed. Please contact a site administrator.";
        }
        HttpResponse<JsonNode> response = Unirest.post("https://oauth.reddit.com/r/schonherz/api/friend?api_type=json&container=r/schonherz&name=" + uname + "&type=contributor")
                .header("user-agent", "schreddit/0.1 by SCH_BoT2")
                .header("Authorization", "bearer " + token.getBody().getObject().getString("access_token")).asJson();

        if (response.getStatus() != 200) {
            //Error posting approve request. Please contact a site administrator.
            return "Error posting approve request. Please contact a site administrator.";
        }

        JSONArray errors = response.getBody().getObject().getJSONObject("json").getJSONArray("errors");

        if (errors.length() > 0) {
            //Failed to approve user. + errorok
            return "Failed to approve user.";
        } else {
            return "Success! Now you can reach the subreddit at: 'https://reddit.com/r/schonherz' ";
        }
    }

    public String codeToToken(String code) throws UnirestException {
        Unirest.setTimeouts(2000, 1500);

        HttpRequest request = Unirest.post("https://www.reddit.com/api/v1/access_token")
                .header("Authorization", "Basic "+clientToken)
                .header("user-agent", "ChangeMeClient/0.1 by gyorik")
                .field("grant_type", "authorization_code")
                .field("code", code)
                .field("redirect_uri", "http://localhost:80/approve").getHttpRequest();

        HttpResponse<JsonNode> response = Unirest.post("https://www.reddit.com/api/v1/access_token")
                .header("Authorization", "Basic "+clientToken)
                .header("user-agent", "ChangeMeClient/0.1 by gyorik")
                .field("grant_type", "authorization_code")
                .field("code", code)
                .field("redirect_uri", "http://localhost:80/approve").asJson();
        return response.getBody().getObject().getString("access_token");
    }

    public String getUname(String token) throws UnirestException {
        GetRequest response = Unirest.get("https://oauth.reddit.com/api/v1/me")
                .header("Authorization", "bearer "+token)
                .header("user-agent", "ChangeMeClient/0.1 by gyorik");
        return response.asJson().getBody().getObject().getString("name");
    }
}
