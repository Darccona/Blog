package org.darccona;

import org.darccona.database.entity.RecordEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.RecordRepository;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BlogController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private SimpleDateFormat formatNow = new SimpleDateFormat("dd.MM.yy HH:mm");

    @GetMapping("/")
    public String start(Model model) {
        return "redirect:http://localhost:8080/blog";
    }

    @GetMapping("/registration")
    public String formReg(Model model) {
        model.addAttribute("divError", false);
        model.addAttribute("reg",  new RegModel());
        return "registration";
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
            userRepository.save(new UserEntity(reg.getName(), bCryptPasswordEncoder.encode(reg.getPassword1())));
        } else {
            model.addAttribute("divError", true);
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            model.addAttribute("reg", new RegModel());
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:http://localhost:8080/login";
    }

    @RequestMapping("/blog")
    public String blogStart(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        List<RecordModel> recordList = new ArrayList<>();
        for (String name : user.getSub()) {
            for (RecordEntity record : recordRepository.findByUser(userRepository.findByName(name))) {
                recordList.add(new RecordModel(record.getText(), name, record.getDate(),
                        record.getLike(), user.getLikeRecord().contains(record.getId()), false, record.getId()));
            }
        }
        Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
        model.addAttribute("record", recordList);

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if (users != user) {
                userList.add(new UserModel(users.getName()));
            }
        }
        model.addAttribute("users", userList);

        if (recordList.size() == 0) {
            model.addAttribute("bool", new BoolModel("sub"));
        } else {
            model.addAttribute("bool", new BoolModel("rec"));
        }
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        return "blog";
    }

    @RequestMapping("/blog/likeRecord")
    public String blogLike(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        List<RecordModel> recordList = new ArrayList<>();
        for (long id: user.getLikeRecord()) {
            RecordEntity record = recordRepository.findById(id);
            recordList.add(new RecordModel(record.getText(), record.getUser().getName(), record.getDate(),
                    record.getLike(), user.getLikeRecord().contains(record.getId()), false, record.getId()));
        }
        Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
        model.addAttribute("record", recordList);

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if (users != user) {
                userList.add(new UserModel(users.getName()));
            }
        }
        model.addAttribute("users", userList);

        model.addAttribute("bool", new BoolModel("like"));
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        return "blog";
    }

    @PostMapping("/blog/addRecord")
    public String recordAdd(@ModelAttribute("string") StringModel string,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = new RecordEntity(string.getText());
        record.setUser(user);
        recordRepository.save(record);
        return "redirect:/blog";
    }

    @GetMapping("/blog/sub")
    public String userSub(@RequestParam(value = "name") String name,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        UserEntity sub = userRepository.findByName(name);
        if ((sub != null) && (sub != user)) {
            user.setSub(name);
            userRepository.save(user);
        }
//        else return "nope";
        return "redirect:/blog";
    }

    @GetMapping("/blog/like")
    public String userLike(@RequestParam(value = "id") long id,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity like = recordRepository.findById(id);
        if (like != null) {
            if (!user.getLikeRecord().contains(id)) {
                user.setLikeRecord(id);
                like.setLike();
            } else {
                user.removeLikeRecord(id);
                like.removeLike();
            }
            userRepository.save(user);
            recordRepository.save(like);
        }
        return "redirect:/blog";
    }


}