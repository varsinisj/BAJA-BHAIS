package com.example.Baja.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        Object role = session.getAttribute("role");
        if (role == null) {
            // User not logged in — redirect to login page
            return "redirect:/login";
        }
        // Forward to static HTML page in src/main/resources/static/dashboard.html
        return "forward:/dashboard.html";
    }
}
