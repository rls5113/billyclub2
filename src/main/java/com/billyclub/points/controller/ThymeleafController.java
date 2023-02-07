package com.billyclub.points.controller;

import com.billyclub.points.model.ERole;
import com.billyclub.points.model.RefreshToken;
import com.billyclub.points.model.Role;
import com.billyclub.points.model.UserEntity;
import com.billyclub.points.payload.request.LoginRequest;
import com.billyclub.points.payload.request.SignupRequest;
import com.billyclub.points.payload.response.JwtResponse;
import com.billyclub.points.repository.RoleRepository;
import com.billyclub.points.repository.UserRepository;
import com.billyclub.points.security.jwt.JwtUtils;
import com.billyclub.points.security.services.RefreshTokenService;
import com.billyclub.points.security.services.UserDetailsImpl;
import com.billyclub.points.security.services.UserDetailsServiceImpl;
import com.billyclub.points.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//@CrossOrigin(origins="*", maxAge = 3600)
@Controller
@RequestMapping("/view")
public class ThymeleafController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    EventServiceImpl eventService;

//    private final UserDetailsServiceImpl userService;

//    public ThymeleafController(UserDetailsServiceImpl userService) {
//        this.userService = userService;
//    }


    @GetMapping("/index")
    public String getIndex() {return "index";}
//    @GetMapping("/signin")
//    public String loginPage(){
//        return "login.html";
//    }
    @GetMapping("/layout")
    public String layoutPage(){
        return "layout";
    }
    @GetMapping("/profile")
    public String profilePage(){
        return "profile";
    }
    @GetMapping("/events-list")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','USER','ADMIN')")
    public ModelAndView eventsListPage(Model model){
        ModelAndView eventsPage = new ModelAndView("events-list");
        eventsPage.addObject("events", eventService.findAll());
        return eventsPage;
    }

    @GetMapping("/auth/signup")
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new SignupRequest());
        return "register";
    }

    @GetMapping("/auth/signin")
    public String getLoginForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", new LoginRequest());
            return "login";
        }
//        LoginRequest user = new LoginRequest();
//        model.addAttribute("user", user);
        return "redirect:/view/index";
    }

    @PostMapping("/auth/signup/save")
    public String register(@Valid @ModelAttribute("user") SignupRequest signupRequest,
                           BindingResult result, Model model) {
        // Create new user's account
        UserEntity user = new UserEntity(signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()));

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return "redirect:/auth/signup?fail";
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return "redirect:/auth/signup?fail";
        }

        if(result.hasErrors()){
            model.addAttribute("user",user);
        }

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        UserEntity newUser = userRepository.save(user);

//        model.addAttribute("user",newUser);
        return "redirect:/view/auth/signin?signupsuccess";
    }
    @PostMapping("/auth/signin/action")
    public String authenticateUser(@Valid @ModelAttribute("user") LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return "redirect:/view/events-list";

//        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
//                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

}
