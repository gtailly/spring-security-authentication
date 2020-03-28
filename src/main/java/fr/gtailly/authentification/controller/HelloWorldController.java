package fr.gtailly.authentification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello World Controller
 *
 * @author Grégory TAILLY
 */
@RestController
public class HelloWorldController {

    @GetMapping("hello")
    public String getHelloWorld() {
        return "Hello World";
    }
}
