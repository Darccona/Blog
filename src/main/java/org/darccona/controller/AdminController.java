package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.darccona.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс контроллера обработки действий администратора
 */
@Controller
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityController.class);

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
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommReplyRepository commReplyRepository;

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
     * Генерирует страницу управления пользователем для администратора
     * @param name имя владельца страницы
     * @param principal данные администратора
     * @param model модель страницы, содержащая все объекты
     * @return страница управления пользователем
     */
    @RequestMapping("/blog/admin/blogUser")
    public String blog(@RequestParam(value = "name") String name, Principal principal, Model model) {

        UserEntity user = userRepository.findByName(name);

        if (user == null) {
            return "nope";
        }

        UserEntity admin = userRepository.findByName(principal.getName());
        AdminUserModel blog = new AdminUserModel(name);

        blog.setUser(user.getName(), user.getNameBlog(), user.getDescription(), user.getRole().equals("ADMIN"));

        List<UserModel> subList = new ArrayList<>();
        for (SubscribeEntity userSub : subscribeRepository.findByName(name)) {
            subList.add(new UserModel(userSub.getUser().getName()));
        }
        blog.setUserSub(subList);

        for (RecordEntity record : recordRepository.findByUser(user)) {
            blog.setRecord(record.getId(), record.getText().split("\n"),
                    record.getDate(), record.getComm(), record.getLike(),
                    favoriteRepository.findByRecord(record.getId()).size());
        }
        blog.sortRecord();

        model.addAttribute("blog", blog);
        model.addAttribute("nav", setNav("", admin.getName(), true));
        model.addAttribute("user", new UserModel(admin.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("notice", setNoticeList(admin));
        model.addAttribute("num", admin.getNotice().size());

        return "adminBlog";
    }

    /**
     * Генерирует страницу управления записью для администратора
     * @param id номер записи
     * @param principal данные администратора
     * @param model модель страницы, содержащая все объекты
     * @return страница управления записью
     */
    @RequestMapping("/blog/admin/record")
    public String record(@RequestParam(value = "id") long id,
                         Principal principal, Model model) {

        RecordEntity record = recordRepository.findById(id);
        UserEntity admin = userRepository.findByName(principal.getName());
        AdminRecordModel recordModel = new AdminRecordModel(id, record.getUser().getName(),
                record.getUser().getNameBlog(), record.getText().split("\n"), record.getDate());

        List<UserModel> userLike = new ArrayList<>();
        for (LikeEntity like: likeRepository.findByRecord(record.getId())) {
            userLike.add(new UserModel(like.getUser().getName()));
        }
        recordModel.setLikeUser(userLike);

        List<UserModel> userFav = new ArrayList<>();
        for (FavoriteEntity fav: favoriteRepository.findByRecord(record.getId())) {
            userLike.add(new UserModel(fav.getUser().getName()));
        }
        recordModel.setFavUser(userFav);

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
        commList = commList.
                stream().
                sorted((o1,o2) -> -o1.getDateSort().compareTo(o2.getDateSort())).
                collect(Collectors.toList());
        recordModel.setComments(commList);

        model.addAttribute("record", recordModel);
        model.addAttribute("nav", setNav("", admin.getName(), true));
        model.addAttribute("user", new UserModel(admin.getName()));
        model.addAttribute("string", new StringModel());
        model.addAttribute("notice", setNoticeList(admin));
        model.addAttribute("num", admin.getNotice().size());

        return "adminRecord";
    }

    /**
     * Удаляет пользователя
     * @param name имя удаляемого пользователя
     * @return перенаправляет на стартовую страницу
     */
    @RequestMapping("/blog/admin/delUser")
    public String delUser(@RequestParam(value = "name") String name) {
        UserEntity user = userRepository.findByName(name);
        userRepository.delete(user);
        LOG.info("Пользователь "+user.getName()+" был удалён администратором");

        return "redirect:/blog";
    }

    /**
     * Удаляет запись
     * @param id номер удаляемой записи
     * @return перенаправляет на страницу пользователя удалённой записи
     */
    @RequestMapping("/blog/admin/delRecord")
    public String delRecord(@RequestParam(value = "id") long id) {
        RecordEntity record = recordRepository.findById(id);
        UserEntity user = record.getUser();
        String url = "/blog/admin/blogUser?name=" + user.getName();
        user.removeRecord(record);
        userRepository.save(user);

        return "redirect:" + url;
    }

    /**
     * Удаляет комментарий
     * @param id номер записи, у которой удаляют комментарий
     * @param recId номер удаляемого комментария, если он не является ответом, иначе номер родительского комментария
     * @param commId номер удаляемого комментария, если он является ответом, иначе присваивается -1
     * @return перенаправляет на страницу записи, у которой удаляют комментарий
     */
    @RequestMapping("/blog/admin/delComment")
    public String delComment(@RequestParam(value = "id") long id,
                             @RequestParam(value = "recId") long recId,
                             @RequestParam(value = "commId", required = false, defaultValue = "-1") long commId) {

        RecordEntity record = recordRepository.findById(id);
        CommentEntity comm = commentRepository.findById(recId);

        if (commId != -1) {
            CommReplyEntity reply = commReplyRepository.findById(commId);
            comm.removeCommReply(reply);
            commentRepository.save(comm);
        } else {
            record.removeComm(comm.getNumReply());
            record.removeComment(comm);
        }

        record.removeComm();
        recordRepository.save(record);

        String url = "/blog/admin/record?id=" + id;

        return "redirect:" + url;
    }

    /**
     * Присваивает пользователю права администратора
     * @param name имя пользователя, получающего права администратора
     * @return перенаправляет на страницу пользователя, получившего права администратора
     */
    @GetMapping("/blog/admin/setAdmin")
    public String setAdmin(@RequestParam(value = "name") String name) {
        UserEntity user = userRepository.findByName(name);
        user.setRole("ADMIN");
        userRepository.save(user);
        LOG.info("Пользователь "+user.getName()+" получил права администратора");
        String url = "/blog/admin/blogUser?name=" + name;

        return "redirect:" + url;
    }

    /**
     * Удаляет права администратора у пользователя
     * @param name имя пользователя, лишённого прав администратора
     * @return перенаправляет на страницу пользователя, лишённого прав администратора
     */
    @GetMapping("/blog/admin/delAdmin")
    public String delAdmin(@RequestParam(value = "name") String name) {
        UserEntity user = userRepository.findByName(name);
        user.setRole("USER");
        userRepository.save(user);
        LOG.info("Пользователь "+user.getName()+" лишился прав администратора");
        String url = "/blog/admin/blogUser?name=" + name;

        return "redirect:" + url;
    }
}
