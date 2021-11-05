package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ReactionsController {

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

    @GetMapping("/blog/sub")
    public String userSub(@RequestParam(value = "name") String name,
                          Principal principal, Model model) {
        UserEntity user = userRepository.findByName(principal.getName());
        UserEntity sub = userRepository.findByName(name);

        if ((sub != null) && (sub != user)) {
            SubscribeEntity subscribe = new SubscribeEntity(name);
            subscribe.setUser(user);
            subscribeRepository.save(subscribe);

            NoticeEntity notice = new NoticeEntity(user.getName(), -1, null, 1);
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

                NoticeEntity notice = new NoticeEntity(user.getName(), id, null, 2);
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
