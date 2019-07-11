package com.mir.lasalle_user_service.controller;

import java.util.Collection;

import com.mir.lasalle_user_service.controller.form.LasalleUserForm;
import com.mir.lasalle_user_service.model.LasalleUser;
import com.mir.lasalle_user_service.repository.LasalleUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    LasalleUserRepository uRepository;

    @Autowired
    public UserController(LasalleUserRepository uRepository) {
        this.uRepository = uRepository;
    }

    @GetMapping("/users")
    @ResponseBody
    public Collection<LasalleUser> findAllLasalleUsers() {
        return uRepository.findAll();
    }

    @GetMapping("/user/{email}")
    @ResponseBody
    public LasalleUser findOneLasalleUserByEmail(@PathVariable("email") String email) {
        return uRepository.findOneLasalleUserByEmail(email);
    }


    // Multi part form data(Web Application)
    // <form><input name="email"></form>

    // Rest Application -> JSON data
    // RequestBody maps JSON object to JAVA object given proper field variable

    @PostMapping("/user/create")
    @ResponseBody
    public LasalleUser createUser(@RequestBody LasalleUserForm lUserForm) {
        LasalleUser toSave = new LasalleUser();
        toSave.setEmail(lUserForm.getEmail());
        toSave.setPassword(lUserForm.getPassword());
        toSave.setName(lUserForm.getName());
        return uRepository.save(toSave);
    }
}
