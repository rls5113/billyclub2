package com.billyclub.points.controller;

import com.billyclub.points.model.dto.RegistrationDto;
import com.billyclub.points.repository.RoleRepository;
import com.billyclub.points.repository.UserRepository;
import com.billyclub.points.security.jwt.JwtUtils;
import com.billyclub.points.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins="*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthControllerOriginal {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userService;

    public AuthControllerOriginal(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/signin")
    public String loginPage(){
        return "login.html";
    }
    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }

    @GetMapping("/signup")
    public String getRegisterForm(Model model) {
        RegistrationDto user = new RegistrationDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/signup/save")
    public String register(@Valid @ModelAttribute("user")RegistrationDto user,
                           BindingResult result, Model model) {
//        UserDetails existingUserEmail = userService.findByEmail(user.getEmail());
//        if(existingUserEmail != null && existingUserEmail. != null && !existingUserEmail.getEmail().isEmpty()) {
//            return "redirect:/register?fail";
//        }
        UserDetails existingUserUsername = userService.loadUserByUsername(user.getUsername());
        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
            return "redirect:/register?fail";
        }
        if(result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
//        userService.saveUser(user);
        return "redirect:/events-list?success";
    }
}
