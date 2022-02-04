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
import java.util.Collections;
import java.util.List;

@Controller
public class RecordController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommReplyRepository commReplyRepository;
    @Autowired
    SubscribeRepository subscribeRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

    public ArrayList<NavModel> setNav(String user, boolean principal) {
        ArrayList<NavModel> nav = new ArrayList<>();
        nav.add(new NavModel("/blog", "Лента"));
//        nav.add(new NavModel("/blog/likeRecord", "Понравившееся"));
//        nav.add(new NavModel("/blog/favRecord", "Избранное"));
        if (principal) {
            nav.add(new NavModel("/blog/userRecord?name=" + user, "Мои_посты"));
        } else {
            nav.add(new NavModel("/blog/login", "Мои_посты"));
        }
        return nav;
    }

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
            Collections.sort(noticeList, NoticeModel.COMPARE_BY_DATE);
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
        Collections.sort(commList, CommentModel.COMPARE_BY_DATE_NEW);
        model.addAttribute("comm", commList);

        model.addAttribute("link", "userRecord/record?id=" + id);
        model.addAttribute("newRecord", new StringModel());
        model.addAttribute("editRecord", new StringModel(record.getText()));
        model.addAttribute("commString", new StringModel());

        return "record";
    }
}
