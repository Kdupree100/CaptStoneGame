package com.example.ACsecurity.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("Home")
public class LoginController {
   @RequestMapping({"/GambleGames"})   // either type '/' or index
   public String showlogin()
   {
       return "GambleGames";
   }
   @RequestMapping("/")
   public String simplemethod() {
       //mapped to hostname:port/home/
       return "login";
   }
   @RequestMapping("/showoff")
   public String showindex() {
       //mapped to hostname:port/home/showoff/
       return "Bingo75";
   }
   @RequestMapping("/Snake")
   public String showGame() {
       
       return "index1";
}
   @RequestMapping("/HangMan")
   public String showShip() {
       
       return "HangMan";
}
   
}