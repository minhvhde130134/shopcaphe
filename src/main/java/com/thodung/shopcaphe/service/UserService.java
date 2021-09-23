package com.thodung.shopcaphe.service;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

import com.thodung.shopcaphe.model.Address;
import com.thodung.shopcaphe.model.Image;
import com.thodung.shopcaphe.model.Product;
import com.thodung.shopcaphe.model.ShopcartItem;
import com.thodung.shopcaphe.model.User;
import com.thodung.shopcaphe.payload.ChangePasswordRequest;
import com.thodung.shopcaphe.payload.LoginRequest;
import com.thodung.shopcaphe.repository.AddressRepository;
import com.thodung.shopcaphe.repository.ShopcartItemRepository;
import com.thodung.shopcaphe.repository.UserRepository;
import com.thodung.shopcaphe.util.HelplerUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ShopcartItemRepository shopcartItemRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private HelplerUtils helper;

    public void changePassword(User u, ChangePasswordRequest request) throws Exception {
        if (passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            u.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(u);
        } else {
            throw new Exception("Sai mật khẩu");
        }
    }

    public Set<Address> getAllAddress(long id) {
        return addressRepository.findByUserId(id);
    }

    public Address saveAddress(User u, Address address) throws Exception {
        if (address.getId() != 0) {
            Address a = addressRepository.findById(address.getId()).get();
            if (u.getId() == a.getUser().getId()) {
                address.setUser(u);
                address.setMainAddress(a.isMainAddress());
                return addressRepository.save(address);
            } else {
                throw new Exception("VL hacker à!!!");
            }
        } else {
            Set<Address> all = addressRepository.findByUserId(u.getId());
            if (all.isEmpty()) {
                address.setMainAddress(true);
            }
            address.setUser(u);
            return addressRepository.save(address);
        }
    }

    public void removeAddress(long id, long userId) throws Exception {
        Address a = addressRepository.getById(id);
        if (a.getUser().getId() == userId && !a.isMainAddress()) {
            addressRepository.delete(a);
        } else {
            throw new Exception("Có lỗi xảy ra!!");
        }
    }

    public User signup(LoginRequest signupRequest) throws Exception {
        String email = signupRequest.getEmail();
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            throw new Exception("Email đã được sử dụng");
        } else {
            User user = new User(passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getFullName(), email);
            return userRepository.save(user);
        }
    }

    public String sendVerify(User u) {
        String code = helper.sendEmail(u.getEmail(), "Xác Thực Tài Khoản", "Mã xác thực của bạn là: ");
        return passwordEncoder.encode(code);
    }

    public boolean verify(String jwtCode, String code, User u) {
        if (passwordEncoder.matches(code, jwtCode)) {
            u.setEmailVerified(true);
            userRepository.save(u);
            return true;
        }
        return false;
    }

    public Set<ShopcartItem> socialAsyncCart(long id) {
        Set<ShopcartItem> ssi = shopcartItemRepository.findByUserId(id);
        return setMainImgLinkItem(ssi);
    }

    public Set<ShopcartItem> setMainImgLinkItem(Set<ShopcartItem> ssi) {
        for (ShopcartItem item : ssi) {
            Product p = item.getProduct();
            Set<Image> set = p.getListImage();
            for (Image image : set) {
                if (image.isMainImg()) {
                    p.setMainImgLink(image.getImgLink());
                }
            }
            item.setProduct(p);
        }
        return ssi;
    }

    public Set<Address> socialAsyncAddress(long id) {
        return addressRepository.findByUserId(id);
    }

    public void setAddressDefault(long id, long userId) throws Exception {
        Set<Address> sa = addressRepository.findByUserId(userId);
        for (Address address : sa) {
            if (address.isMainAddress()) {
                address.setMainAddress(false);
            }
            if (address.getId() == id && address.getUser().getId() == userId) {
                address.setMainAddress(true);
            }
        }
        addressRepository.saveAll(sa);
    }

    public User getUserInfo(long id) {
        return userRepository.getInfoById(id);
    }

    public User updateInfo(User u, String fullName, Long dob, String phone) {
        if (dob != null) {
            u.setDob(new Date(dob));
        }
        if (phone != null) {
            u.setPhone(phone);
        }
        u.setFullName(fullName);
        return userRepository.save(u);
    }

    public void updateEmail(User u, String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new Exception("Email đã tồn tại");
        }
        u.setEmail(email);
        u.setEmailVerified(false);
        userRepository.save(u);
    }

    public void forgetPassword(String email) throws Exception {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            String code = helper.sendEmail(email, "Thay đổi mật khẩu", "Mật khẩu mới của bạn là: ");
            User user = u.get();
            user.setPassword(passwordEncoder.encode(code));
            userRepository.save(user);
        }else{
            throw new Exception("Email không tồn tại!");
        }
    }

    public User forgetChangePassword(ChangePasswordRequest request) {
        User u = userRepository.findByEmail(request.getEmail()).get();
        if (passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            u.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        return userRepository.save(u);
    }

}