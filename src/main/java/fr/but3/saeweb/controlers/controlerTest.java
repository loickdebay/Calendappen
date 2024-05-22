package fr.but3.saeweb.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
class controlerTest {
    @RequestMapping("/test")
    public String hello() {
    return "test";
    }
}
