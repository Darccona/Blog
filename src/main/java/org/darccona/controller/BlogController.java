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

    public ArrayList<NavModel> setNav(String name, String user) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Рекомендации"));
        nav.add(new NavModel("/blog/likeRecord", "Понравившееся"));
        nav.add(new NavModel("/blog/favRecord", "Избранное"));
        nav.add(new NavModel("/blog/userRecord?name="+user, "Мои_посты"));

        switch (name) {
            case "rec": nav.get(0).setBool(); break;
            case "like": nav.get(1).setBool(); break;
            case "fav": nav.get(2).setBool(); break;
            case "user": nav.get(3).setBool(); break;
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
                recordList.add(new RecordModel(record.getText(), sub.getName(), record.getDate(),
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
        model.addAttribute("nav", setNav("rec", user.getName()));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());

        return "blog";
    }

    @RequestMapping("/blog/likeRecord")
    public String blogLike(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        List<RecordModel> recordList = new ArrayList<>();
        for (LikeEntity like : user.getLike()) {
            RecordEntity record = recordRepository.findById(like.getRecord());
            if (record != null) {
                recordList.add(new RecordModel(record.getText(), record.getUser().getName(), record.getDate(),
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
        model.addAttribute("nav", setNav("like", user.getName()));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());

        return "blog";
    }

    @RequestMapping("/blog/favRecord")
    public String blogFav(Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());

        List<RecordModel> recordList = new ArrayList<>();
        for (FavoriteEntity favorite : user.getFavorite()) {
            RecordEntity record = recordRepository.findById(favorite.getRecord());
            if (record != null) {
                recordList.add(new RecordModel(record.getText(), record.getUser().getName(), record.getDate(),
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
        model.addAttribute("nav", setNav("fav", user.getName()));
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
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
            model.addAttribute("nav", setNav("", user.getName()));
            for (RecordEntity record : userRecord.getRecord()) {
                recordList.add(new RecordModel(record.getText(), userRecord.getName(), record.getDate(),
                        record.getLike(), record.getComm(),
                        likeRepository.findByUserAndRecord(user, record.getId())!=null,
                        favoriteRepository.findByUserAndRecord(user, record.getId())!=null,
                        record.getId()));
            }

            if (recordList.size() != 0) {
                Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                model.addAttribute("record", recordList);
                model.addAttribute("bool", new BoolModel("userRecord"));
            } else {
                model.addAttribute("bool", new BoolModel("isEmpty"));
                model.addAttribute("message", "Этот пользователь ещё ничего не постил.");
            }
        } else {
            model.addAttribute("nav", setNav("user", user.getName()));
            for (RecordEntity record : userRecord.getRecord()) {
                recordList.add(new RecordModel(record.getText(), userRecord.getName(), record.getDate(),
                        record.getLike(), record.getComm(),
                        false,false, record.getId()));
            }

            if (recordList.size() != 0) {
                Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                model.addAttribute("record", recordList);
                model.addAttribute("bool", new BoolModel("myRecord"));
            } else {
                model.addAttribute("bool", new BoolModel("isEmpty"));
                model.addAttribute("message", "Вы ещё ничего не постили.");
            }
        }

        model.addAttribute("link", "userRecord?name=" + userRecord.getName());
        model.addAttribute("users", setUserList(user));
        model.addAttribute("notice", setNoticeList(user));
        model.addAttribute("num", user.getNotice().size());
        model.addAttribute("user", new UserModel(user.getName()));
        model.addAttribute("string", new StringModel());

        return "blog";
    }
}