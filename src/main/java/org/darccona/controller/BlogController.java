package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.darccona.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
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
    SubscribeRepository subscribeRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

    public ArrayList<NavModel> setNav(String name, String user, boolean principal) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Лента"));
        if (principal) {
            nav.add(new NavModel("/blog/userRecord?name=" + user, "Мои_посты"));
        } else {
            nav.add(new NavModel("/blog/login", "Мои_посты"));
        }

        switch (name) {
            case "rec": nav.get(0).setBool(); break;
            case "user": nav.get(1).setBool(); break;
        }

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

    public List<UserModel> setUserList(UserEntity user) {
        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
                userList.add(new UserModel(users.getName()));
            }
        }
        return userList;
    }

    @RequestMapping("/blog")
    public String blogStart(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        List<RecordModel> recordList = new ArrayList<>();
        for (SubscribeEntity sub : user.getSubscribe()) {
            for (RecordEntity record : recordRepository.findByUser(userRepository.findByName(sub.getName()))) {
                String[] text = record.getText().split("\n");
                recordList.add(new RecordModel(text, sub.getName(), "", record.getDate(),
                        record.getLike(), record.getComm(),
                        likeRepository.findByUserAndRecord(user, record.getId())!=null,
                        favoriteRepository.findByUserAndRecord(user, record.getId())!=null,
                        record.getId()));
            }
        }

        if (recordList.size() != 0) {
            Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
            model.addAttribute("record", recordList);
            model.addAttribute("bool", new BoolModel("rec"));
        } else {
            model.addAttribute("bool", new BoolModel("isEmpty"));
            model.addAttribute("message", "Вы ни на кого не подписаны.\nПора это исправить ->");
        }

        model.addAttribute("link", "");
        model.addAttribute("nav", setNav("rec", user.getName(), true));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("principal", true);
        model.addAttribute("admin", user.getRole().equals("ADMIN"));

        return "blog";
    }

    @RequestMapping("/blog/likeRecord")
    public String blogLike(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        List<RecordModel> recordList = new ArrayList<>();
        for (LikeEntity like : user.getLike()) {
            RecordEntity record = recordRepository.findById(like.getRecord());
            if (record != null) {
                String[] text = record.getText().split("\n");
                recordList.add(new RecordModel(text, record.getUser().getName(), "", record.getDate(),
                        record.getLike(), record.getComm(),
                        true, favoriteRepository.findByUserAndRecord(user, record.getId()) != null,
                        record.getId()));
            }
        }

        if (recordList.size() != 0) {
            Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
            model.addAttribute("record", recordList);
            model.addAttribute("bool", new BoolModel("like"));
        } else {
            model.addAttribute("bool", new BoolModel("isEmpty"));
            model.addAttribute("message", "Вы ещё ничего не добавили в понравившееся.");
        }

        model.addAttribute("link", "likeRecord");
        model.addAttribute("nav", setNav("like", user.getName(), true));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("principal", true);
        model.addAttribute("admin", user.getRole().equals("ADMIN"));

        return "blog";
    }

    @RequestMapping("/blog/favRecord")
    public String blogFav(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        List<RecordModel> recordList = new ArrayList<>();
        for (FavoriteEntity favorite : user.getFavorite()) {
            RecordEntity record = recordRepository.findById(favorite.getRecord());
            if (record != null) {
                String[] text = record.getText().split("\n");
                recordList.add(new RecordModel(text, record.getUser().getName(), "", record.getDate(),
                        record.getLike(), record.getComm(),
                        likeRepository.findByUserAndRecord(user, record.getId()) != null, true,
                        record.getId()));
            }
        }

        if (recordList.size() != 0) {
            Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
            model.addAttribute("record", recordList);
            model.addAttribute("bool", new BoolModel("fav"));
        } else {
            model.addAttribute("bool", new BoolModel("isEmpty"));
            model.addAttribute("message", "Вы ещё ничего не добавили в избранное.");
        }

        model.addAttribute("link", "favRecord");
        model.addAttribute("nav", setNav("fav", user.getName(), true));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("principal", true);
        model.addAttribute("admin", user.getRole().equals("ADMIN"));

        return "blog";
    }

    @RequestMapping("/blog/userRecord")
    public String blogUser(@RequestParam(value = "name") String name,
                           Principal principal, Model model) {

        UserEntity userRecord = userRepository.findByName(name);
        List<RecordModel> recordList = new ArrayList<>();

        if (principal != null) {
            model.addAttribute("principal", true);
            UserEntity user = userRepository.findByName(principal.getName());

            if (user != userRecord) {
                model.addAttribute("bool", new BoolModel("userRecord"));
                model.addAttribute("subscribe", (subscribeRepository.findByUserAndName(user, userRecord.getName()) != null));
                model.addAttribute("nav", setNav("", user.getName(), true));

                for (RecordEntity record : userRecord.getRecord()) {
                    String[] text = record.getText().split("\n");
                    recordList.add(new RecordModel(text, userRecord.getName(), userRecord.getNameBlog(), record.getDate(),
                            record.getLike(), record.getComm(),
                            likeRepository.findByUserAndRecord(user, record.getId()) != null,
                            favoriteRepository.findByUserAndRecord(user, record.getId()) != null,
                            record.getId()));
                }
                Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                model.addAttribute("record", recordList);

            } else {
                BoolModel bool = new BoolModel("myRecord");
                model.addAttribute("bool", new BoolModel("myRecord"));
                model.addAttribute("nav", setNav("user", user.getName(), true));

                for (RecordEntity record : userRecord.getRecord()) {
                    String[] text = record.getText().split("\n");
                    recordList.add(new RecordModel(text, userRecord.getName(), userRecord.getNameBlog(), record.getDate(),
                            record.getLike(), record.getComm(),
                            false,false, record.getId()));
                }

                if (recordList.size() != 0) {
                    Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                    model.addAttribute("record", recordList);
                } else {
                    bool.setEmpty();
                    model.addAttribute("message", "Вы ещё ничего не постили.");
                }
                model.addAttribute("bool", bool);
            }

            model.addAttribute("users", setUserList(user));
            model.addAttribute("user", new UserModel(user.getName()));
            model.addAttribute("notice", setNoticeList(user));
            model.addAttribute("num", user.getNotice().size());

        } else {
            model.addAttribute("principal", false);
            model.addAttribute("nav", setNav("", "", false));
            model.addAttribute("subscribe", false);
            model.addAttribute("bool", new BoolModel("userRecord"));

            for (RecordEntity record : userRecord.getRecord()) {
                String[] text = record.getText().split("\n");
                recordList.add(new RecordModel(text, userRecord.getName(), "", record.getDate(),
                        record.getLike(), record.getComm(),
                        false, false, record.getId()));
            }
            Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
            model.addAttribute("record", recordList);

            List<UserModel> userList = new ArrayList<>();
            for (UserEntity users : userRepository.findAll()) {
                userList.add(new UserModel(users.getName()));
            }
            model.addAttribute("users", userList);
        }

        model.addAttribute("userRecord", new UserModel(
                userRecord.getName(), userRecord.getNameBlog(), userRecord.getDescription()));
        model.addAttribute("link", "userRecord?name=" + userRecord.getName());
        model.addAttribute("string", new StringModel());
        model.addAttribute("admin", userRepository.findByName(principal.getName()).getRole().equals("ADMIN"));

        return "blog";
    }
}