package org.darccona.controller;

import org.darccona.database.entity.NoticeEntity;
import org.darccona.database.entity.RecordEntity;
import org.darccona.database.entity.UserEntity;
import org.darccona.database.repository.NoticeRepository;
import org.darccona.database.repository.RecordRepository;
import org.darccona.database.repository.UserRepository;
import org.darccona.model.StringModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Класс контроллера обработки действий пользователя
 */
@Controller
public class ActionsController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * Обрабатывает добавление новой записи
     * @param string текст новой записи
     * @param link ссылка на страницу, с которой пользователь добавил новую запись
     * @param principal данные пользователя, добавившего новую запись
     * @return переправляет на страницу, с которой пользователь добавил новую запись
     */
    @PostMapping("/blog/addRecord")
    public String recordAdd(@ModelAttribute("string") StringModel string,
                            @RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = new RecordEntity(string.getText());
        record.setUser(user);
        recordRepository.save(record);
        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    /**
     * Обрабатывает редавтирование записи
     * @param string новый текст записи
     * @param id номер изменяемой записи
     * @param link ссылка на страницу, с которой пользователь меняет запись
     * @param principal данные пользователя, меняющего запись
     * @return переправляет на страницу, с которой пользователь меняет запись
     */
    @PostMapping("/blog/editRecord")
    public String recordEdit(@ModelAttribute("editString") StringModel string,
                             @RequestParam(value = "id") long id,
                             @RequestParam(value = "link", required = false, defaultValue = "") String link,
                             Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        RecordEntity record = recordRepository.findById(id);

        if (record.getUser() == user) {
            record.setText(string.getText());
            recordRepository.save(record);
        }

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }

    /**
     * Обрабатывает удаление записи
     * @param id номер удаляемой записи
     * @param link ссылка на страницу, с которой пользователь удаляет запись
     * @param principal данные пользователя, удаляющего запись
     * @return переправляет на страницу, с которой пользователь удалил запись
     */
    @GetMapping("/blog/delRecord")
    public String recordDel(@RequestParam(value = "id") long id,
                            @RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal) {
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

    /**
     * Обрабатывает удаление уведомления
     * @param id номер удаляемого уведомление
     * @param principal данные пользователя, удаляющего уведомление
     * @return переправляет на страницу пользователя или запись в зависимости от типа уведомления
     */
    @GetMapping("/blog/delMes")
    public String mesDel(@RequestParam(value = "id") long id,
                         Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        NoticeEntity notice = noticeRepository.findById(id);
        String url = "http://localhost:8080/blog/";

        if (notice.getType() == 1) {
            url += "userRecord?name=" + notice.getAuthor();
        } else {
            url += "userRecord/record?id=" + notice.getRecord();
        }

        user.removeNotice(notice);
        userRepository.save(user);

        return "redirect:" + url;
    }

    /**
     * Обрабатывает удаление всех уведомлений
     * @param link ссылка на страницу, с которой пользователь удаляет все уведомления
     * @param principal данные пользователя, удаляющего уведомления
     * @return переправляет на страницу, с которой пользователь удалил уведомления
     */
    @GetMapping("/blog/delAllMes")
    public String mesAllDel(@RequestParam(value = "link", required = false, defaultValue = "") String link,
                            Principal principal) {
        UserEntity user = userRepository.findByName(principal.getName());
        user.removeAllNotice();
        userRepository.save(user);

        String url = "http://localhost:8080/blog/" + link;

        return "redirect:" + url;
    }
}
