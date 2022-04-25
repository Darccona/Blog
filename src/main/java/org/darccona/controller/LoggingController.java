package org.darccona.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Класса контроллера логирования
 */
@Controller
public class LoggingController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    /**
     * Перенаправляет на главную страницу при входе
     * @return перенаправляет на главную страницу
     */
    @RequestMapping("/")
    public String index() {
        return "redirect:http://localhost:8080/blog?new=1";
    }
}
