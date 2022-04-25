package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.darccona.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс контроллера обработки действий на страницах с множеством записей
 */
@Controller
public class BlogController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * Возвращает навигацию для страницы
     * @param name имя страницы
     * @param user имя пользователя
     * @param principal логическая переменная, true если пользователь зашёл
     *                  и false если не авторизирован
     * @return
     */
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

    /**
     * Возвращает список уведомлений для пользователя
     * @param user объект пользователя
     * @return список уведомлений
     */
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
        noticeList = noticeList
                .stream()
                .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                .collect(Collectors.toList());

        return noticeList;
    }

    /**
     * Возвращает список пользователей, на которых можно подписаться
     * @param user имя авторизированного пользователя
     * @return список пользователей
     */
    public List<UserModel> setUserList(UserEntity user) {
        List<UserModel> userList = new ArrayList<>();
        for (UserEntity users : userRepository.findAll()) {
            if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName())==null)) {
                userList.add(new UserModel(users.getName()));
            }
        }

        return userList;
    }

    /**
     * Генерирует главная страницу блога, содержащую записи подписок
     * @param inputStart возвращает 1 при первом заходе на страницу, иначе 0
     * @param principal данные пользователя
     * @param model модель страницы, содержащая все объекты
     * @return главная страница блога
     */
    @RequestMapping("/blog")
    public String blogStart(@RequestParam(value = "new", required = false, defaultValue = "0") int inputStart,
                            Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        BoolModel boolModel;

        if (user == null) {
            return "nope";
        }

        List<RecordModel> recordList = user.getSubscribe()
                .stream().flatMap(sub -> recordRepository.findByUser(userRepository.findByName(sub.getName()))
                .stream().map((record) -> new RecordModel(
                        record.getText().split("\n"), sub.getName(), "", record.getDate(),
                        record.getLike(), record.getComm(),
                        likeRepository.findByUserAndRecord(user, record.getId())!=null,
                        favoriteRepository.findByUserAndRecord(user, record.getId())!=null,
                        record.getId())))
                .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                .collect(Collectors.toList());

        if (recordList.size() != 0) {
            model.addAttribute("record", recordList);
            boolModel = new BoolModel("rec");
        } else {
            boolModel = new BoolModel("isEmpty");
            model.addAttribute("message", "Вы ни на кого не подписаны.\nПора это исправить ->");
        }

        if ((inputStart == 1) && (!(user.getClosed()))) {
            boolModel.setInputStart();
            String2Model string2 = new String2Model();
            string2.setString1(user.getNameBlog());
            model.addAttribute("strings", string2);
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
        model.addAttribute("bool", boolModel);

        return "blog";
    }

    /**
     * Генерирует страницу, содержащую все понравившиеся пользователю записи
     * @param principal данные пользователя
     * @param model модель страницы, содержащая все объекты
     * @return страница, содержащая все понравившиеся пользователю записи
     */
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
            recordList = recordList
                    .stream()
                    .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                    .collect(Collectors.toList());
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

    /**
     * Генерирует страницу, содержащую все записи, дабавленные в избранное пользователем
     * @param principal данные пользователя
     * @param model модель страницы, содержащая все объекты
     * @return страница, содержащая все записи, дабавленные в избранное пользователем
     */
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
            recordList = recordList
                    .stream()
                    .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                    .collect(Collectors.toList());
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

    /**
     * Генерирует страницу, содержащую все записи пользователя
     * @param name имя пользователя, владеющего страницей
     * @param principal данные авторизированного пользователя
     * @param model модель страницы, содержащая все объекты
     * @return страница, содержащая все записи пользователя
     */
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
                recordList = recordList
                        .stream()
                        .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                        .collect(Collectors.toList());
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
                    recordList = recordList.stream().sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort())).collect(Collectors.toList());
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
            model.addAttribute("admin", userRepository.findByName(principal.getName()).getRole().equals("ADMIN"));

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
            recordList = recordList
                    .stream()
                    .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                    .collect(Collectors.toList());
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

        return "blog";
    }
}