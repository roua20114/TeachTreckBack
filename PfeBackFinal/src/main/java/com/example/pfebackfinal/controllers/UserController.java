package com.example.pfebackfinal.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-actions")
@PreAuthorize("hasAuthority('USER')")

public class UserController {


}
