package org.darccona;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
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
    SubscribeRepository subscribeRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

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
        Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
        model.addAttribute("record", recordList);

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
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
            model.addAttribute("bool", new BoolModel("isEmpty"));
            model.addAttribute("message", "Вы ни на кого не подписаны.\nПора это исправить ->");
        } else {
            model.addAttribute("bool", new BoolModel("rec"));
        }

        model.addAttribute("link", "");
        model.addAttribute("nav", setNav("rec", user.getName()));
        model.addAttribute("user", user.getName());
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

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
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

        model.addAttribute("link", "likeRecord");
        model.addAttribute("nav", setNav("like", user.getName()));
        model.addAttribute("user", user.getName());
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

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
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

        model.addAttribute("link", "favRecord");
        model.addAttribute("nav", setNav("fav", user.getName()));
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
                        record.getLike(), record.getComm(),
                        likeRepository.findByUserAndRecord(user, record.getId())!=null,
                        favoriteRepository.findByUserAndRecord(user, record.getId())!=null,
                        record.getId()));
            }
            model.addAttribute("nav", setNav("", user.getName()));

            if (recordList.size() != 0) {
                Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                model.addAttribute("record", recordList);
                model.addAttribute("bool", new BoolModel("userRecord"));
            } else {
                model.addAttribute("bool", new BoolModel("isEmpty"));
                model.addAttribute("message", "Этот пользователь ещё ничего не постил.");
            }
        } else {
            for (RecordEntity record : userRecord.getRecord()) {
                recordList.add(new RecordModel(record.getText(), userRecord.getName(), record.getDate(),
                        record.getLike(), record.getComm(),
                        false,false, record.getId()));
            }
            model.addAttribute("nav", setNav("user", user.getName()));

            if (recordList.size() != 0) {
                Collections.sort(recordList, RecordModel.COMPARE_BY_DATE);
                model.addAttribute("record", recordList);
                model.addAttribute("bool", new BoolModel("myRecord"));
            } else {
                model.addAttribute("bool", new BoolModel("isEmpty"));
                model.addAttribute("message", "Вы ещё ничего не постили.");
            }
        }





        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
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

        model.addAttribute("link", "userRecord?name=" + userRecord.getName());
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());

        return "blog";
    }

    @RequestMapping("/blog/userRecord/record")
    public String blogRecord(@RequestParam(value = "id") long id,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);

        if (record == null) {
            return "nope";
        }

        if (record.getUser() == user) {
            model.addAttribute("bool", new BoolModel("myRecord"));
        } else {
            model.addAttribute("bool", new BoolModel("userRecord"));
        }

        RecordModel rec = new RecordModel(record.getText(), record.getUser().getName(), record.getDate(),
                record.getLike(), record.getComm(),
                likeRepository.findByUserAndRecord(user, record.getId())!=null,
                favoriteRepository.findByUserAndRecord(user, record.getId())!=null,
                record.getId());
        model.addAttribute("record", rec);

        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
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

        model.addAttribute("nav", setNav("", user.getName()));
        model.addAttribute("link", "userRecord/record?id=" + id);
        model.addAttribute("user", user.getName());
        model.addAttribute("string", new StringModel());
        model.addAttribute("editString", new StringModel(record.getText()));

        return "record";
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

    @GetMapping("/blog/delAllMes")
    public String mesAllDel(@RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        user.removeAllNotice();
        userRepository.save(user);

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    @GetMapping("/blog/sub")
    public String userSub(@RequestParam(value = "name") String name,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        UserEntity sub = userRepository.findByName(name);

        if ((sub != null) && (sub != user)) {
            SubscribeEntity subscribe = new SubscribeEntity(name);
            subscribe.setUser(user);
            subscribeRepository.save(subscribe);

            NoticeEntity notice = new NoticeEntity(user.getName(), null, null);
            notice.setUser(sub);
            noticeRepository.save(notice);
        }

        return "redirect:/blog";
    }

    @GetMapping("/blog/like")
    public String userLike(@RequestParam(value = "id") long id,
                           @RequestParam(value = "link", required = false, defaultValue = "") String link,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);
        LikeEntity like = likeRepository.findByUserAndRecord(user, id);

        if ((record != null) && (record.getUser() != user)) {
            if (like == null) {
                LikeEntity likeRecord = new LikeEntity(id);
                likeRecord.setUser(user);
                likeRepository.save(likeRecord);

                record.setLike();
                recordRepository.save(record);

                NoticeEntity notice = new NoticeEntity(user.getName(), record.getText(), null);
                notice.setUser(record.getUser());
                noticeRepository.save(notice);
            } else {
                user.removeLike(like);
                userRepository.save(user);

                record.removeLike();
                recordRepository.save(record);
            }
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    @GetMapping("/blog/fav")
    public String userFav(@RequestParam(value = "id") long id,
                          @RequestParam(value = "link", required = false, defaultValue = "") String link,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);
        FavoriteEntity favorite = favoriteRepository.findByUserAndRecord(user, id);

        if ((record != null) && (record.getUser() != user)) {
            if (favorite == null) {
                FavoriteEntity favoriteRecord = new FavoriteEntity(id);
                favoriteRecord.setUser(user);
                favoriteRepository.save(favoriteRecord);
            } else {
                user.removeFavorite(favorite);
                userRepository.save(user);
            }
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }
}