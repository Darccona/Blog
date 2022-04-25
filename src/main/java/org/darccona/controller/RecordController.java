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
 * Класс контроллера обработки действий с записью
 */
@Controller
public class RecordController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommReplyRepository commReplyRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * Возвращает навигацию для страницы
     * @param user имя пользователя
     * @param principal логическая переменная, true если пользователь зашёл
     *                  и false если не авторизирован
     * @return
     */
    public ArrayList<NavModel> setNav(String user, boolean principal) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Лента"));
        if (principal) {
            nav.add(new NavModel("/blog/userRecord?name=" + user, "Мои_посты"));
        } else {
            nav.add(new NavModel("/blog/login", "Мои_посты"));
        }
        return nav;
    }

    /**
     * Возвращает страницу с записью и комментариями к ней
     * @param id номер записи
     * @param principal данные пользователя
     * @param model модель страницы, содержащая все объекты
     * @return страница записи
     */
    @RequestMapping("/blog/userRecord/record")
    public String blogRecord(@RequestParam(value = "id") long id,
                             Principal principal, Model model) {

        RecordEntity record = recordRepository.findById(id);

        if (record == null) {
            return "nope";
        }

        if (principal != null) {
            model.addAttribute("principal", true);
            UserEntity user = userRepository.findByName(principal.getName());

            if (record.getUser() == user) {
                model.addAttribute("bool", new BoolModel("myRecord"));
            } else {
                model.addAttribute("bool", new BoolModel("userRecord"));
            }

            String[] text = record.getText().split("\n");
            RecordModel rec = new RecordModel(text, record.getUser().getName(), record.getUser().getNameBlog(),
                    record.getDate(), record.getLike(), record.getComm(),
                    likeRepository.findByUserAndRecord(user, record.getId()) != null,
                    favoriteRepository.findByUserAndRecord(user, record.getId()) != null,
                    record.getId());
            model.addAttribute("record", rec);

            List<UserModel> userList = new ArrayList<>();
            for (UserEntity users : userRepository.findAll()) {
                if ((users != user) && (subscribeRepository.findByUserAndName(user, users.getName()) == null)) {
                    userList.add(new UserModel(users.getName()));
                }
            }
            model.addAttribute("users", userList);

            List<NoticeModel> noticeList = new ArrayList<>();
            for (NoticeEntity notice: user.getNotice()) {
                if (notice.getType() == 1) {
                    noticeList.add(new NoticeModel(notice.getText(), "", notice.getDate(), false, notice.getId()));
                } else if (notice.getType() == 2) {
                    RecordEntity record1 = recordRepository.findById(notice.getRecord());
                    if (record1 != null) {
                        noticeList.add(new NoticeModel(notice.getText(), record1.getText(), notice.getDate(), true, notice.getId()));
                    } else {
                        noticeList.add(new NoticeModel(notice.getText(), "*Пост удалён*", notice.getDate(), true, notice.getId()));
                    }
                } else if ((notice.getType() == 3)||(notice.getType() == 4)) {
                    noticeList.add(new NoticeModel(notice.getText(), notice.getComm(), notice.getDate(), true, notice.getId()));
                }
            }
            noticeList = noticeList.stream().sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort())).collect(Collectors.toList());
            model.addAttribute("notice", noticeList);
            model.addAttribute("num", user.getNotice().size());

            model.addAttribute("nav", setNav(user.getName(), true));
            model.addAttribute("user", new UserModel(
                    user.getName(), user.getNameBlog(), user.getDescription()));
            model.addAttribute("admin", userRepository.findByName(principal.getName()).getRole().equals("ADMIN"));

        } else {
            model.addAttribute("principal", false);
            model.addAttribute("bool", new BoolModel("userRecord"));

            String[] text = record.getText().split("\n");
            RecordModel rec = new RecordModel(text, record.getUser().getName(), record.getUser().getNameBlog(), record.getDate(),
                    record.getLike(), record.getComm(), false, false, record.getId());
            model.addAttribute("record", rec);

            List<UserModel> userList = new ArrayList<>();
            for (UserEntity users : userRepository.findAll()) {
                userList.add(new UserModel(users.getName()));
            }
            model.addAttribute("users", userList);

            model.addAttribute("nav", setNav("", false));
        }

        List<CommentModel> commList = new ArrayList<>();
        for (CommentEntity comm : record.getComment()) {
            CommentModel c = new CommentModel(comm.getId(), comm.getName(), comm.getText(), comm.getDate());
            for (CommReplyEntity reply : comm.getCommReply()) {
                CommReplyEntity r = commReplyRepository.findById(reply.getCommId());
                if (r == null) {
                    c.setReply(reply.getId(), reply.getName(), "", reply.getText(), reply.getDate());
                } else {
                    c.setReply(reply.getId(), reply.getName(), r.getName(), reply.getText(), reply.getDate());
                }
            }
            c.sortReply();
            commList.add(c);
        }
        commList = commList
                .stream()
                .sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort()))
                .collect(Collectors.toList());
        model.addAttribute("comm", commList);

        model.addAttribute("link", "userRecord/record?id=" + id);
        model.addAttribute("newRecord", new StringModel());
        model.addAttribute("editRecord", new StringModel(record.getText()));
        model.addAttribute("commString", new StringModel());

        return "record";
    }
}
