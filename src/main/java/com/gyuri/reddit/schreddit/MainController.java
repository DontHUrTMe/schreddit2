package com.gyuri.reddit.schreddit;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MainController {

    @Autowired
    private ApproveUser approveUser;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/approve")
    public String approve(Model model) {
        model.addAttribute("rUname", new RUname());
        return "approve";
    }

    @GetMapping("/call")
    public String call(@ModelAttribute RUname user, Model model) throws UnirestException {
        String response = approveUser.approve(user.getUname());
        model.addAttribute("response", response);
        return "call";
    }

}