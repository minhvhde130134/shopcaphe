package com.thodung.shopcaphe.controller;

import java.util.Set;

import com.thodung.shopcaphe.model.Address;
import com.thodung.shopcaphe.model.ShopcartItem;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.payload.ChangePasswordRequest;
import com.thodung.shopcaphe.payload.LoginRequest;
import com.thodung.shopcaphe.payload.LoginResponse;
import com.thodung.shopcaphe.security.TokenProvider;
import com.thodung.shopcaphe.security.UserPrincipal;
import com.thodung.shopcaphe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Xác thực thông tin người dùng Request lên
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User u = userPrincipal.getUser();
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.createToken(u);
        return new LoginResponse(jwt, u.getFullName(), u.getPhone(), u.getAvatarLink(), u.isEmailVerified(),
                u.getRole(), userService.setMainImgLinkItem(u.getShopcart()), u.getAddresses());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody LoginRequest signupRequest) {
        try {
            User u = userService.signup(signupRequest);
            String jwt = tokenProvider.createToken(u);
            LoginResponse lr = new LoginResponse(jwt, u.getFullName(), u.getPhone(), u.getAvatarLink(),
                    u.isEmailVerified(), u.getRole(), u.getShopcart(), u.getAddresses());
            return new ResponseEntity<LoginResponse>(lr, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUserInfo")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<User>(u, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/socialAsyncCart")
    public ResponseEntity<?> socialAsyncCart(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Set<ShopcartItem>>(userService.socialAsyncCart(u.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/socialAsyncAddress")
    public ResponseEntity<?> socialAsyncAddress(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Set<Address>>(userService.socialAsyncAddress(u.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String email) {
        try {
            userService.forgetPassword(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgetChangePassword")
    public ResponseEntity<?> forgetChangePassword(@RequestBody ChangePasswordRequest request) {
        try {
            User u = userService.forgetChangePassword(request);
            String jwt = tokenProvider.createToken(u);
            return new ResponseEntity<LoginResponse>(
                    new LoginResponse(jwt, u.getFullName(), u.getPhone(), u.getAvatarLink(), u.isEmailVerified(),
                            u.getRole(), userService.setMainImgLinkItem(u.getShopcart()), u.getAddresses()),
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sendVerify")
    public ResponseEntity<?> sendVerify(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<String>(userService.sendVerify(u), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(Authentication authentication, @RequestParam String code,
            @RequestParam String jwtCode) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            boolean isVerify = userService.verify(jwtCode, code, u);
            if (isVerify)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> getLikedProducts(@RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            userService.changePassword(u, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@RequestParam String fullName, @RequestParam(required = false) Long dob,
            @RequestParam(required = false) String phone, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<User>(userService.updateInfo(u, fullName, dob, phone), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestParam String email, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            userService.updateEmail(u, email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAddress")
    public ResponseEntity<?> getAllAddress(Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Set<Address>>(userService.getAllAddress(u.getId()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/removeAddress")
    public ResponseEntity<?> removeAddress(@RequestParam long id, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            userService.removeAddress(id, u.getId());
            return new ResponseEntity<String>("Xóa thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/setAddressDefault")
    public ResponseEntity<?> setAddressDefault(@RequestParam long id, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            userService.setAddressDefault(id, u.getId());
            return new ResponseEntity<String>("Đặt mặc định thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveAddress")
    public ResponseEntity<?> saveAddress(@RequestBody Address address, Authentication authentication) {
        try {
            UserPrincipal up = (UserPrincipal) authentication.getPrincipal();
            User u = up.getUser();
            return new ResponseEntity<Address>(userService.saveAddress(u, address), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}