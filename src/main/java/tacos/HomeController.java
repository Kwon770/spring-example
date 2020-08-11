package tacos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Anotation to make spring generate bean instance
@Controller
public class HomeController {
    @GetMapping("/") // handle the web request what come from root "/"
    public String home() {
        return "home"; // Return View name
    }
}
