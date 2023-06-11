package org.greenway.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.greenway.backend.model.User;
import org.greenway.backend.service.RegistrationService;
import org.greenway.backend.service.UserService;
import org.greenway.backend.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService, AuthenticationManager authenticationManager, UserService userService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ApiResponse> registration(@RequestBody User user, BindingResult bindingResult){
        userValidator.validate(user,bindingResult);
        if (bindingResult.hasErrors()){
            return new ResponseEntity<>(
                    new ApiResponse(false,"this email taken"),HttpStatus.CONFLICT);
        }
        registrationService.register(user);
        return new ResponseEntity<>(
                new ApiResponse(true,"registered"),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginApiResponse> login(@RequestBody User user, HttpServletRequest request) throws Exception {

        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        Authentication auth = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        User foundUser = userService.findUserByEmail(user.getEmail());
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("name",foundUser.getName());
        userInfo.put("email",foundUser.getEmail());
        userInfo.put("phoneNumber",foundUser.getPhoneNumber());
        userInfo.put("role",foundUser.getRole());
        return new ResponseEntity<>(
                new LoginApiResponse("ok","Logged in",authReq.getPrincipal().toString(),userInfo),HttpStatus.OK);
    }
    //USER
    @GetMapping("/account")
    public ResponseEntity<ApiResponse> dashboard(){
        return new ResponseEntity<>(new ApiResponse(true,"User's account"),HttpStatus.OK);
    }
    //ADMIN
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> admin(){
        return new ResponseEntity<>(new ApiResponse(true,"Admin's account"),HttpStatus.OK);
    }
}
