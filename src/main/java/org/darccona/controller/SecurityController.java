package org.darccona.controller;

import org.darccona.config.EmailService;
import org.darccona.database.entity.ConfirmationUserEntity;
import org.darccona.database.entity.LinkEmailEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.ConfirmationUserRepository;
import org.darccona.database.repository.LinkEmailRepository;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.String2Model;
import org.darccona.model.RegModel;
import org.darccona.model.StringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.Random;
import java.util.zip.ZipEntry;


@Controller
public class SecurityController {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ConfirmationUserRepository confirmationUserRepository;
    @Autowired
    LinkEmailRepository linkEmailRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/registration")
    public String formReg(Model model) {
        model.addAttribute("divError", false);
        model.addAttribute("reg",  new RegModel());

        return "registration";
    }

    private String getLink(String name) {
        Random random = new Random();
        Date date = new Date();

        long time = date.getTime();
        for (int i=0; i<name.length(); i++) {
            time += (int) name.charAt(i);
        }
        time += random.nextInt(10000);

        return Long.toHexString(time);
    }

    private String getCode() {
        Random random = new Random();
        String code = "";

        for(int i=0; i<5; i++) {
            code += random.nextInt(10);
        }

        return code;
    }

    private void regEmail(String name, String email) {

        ConfirmationUserEntity confirmation = confirmationUserRepository.findByName(name);
        String link = getLink(name);
        String code = getCode();

        if (confirmation != null) {
            confirmation.setLink(link);
            confirmation.setCode(code);
            confirmation.setDate();
        } else {
            confirmation = new ConfirmationUserEntity(name, email, link, code);
        }
        confirmationUserRepository.save(confirmation);

        String str = "Здравстуйте, " + name + ". Вы отправили запрос на регистрацию на сайте blog? " +
                "Кто-то (надеемся, что вы) попросил нас зарегистрировать аккаунт на вашу почту. " +
                "Чтобы сделать это, пройдите по ссылке http://localhost:8080/blog/confirmationUser?id=" + link +
                ", или введите код " + code +
                " на сайте, во вкладке настроек, подтверждение аккаунта. " +
                "Если вы не запрашивали регистрацию, игнорируйте это сообщение!";

        try {
            emailService.sendSimpleEmail(email, "Подтверждение аккаунта Blog", str);
            LOG.info("Письмо для подтверждения аакаунта пользователя "+name+" отправлено на почту "+email);
        } catch (MailException mailException) {
            LOG.error("Ошибка при отправке письма..{}", mailException.getStackTrace());
        }
    }

