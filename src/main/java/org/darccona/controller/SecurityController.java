package org.darccona.controller;

import org.darccona.config.EmailService;
import org.darccona.database.entity.ConfirmationUserEntity;
import org.darccona.database.entity.LinkEmailEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.ConfirmationUserRepository;
import org.darccona.database.repository.LinkEmailRepository;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.RegModel;
import org.darccona.model.String2Model;
import org.darccona.model.StringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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

/**
 * Класс контролеера обработки действий с аккаунтом и безопастностью
 */
@Controller
public class SecurityController {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationUserRepository confirmationUserRepository;
    @Autowired
    private LinkEmailRepository linkEmailRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Возвращает страницу для регистрации
     * @param model модель страницы, содержащая все объекты
     * @return страница для регистрации
     */
    @GetMapping("/registration")
    public String formReg(Model model) {
        model.addAttribute("divError", false);
        model.addAttribute("reg",  new RegModel());

        return "registration";
    }

    /**
     * Генерирует окончание для ссылки
     * @param name имя пользователя
     * @return окончание для ссылки
     */
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

    /**
     * Генерирует 5-значный код из цифр
     * @return 5-значный код из цифр
     */
    private String getCode() {
        Random random = new Random();
        String code = "";

        for(int i=0; i<5; i++) {
            code += random.nextInt(10);
        }

        return code;
    }

    /**
     * Отправляет сообщение для подтверждения аккаунта на эллектронную почту
     * @param name имя пользователя
     * @param email эллектронная почта пользователя
     */
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

    /**
     * Отправляет сообщение для смены пороля на эллектронную почту
     * @param name имя пользователя
     * @param email эллектронная почта пользователя
     */
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

    /**
     * Обработка запроса на регистрацию. Если информации достаточно и она верная,
     * то регестрирует пользователя, иначе перенаправляет на страницу регистрации с сообщение об ошибке
     * @param reg модел, содержащая всю необходимую, для регистрацию пользователя информацию
     * @param model модель страницы, содержащая все объекты
     * @return перенаправляет на страницу входа или регистрации
     */
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

    /**
     * Повторно отправляет письмо для подтверждения аккаунта на эллектронную почту
     * @param principal данные пользователя
     * @return перенаправляет на страницу настроек аккаунта
     */
    @GetMapping("/blog/confirmationEmail")
    public String confirmationEmail(Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        regEmail(user.getName(), user.getEmail());

        return "redirect:/blog/settingUser";
    }

    /**
     * Создаёт новую ссылку для смены пароля пользователя
     * @param principal данные пользователя
     * @return переносит на страницу для смены пароля пользователя
     */
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

    /**
     * Генерирует страницу для указания имени пользователя для востановления доступа
     * @param model модель страницы, содержащая все объекты
     * @return страницу для указания имени пользователя
     */
    @GetMapping("/blog/newPassword/send")
    public String newPasswordEmailSend(Model model) {
        model.addAttribute("string", new StringModel());
        model.addAttribute("divError", false);

        return "email";
    }

    /**
     * Получает имя пользователя и отправляет письмо для востановления доступа на его почту.
     * Если нет пользователя с таким именем возвращает страницу для ввода имени с указанием на ошибку
     * @param string имя пользователя, которому нужно востановить пароль
     * @param model модель страницы, содержащая все объекты
     * @return страницу для входа или страницу для воода имени
     */
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

    /**
     * Генерирует страницу для указания нового пароля
     * @param id ссылка для востановления доступа
     * @param model модель страницы, содержащая все объекты
     * @return страница для указания нового пароля
     */
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

    /**
     * Получает новый пароль и присваивает его пользователю
     * @param id ссылка для востановления доступа
     * @param password пара одинаковых новых паролей
     * @param model модель страницы, содержащая все объекты
     * @return перенаправляет на главную страницу блога
     */
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

    /**
     * Получает и присваивает пользователю новую электронную почту
     * @param string адрес новой электронной почты
     * @param principal данные пользователя
     * @return перенаправляет на страницу настроек пользователя
     */
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

    /**
     * Обрабатывает подтверждение пользователем электронной почты
     * @param id ссылка для подтверждения пользователя
     * @return перенаправляет на главную страницу
     */
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

    /**
     * Обрабатывает выход пользователя из аккаунта
     * @param request данные на пользователя
     * @return перенаправляет на страницу входа
     * @throws ServletException
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:http://localhost:8080/login";
    }
}
