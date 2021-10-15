package org.darccona;

import org.darccona.database.entity.NoticeEntity;
import org.darccona.database.entity.RecordEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.NoticeRepository;
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
    NoticeRepository noticeRepository;
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
                        record.getLike(), record.getFav(), record.getComm(),
                        user.getLikeRecord().contains(record.getId()), user.getFavRecord().contains(record.getId()), record.getId()));
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

        List<NoticeModel> noticeList = new ArrayList<>();
        for (NoticeEntity notice: user.getNotice()) {
            noticeList.add(new NoticeModel(notice.getText(), notice.getRecord(), notice.getComm(), notice.getDate(), notice.getType()));
        }
        Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
        model.addAttribute("notice", noticeList);
        model.addAttribute("num", user.getNotice().size());

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
                    record.getLike(), record.getFav(), record.getComm(),
                    user.getLikeRecord().contains(record.getId()), user.getFavRecord().contains(record.getId()), record.getId()));
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

        List<NoticeModel> noticeList = new ArrayList<>();
        for (NoticeEntity notice: user.getNotice()) {
            noticeList.add(new NoticeModel(notice.getText(), notice.getRecord(), notice.getComm(), notice.getDate(), notice.getType()));
        }
        Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
        model.addAttribute("notice", noticeList);
        model.addAttribute("num", user.getNotice().size());

        model.addAttribute("bool", new BoolModel("like"));
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        return "blog";
    }

    @RequestMapping("/blog/favRecord")
    public String blogFav(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        List<RecordModel> recordList = new ArrayList<>();
        for (long id: user.getFavRecord()) {
            RecordEntity record = recordRepository.findById(id);
            recordList.add(new RecordModel(record.getText(), record.getUser().getName(), record.getDate(),
                    record.getLike(), record.getFav(), record.getComm(),
                    user.getLikeRecord().contains(record.getId()), user.getFavRecord().contains(record.getId()), record.getId()));
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

        List<NoticeModel> noticeList = new ArrayList<>();
        for (NoticeEntity notice: user.getNotice()) {
            noticeList.add(new NoticeModel(notice.getText(), notice.getRecord(), notice.getComm(), notice.getDate(), notice.getType()));
        }
        Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
        model.addAttribute("notice", noticeList);
        model.addAttribute("num", user.getNotice().size());

        model.addAttribute("bool", new BoolModel("fav"));
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        return "blog";
    }

    @RequestMapping("/blog/userRecord")
    public String blogUser(@RequestParam(value = "name") String name,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        UserEntity userRecord = userRepository.findByName(name);
        List<RecordModel> recordList = new ArrayList<>();
        if (user != userRecord) {
            for (RecordEntity record : userRecord.getRecord()) {
                recordList.add(new RecordModel(record.getText(), userRecord.getName(), record.getDate(),
                        record.getLike(), record.getFav(), record.getComm(),
                        user.getLikeRecord().contains(record.getId()), user.getFavRecord().contains(record.getId()), record.getId()));
            }
            model.addAttribute("bool", new BoolModel("userRecord"));
        } else {
            for (RecordEntity record : userRecord.getRecord()) {
                recordList.add(new RecordModel(record.getText(), userRecord.getName(), record.getDate(),
                        record.getLike(), record.getFav(), record.getComm(),
                        false,false, record.getId()));
            }
            model.addAttribute("bool", new BoolModel("myRecord"));
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

        List<NoticeModel> noticeList = new ArrayList<>();
        for (NoticeEntity notice: user.getNotice()) {
            noticeList.add(new NoticeModel(notice.getText(), notice.getRecord(), notice.getComm(), notice.getDate(), notice.getType()));
        }
        Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
        model.addAttribute("notice", noticeList);
        model.addAttribute("num", user.getNotice().size());

        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        return "blog";
    }

    @PostMapping("/blog/addRecord")
    public String recordAdd(@ModelAttribute("string") StringModel string,
                            @RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = new RecordEntity(string.getText());
        record.setUser(user);
        recordRepository.save(record);
        String url = "http://localhost:8080/blog/" + link;
        if (link.equals("userRecord")) {
            url += "?name=" + user.getName();
        }
        return "redirect:" + url;
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

        NoticeEntity notice = new NoticeEntity(2, user.getName(), null, null);
        notice.setUser(sub);
        noticeRepository.save(notice);
//        sub.setNotice(notice);
        userRepository.save(sub);

        return "redirect:/blog";
    }

    @GetMapping("/blog/like")
    public String userLike(@RequestParam(value = "id") long id,
                           @RequestParam(value = "link", required = false, defaultValue = "") String link,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);
        if (record != null) {
            if (!user.getLikeRecord().contains(id)) {
                user.setLikeRecord(id);
                record.setLike();
            } else {
                user.removeLikeRecord(id);
                record.removeLike();
            }
            userRepository.save(user);
            recordRepository.save(record);
        }

        NoticeEntity notice = new NoticeEntity(1, user.getName(), record.getText(), null);
        notice.setUser(record.getUser());
        noticeRepository.save(notice);
//        record.getUser().setNotice(notice);
        userRepository.save(record.getUser());

        String url = "http://localhost:8080/blog/" + link;
        if (link.equals("userRecord")) {
            url += "?name=" + record.getUser().getName();
        }
        return "redirect:" + url;
    }

    @GetMapping("/blog/fav")
    public String userFav(@RequestParam(value = "id") long id,
                          @RequestParam(value = "link", required = false, defaultValue = "") String link,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);
        if (record != null) {
            if (!user.getFavRecord().contains(id)) {
                user.setFavRecord(id);
                record.setFav();
            } else {
                user.removeFavRecord(id);
                record.removeFav();
            }
            userRepository.save(user);
            recordRepository.save(record);
        }
        String url = "http://localhost:8080/blog/" + link;
        if (link.equals("userRecord")) {
            url += "?name=" + record.getUser().getName();
        }
        return "redirect:" + url;
    }
}