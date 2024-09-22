package project_management.Project_Management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeCobtroller {

     @GetMapping("/hello")
      public String hello(){
            return "Its working fine";
      }
}
