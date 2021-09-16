package com.gyuri.reddit.schreddit;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.Charset;
import java.util.Random;

@Controller
public class MainController {

    private String state() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }

    @Autowired
    private ApproveUser approveUser;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/approve")
    public String approve(@RequestParam String code, Model model) throws UnirestException {
        RUname rUname = new RUname();
        String token = approveUser.codeToToken(code);
        String uname = approveUser.getUname(token);
        rUname.setUname(uname);
        rUname.setToken(token);
        model.addAttribute("rUname", rUname);
        return "approve";
    }

    @GetMapping("/auth")
    public RedirectView authreddit(Model model) {
        return new RedirectView("https://www.reddit.com/api/v1/authorize?client_id=h0joy84RiHJePyZvfaEG9w&response_type=code&state="+state()+"&redirect_uri=https://r.sch.bme.hu/approve&duration=temporary&scope=identity");
    }


    @GetMapping("/call")
    public String call(@ModelAttribute RUname user, Model model) throws Exception {
        String uname = approveUser.getUname(user.getToken());
        if(!uname.equals(user.getUname()))
        {
            throw new Exception("WTF Dude?!?!");
        }
        String response = approveUser.approve(uname);
        model.addAttribute("response", response);
        return "call";
    }

}