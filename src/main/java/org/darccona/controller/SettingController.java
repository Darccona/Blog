package org.darccona.controller;

import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.NavModel;
import org.darccona.model.SettingModel;
import org.darccona.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class SettingController {

    @Autowired
    UserRepository userRepository;

    public ArrayList<NavModel> setNav(String user) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Рекомендации"));
//        nav.add(new NavModel("/blog/likeRecord", "Понравившееся"));
//        nav.add(new NavModel("/blog/favRecord", "Избранное"));
        nav.add(new NavModel("/blog/userRecord?name="+user, "Мои_посты"));
        return nav;
    }

    @GetMapping("/blog/settingUser")
    public String settingUser(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        model.addAttribute("nav", setNav(user.getName()));
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("setting", new SettingModel(
                user.getDescription(), user.getNameBlog(), user.getClosed()));

        return "setting";
    }

    @PostMapping("/blog/settingUser/set")
    public String settingSet(@ModelAttribute("setting") SettingModel setting,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        user.setDescription(setting.getDescription());
        user.setClosed(setting.getClosed());
        user.setNameBlog(setting.getNameBlog());
        userRepository.save(user);

        String url = "http://localhost:8080/blog/userRecord?name=" + user.getName();

        return "redirect:" + url;
    }

}
