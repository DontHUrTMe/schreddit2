package com.gyuri.reddit.schreddit;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Controller
@EnableWebMvc
public class MainController extends WebMvcConfigurerAdapter {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/approve")
    public String approve(Model model) {
        model.addAttribute("rUname", new rUname());
        return "approve";
    }

    @GetMapping("/call")
    public String call(@ModelAttribute rUname user, Model model) throws UnirestException {
        ApproveUser approveUser = new ApproveUser();
        String response = approveUser.approve(user.getUname());
        model.addAttribute("response", response);
        return "call";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webfonts/**",
                "/images/**",
                "/css/**",
                "/js/**",
                "/sass/**")
                .addResourceLocations(
                        "classpath:/static/webfonts/",
                        "classpath:/static/images/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/sass/");
    }
}