package org.darccona.controller;

import org.darccona.database.entity.ConfirmationUserEntity;
import org.darccona.database.entity.NoticeEntity;
import org.darccona.database.entity.RecordEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.ConfirmationUserRepository;
import org.darccona.database.repository.RecordRepository;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class SettingController {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    ConfirmationUserRepository confirmationUserRepository;

    public ArrayList<NavModel> setNav(String user) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Лента"));
//        nav.add(new NavModel("/blog/likeRecord", "Понравившееся"));
//        nav.add(new NavModel("/blog/favRecord", "Избранное"));
        nav.add(new NavModel("/blog/userRecord?name="+user, "Мои_посты"));
        return nav;
    }

    public List<NoticeModel> setNoticeList(UserEntity user) {
        List<NoticeModel> noticeList = new ArrayList<>();
        for (NoticeEntity notice: user.getNotice()) {
            if (notice.getType() == 1) {
                noticeList.add(new NoticeModel(notice.getText(), "", notice.getDate(), false, notice.getId()));
            } else if (notice.getType() == 2) {
                RecordEntity record = recordRepository.findById(notice.getRecord());
                if (record != null) {
                    noticeList.add(new NoticeModel(notice.getText(), record.getText(), notice.getDate(), true, notice.getId()));
                } else {
                    noticeList.add(new NoticeModel(notice.getText(), "*Пост удалён*", notice.getDate(), true, notice.getId()));
                }
            } else if ((notice.getType() == 3)||(notice.getType() == 4)) {
                noticeList.add(new NoticeModel(notice.getText(), notice.getComm(), notice.getDate(), true, notice.getId()));
            }
        }
        Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
        return noticeList;
    }

    @GetMapping("/blog/settingUser")
    public String settingUser(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        model.addAttribute("nav", setNav(user.getName()));
        model.addAttribute("email", user.getEmail());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("confirmation", user.getConfirmed());
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("link", "settingUser");
        model.addAttribute("setting", new SettingModel(
                user.getDescription(), "", user.getNameBlog()));

        return "setting";
    }

    @PostMapping("/blog/settingUser/set")
    public String settingUserSet(@ModelAttribute("setting") SettingModel setting,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        if (setting.getCode() != "") {
            ConfirmationUserEntity confirmation = confirmationUserRepository.findByName(user.getName());
            if ((confirmation != null) && (confirmation.getCode().equals(setting.getCode()))) {
                user.setEmail(confirmation.getEmail());
                user.setConfirmed(true);
                confirmationUserRepository.delete(confirmation);
                LOG.info("Пользователь "+user.getName()+" подтвердил аккаунт");
            }
        }

        user.setDescription(setting.getDescription());
//        user.setClosed(setting.getClosed());
        user.setNameBlog(setting.getNameBlog());
        user.setClosed(true);
        userRepository.save(user);

        String url = "http://localhost:8080/blog/userRecord?name=" + user.getName();

        return "redirect:" + url;
    }

    @PostMapping("/blog/setting/set")
    public String settingSet(@ModelAttribute("string2") String2Model string2,
                             Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        user.setNameBlog(string2.getString1());
        user.setDescription(string2.getString2());
        user.setClosed(true);
        userRepository.save(user);

        return "redirect:/blog";
    }

    @GetMapping("/blog/setting/back")
    public String settingBack(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        user.setClosed(true);
        userRepository.save(user);

        return "redirect:/blog";
    }

}
