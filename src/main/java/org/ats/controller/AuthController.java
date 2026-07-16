package org.ats.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.ats.dto.UserRequest;
import org.ats.entities.User;
import org.ats.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "views/auths/login";
    }

    @PostMapping("/login")
    public ModelAndView processLogin(@RequestParam(name = "email") String emailAddress,
                                     @RequestParam(name = "password") String password,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes
    ) {
        System.out.println("Email address : " + emailAddress + "; Password: " + password);

        ModelAndView mv = new ModelAndView();
        User user = authService.authenticate(new UserRequest(emailAddress, password));
        session.setAttribute("user", user);

        // Short session
        redirectAttributes.addFlashAttribute("message", "Successfully logged in");

        mv.setViewName("redirect:/jobs");

        return mv;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auths/login";
    }

}
