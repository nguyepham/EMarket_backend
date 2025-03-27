package nguye.emarket.backend.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/api/v1/home")
    public String hello() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome to eMarket, " + username + ", " + userId + "!";
    }
}
