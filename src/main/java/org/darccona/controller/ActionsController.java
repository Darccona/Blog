package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.darccona.model.StringModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ActionsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    NoticeRepository noticeRepository;

    @PostMapping("/blog/addRecord")
    public String recordAdd(@ModelAttribute("string") StringModel string,
                            @RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = new RecordEntity(string.getText());
        record.setUser(user);
        recordRepository.save(record);
        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    @PostMapping("/blog/editRecord")
    public String recordEdit(@ModelAttribute("editString") StringModel string,
                             @RequestParam(value = "id") long id,
                             @RequestParam(value = "link", required = false, defaultValue = "") String link,
                             Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);

        if (record.getUser() == user) {
            record.setText(string.getText());
            recordRepository.save(record);
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    @GetMapping("/blog/delRecord")
    public String recordDel(@RequestParam(value = "id") long id,
                            @RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);
        UserEntity userRecord = record.getUser();

        if (userRecord == user) {
            userRecord.removeRecord(record);
            userRepository.save(userRecord);
        } else {
            return "nope";
        }

        String url = "http://localhost:8080/blog/userRecord?name=" + user.getName();

        return "redirect:" + url;
    }

    @GetMapping("/blog/delMes")
    public String mesDel(@RequestParam(value = "id") long id,
                         Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        NoticeEntity notice = noticeRepository.findById(id);
        String url = "http://localhost:8080/blog/";

        if (notice.getType() == 1) {
            url += "userRecord?name=" + notice.getAuthor();
        } else {
            url += "userRecord/record?id=" + notice.getRecord();
        }

        user.removeNotice(notice);
        userRepository.save(user);

        return "redirect:" + url;
    }

    @GetMapping("/blog/delAllMes")
    public String mesAllDel(@RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        user.removeAllNotice();
        userRepository.save(user);

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }
}
