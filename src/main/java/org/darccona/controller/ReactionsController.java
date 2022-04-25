package org.darccona.controller;

import org.darccona.database.entity.*;
import org.darccona.database.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Класс контроллера обработки реакций пользователя
 */
@Controller
public class ReactionsController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private NoticeRepository noticeRepository;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * Обработка подписки пользователя
     * @param name имя пользователя, на которого подписываются
     * @param principal данные авторизированного пользователя
     * @return перенаправляет на страницу пользователя, на которого подписались
     */
    @GetMapping("/blog/sub")
    public String userSub(@RequestParam(value = "name") String name,
                          Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        UserEntity sub = userRepository.findByName(name);
        SubscribeEntity subscribe = subscribeRepository.findByUserAndName(user, sub.getName());

        if ((sub != null) && (sub != user)) {

            if (subscribe != null) {
                user.removeSubscribe(subscribe);
                userRepository.save(user);
            } else {
                SubscribeEntity subscribe1 = new SubscribeEntity(name);
                subscribe1.setUser(user);
                subscribeRepository.save(subscribe1);

                NoticeEntity notice = new NoticeEntity(user.getName(), -1, null, 1);
                notice.setUser(sub);
                noticeRepository.save(notice);
            }
        }

        return "redirect:/blog/userRecord?name=" + sub.getName();
    }

    /**
     * Обработка пользователем нажатия "нравится"
     * @param id номер записи
     * @param link ссылка на страницу, с которой пользователь поставил "нравится"
     * @param principal данные пользователя
     * @return перенаправляет на страницу, с которой пользователь поставил "нравится"
     */
    @GetMapping("/blog/like")
    public String userLike(@RequestParam(value = "id") long id,
                           @RequestParam(value = "link", required = false, defaultValue = "") String link,
                           Principal principal) {
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

    /**
     * Обработка пользователем добавления в избранное
     * @param id номер записи
     * @param link ссылка на страницу, с которой пользователь добавил в избранное
     * @param principal данные пользователя
     * @return перенаправляет на страницу, с которой пользователь добавил в избранное
     */
    @GetMapping("/blog/fav")
    public String userFav(@RequestParam(value = "id") long id,
                          @RequestParam(value = "link", required = false, defaultValue = "") String link,
                          Principal principal) {
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
