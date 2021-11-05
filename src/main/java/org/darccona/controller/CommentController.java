package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.darccona.model.StringModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class CommentController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RecordRepository recordRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommReplyRepository commReplyRepository;
    @Autowired
    NoticeRepository noticeRepository;

    @PostMapping("/blog/comm")
    public String userComm(@ModelAttribute("commString") StringModel string,
                           @RequestParam(value = "id") long id,
                           @RequestParam(value = "comm", required = false, defaultValue = "-1") long comm,
                           @RequestParam(value = "link", required = false, defaultValue = "") String link,
                           Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);

        if (record != null) {

            if (comm == -1) {
                CommentEntity comment = new CommentEntity(user.getName(), string.getText());
                comment.setRecord(record);
                commentRepository.save(comment);
            } else {
                CommentEntity comment = commentRepository.findById(comm);
                CommReplyEntity reply = new CommReplyEntity(user.getName(), string.getText(), comm);
                reply.setComment(comment);
                commReplyRepository.save(reply);

                if (userRepository.findByName(comment.getName()) != user) {
                    NoticeEntity notice1 = new NoticeEntity(user.getName(), id, string.getText(), 4);
                    notice1.setUser(userRepository.findByName(comment.getName()));
                    noticeRepository.save(notice1);
                }
            }

            record.setComm();
            recordRepository.save(record);

            if (record.getUser() != user) {
                NoticeEntity notice = new NoticeEntity(user.getName(), id, string.getText(), 3);
                notice.setUser(record.getUser());
                noticeRepository.save(notice);
            }
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    @PostMapping("/blog/commReply")
    public String userCommReply(@ModelAttribute("commString") StringModel string,
                                @RequestParam(value = "id") long id,
                                @RequestParam(value = "comm") long comm,
                                @RequestParam(value = "link", required = false, defaultValue = "") String link,
                                Principal principal, Model model) {

        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);

        if (record != null) {
            CommReplyEntity commentReply = commReplyRepository.findById(comm);
            CommentEntity comment = commentReply.getComment();
            CommReplyEntity reply = new CommReplyEntity(user.getName(), string.getText(), comm);
            reply.setComment(comment);
            commReplyRepository.save(reply);

            if (userRepository.findByName(commentReply.getName()) != user) {
                NoticeEntity notice1 = new NoticeEntity(user.getName(), id, string.getText(), 4);
                notice1.setUser(userRepository.findByName(commentReply.getName()));
                noticeRepository.save(notice1);
            }

            record.setComm();
            recordRepository.save(record);

            if (record.getUser() != user) {
                NoticeEntity notice = new NoticeEntity(user.getName(), id, string.getText(), 3);
                notice.setUser(record.getUser());
                noticeRepository.save(notice);
            }
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }
}