    private void newPassword(String name, String email) {
        LinkEmailEntity linkEmail = linkEmailRepository.findByName(name);
        String link = getLink(name);

        if (linkEmail != null) {
            linkEmail.setLink(link);
        } else {
            linkEmail = new LinkEmailEntity(name, link);
        }
        linkEmailRepository.save(linkEmail);

        String str = "Здравстуйте, " + name + ". Вы отправили запрос на смену пароля на сайте blog? " +
                "Кто-то (надеемся, что вы) попросил нас отправить ссылку для смены пароля на вашу почту. " +
                "Чтобы сделать это, пройдите по ссылке http://localhost:8080/blog/newPassword/email?id=" + link +
                ". Если вы не запрашивали смену пароля, игнорируйте это сообщение!";

        try {
            emailService.sendSimpleEmail(email, "Смена пароля Blog", str);
            LOG.info("Письмо для смены пароля пользователя "+name+" отправлено на почту "+email);
        } catch (MailException mailException) {
            LOG.error("Ошибка при отправке письма..{}", mailException.getStackTrace());
        }
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("reg") RegModel reg, Model model) {
        UserEntity user = userRepository.findByName(reg.getName());

        if (user == null) {
            if ((reg.getName().equals("") || reg.getPassword1().equals("") || reg.getPassword2().equals(""))) {
                model.addAttribute("divError", true);
                model.addAttribute("error", "Все поля должны быть заполнены");
                model.addAttribute("reg", new RegModel());
                return "registration";
            }

            if (!reg.getPassword1().equals(reg.getPassword2())) {
                model.addAttribute("divError", true);
                model.addAttribute("error", "Пароли не совпадают");
                model.addAttribute("reg", new RegModel());
                return "registration";
            }
            userRepository.save(new UserEntity(reg.getName(), reg.getEmail(), bCryptPasswordEncoder.encode(reg.getPassword1())));
            LOG.info("Пользователь "+reg.getName()+" зарегистрировался");
            regEmail(reg.getName(), reg.getEmail());

        } else {
            model.addAttribute("divError", true);
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            model.addAttribute("reg", new RegModel());
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/blog/confirmationEmail")
    public String confirmationEmail(Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        regEmail(user.getName(), user.getEmail());

        return "redirect:/blog/settingUser";
    }

    @GetMapping("/blog/newPassword")
    public String newPassword(Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        LinkEmailEntity linkEmail = linkEmailRepository.findByName(user.getName());
        String link = getLink(user.getName());

        if (linkEmail != null) {
            linkEmail.setLink(link);
        } else {
            linkEmail = new LinkEmailEntity(user.getName(), link);
        }
        linkEmailRepository.save(linkEmail);
        String url = "/blog/newPassword/email?id=" + link;

        return "redirect:" + url;
    }

    @GetMapping("/blog/newPassword/send")
    public String newPasswordEmailSend(Model model) {
        model.addAttribute("string", new StringModel());
        model.addAttribute("divError", false);

        return "email";
    }

    @PostMapping("/blog/newPassword/send")
    public String newPasswordEmailSendPost(@ModelAttribute("string") StringModel string, Model model) {
        UserEntity user = userRepository.findByName(string.getText());

        if (user != null) {
            newPassword(user.getName(), user.getEmail());
        } else {
            model.addAttribute("string", new StringModel());
            model.addAttribute("divError", true);
            model.addAttribute("error", "Такого пользователя не существует");

            return "email";
        }

        return "redirect:/login";
    }

    @GetMapping("/blog/newPassword/email")
    public String newPasswordEmail(@RequestParam(value = "id") String id, Model model) {
        LinkEmailEntity linkEmail = linkEmailRepository.findByLink(id);

        if (linkEmail != null) {
            model.addAttribute("password", new String2Model());
            model.addAttribute("id", id);
            model.addAttribute("divError", false);

            return "password";
        }

        return "nope";
    }

    @PostMapping("/blog/newPassword/set")
    public String newPasswordSet(@RequestParam(value = "id") String id,
                                 @ModelAttribute("password") String2Model password,
                                 Model model) {

        if (!(password.getString1().equals(password.getString2()))) {
            model.addAttribute("password", new String2Model());
            model.addAttribute("id", id);
            model.addAttribute("divError", true);
            model.addAttribute("error", "Пароли не совпадают");

            return "password";
        }

        LinkEmailEntity linkEmail = linkEmailRepository.findByLink(id);

        if (linkEmail != null) {
            UserEntity user = userRepository.findByName(linkEmail.getName());
            user.setPassword(bCryptPasswordEncoder.encode(password.getString1()));
            LOG.info("Пользователь "+user.getName()+" сменил пароль");
            linkEmailRepository.delete(linkEmail);
        }

        return "redirect:/blog";
    }

    @PostMapping("/blog/confirmationNewEmail")
    public String confirmationNewEmail(@ModelAttribute("string") StringModel string,
                                       Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        user.setEmail(string.getText());
        user.setConfirmed(false);
        userRepository.save(user);
        regEmail(user.getName(), string.getText());

        return "redirect:/blog/settingUser";
    }

    @GetMapping("/blog/confirmationUser")
    public String confirmationUser(@RequestParam(value = "id") String id) {

        ConfirmationUserEntity confirmation = confirmationUserRepository.findByLink(id);

        if (confirmation!=null) {
            UserEntity user = userRepository.findByName(confirmation.getName());
            user.setEmail(confirmation.getEmail());
            user.setConfirmed(true);
            userRepository.save(user);
            confirmationUserRepository.delete(confirmation);
            LOG.info("Пользователь "+user.getName()+" подтвердил аккаунт");
        }

        return "redirect:/blog";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:http://localhost:8080/login";
    }
}
