package org.darccona.config;

/**
 * Интерфейс для отправки электронных писем
 */
public interface EmailService {

    /**
     * Отправка электронных писем
     * @param toAddress адрес получателя
     * @param subject тема письма
     * @param message тело письма
     */
    void sendSimpleEmail(final String toAddress, final String subject, final String message);
}
