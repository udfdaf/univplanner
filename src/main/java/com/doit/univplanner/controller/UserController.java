        package com.doit.univplanner.controller;

        import com.doit.univplanner.dto.UserDto;
        import com.doit.univplanner.entity.User;
        import com.doit.univplanner.exception.CustomException;
        import com.doit.univplanner.exception.ErrorCode;
        import com.doit.univplanner.service.UserService;
        import jakarta.validation.Valid;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

        @Controller
        @RequestMapping("/")
        public class UserController {
            @Autowired
            private UserService userService;

            @GetMapping
            public String home() {
                return "login";
            }

            @GetMapping("/login")
            public String login() {
                return "login";
            }

            @GetMapping("/signup")
            public String signup() {
                return "signup";
            }

            @PostMapping("/register")
            public String register(@Valid @ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes) {
                try {
                    userService.registerUser(userDto);
                    redirectAttributes.addFlashAttribute("message", "새로운 유저가 되신 것을 환영합니다!");
                    return "redirect:/login";
                } catch (CustomException e) {
                    redirectAttributes.addFlashAttribute("error", e.getMessage());
                    return "redirect:/signup";
                }
            }
        }