package com.TimeVenture.controller.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewsController {

    @GetMapping("/reviews")
    public String reveiwsPage(){
        return "reviewTest";
    }
}
