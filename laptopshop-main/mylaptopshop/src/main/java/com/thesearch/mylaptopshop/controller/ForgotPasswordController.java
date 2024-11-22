package com.thesearch.mylaptopshop.controller;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thesearch.mylaptopshop.dto.MailBody;
import com.thesearch.mylaptopshop.model.ForgotPassword;
import com.thesearch.mylaptopshop.model.User;
import com.thesearch.mylaptopshop.repository.ForgotPasswordRepository;
import com.thesearch.mylaptopshop.repository.UserRepository;
import com.thesearch.mylaptopshop.request.ChangePasswordRequest;
import com.thesearch.mylaptopshop.service.mail.MailService;


@RestController
@RequestMapping("${api.prefix}/forgotpassword")
@CrossOrigin(origins ="http://127.0.0.1:5500/")
public class ForgotPasswordController {
    private final MailService mailService;
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordController(MailService mailService, UserRepository userRepository
                                    ,ForgotPasswordRepository forgotPasswordRepository
                                    ,PasswordEncoder passwordEncoder) {
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyMail(@PathVariable String email){
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address: " + email);
        }
        Integer otp = otpGenerator();
        User user = Optional.ofNullable(userRepository.findByEmail(email))
            .orElseThrow(()-> new UsernameNotFoundException("This user doesn't exist!"));
            forgotPasswordRepository.findByUser(user).ifPresent(fp->forgotPasswordRepository.deleteById(fp.getForgotPasswordId()));
        System.out.println(email);
        MailBody mailBody = MailBody.builder()
            .to(email)
            .text("Otp to reset your password: "+ otp)
            .subject("Password Reset")
            .build();   
        System.out.print("still standing");
        ForgotPassword forgotPassword = ForgotPassword.builder()
            .otp(otp)
            .expirationTime(new Date(System.currentTimeMillis()+70*1009))
            .user(user)
            .build();
        System.out.println("rize");
        mailService.sendSimpleMessage(mailBody);
        System.out.println("so we doom");
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("Check your email for the otp!");
    }
    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email){
        User user = Optional.ofNullable(userRepository.findByEmail(email))
            .orElseThrow(()-> new UsernameNotFoundException("This user doesn't exist!"));
        ForgotPassword forgotPassword = forgotPasswordRepository.findByOtpAndUser(otp, user).orElseThrow(() -> new RuntimeException("Wrong otp"));
        
        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getForgotPasswordId());
            return new ResponseEntity<>("Too late! Try take another otp.",HttpStatus.EXPECTATION_FAILED);
        }
        
        
        return ResponseEntity.ok("Let reset your password!");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePasswordRequest changePasswordRequest
                                                        ,@PathVariable String email){
        if(!Objects.equals(changePasswordRequest.password(), changePasswordRequest.repeatPassword())){
            return new ResponseEntity<>("Password does not match!",HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.password());
        userRepository.updatePassword(email, encodedPassword);                                               
        return ResponseEntity.ok("Password changed!");

    }
    private Integer otpGenerator(){
        Random random = new Random();
        return random.nextInt(100000,999999);
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return email != null && email.matches(emailRegex);
    }
    
}
