package com.TimeVenture.controller.member;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SessionController {

    @GetMapping("/api/user")
    @ResponseBody
    public Map<String, Object> getUserInfo(HttpSession session) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("email", session.getAttribute("email"));
        userInfo.put("name", session.getAttribute("name"));
        return userInfo;
    }
}
