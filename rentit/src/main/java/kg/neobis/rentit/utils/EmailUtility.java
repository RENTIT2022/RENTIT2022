package kg.neobis.rentit.utils;

import lombok.experimental.UtilityClass;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@UtilityClass
public class EmailUtility {

    public void sendConfirmationCodeToEmail(String recipientEmail, Integer code, JavaMailSender javaMailSender) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("neobis.team5@gmail.com");
        helper.setTo(recipientEmail);

        String subject = "[Rentit] Смена пароля";

        String content = "<p>Здравствуйте, " + recipientEmail + "</p>"
                + "<p>Вы запросили сбросить свой пароль.</p>"
                + "<p>Вот ваш код подтверждения: <strong>" + code + "</strong></p>"
                + "<br>"
                + "<p>Проигнорируйте это письмо, если вы помните свой пароль, "
                + "или не отправляли этот запрос.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }

    public void sendUserAcceptanceMessageToEmail(String recipientEmail, String name, JavaMailSender javaMailSender) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("neobis.team5@gmail.com");
        helper.setTo(recipientEmail);

        String subject = "[Rentit] Техническая Поддержка";

        String content = "<p>Хей <strong>" + name + "</strong>,</p>"
                + "<br>"
                + "<p>Добро пожаловать!</p>"
                + "<br>"
                + "<p>Спасибо что присоединились к RENTIT.KG</p>"
                + "<p>Теперь у вас больше возможностей на нашем приложении!</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }

    public void sendUserRejectionMessageToEmail(String recipientEmail, String name, JavaMailSender javaMailSender) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom("neobis.team5@gmail.com");
        helper.setTo(recipientEmail);

        String subject = "[Rentit] Техническая Поддержка";

        String content = "<p>Хей <strong>" + name + "</strong>,</p>"
                + "<br>"
                + "<p>Здравствуйте, " + recipientEmail + "!</p>"
                + "<br>"
                + "<p>Вы недавно заполнили форму для полной регистрации.</p>"
                + "<p>Ваши данные не прошли проверку!</p>"
                + "<p>Просьба, перепроверить ваши данные и заново пройти полную регистрация, для получения большего доступа на нашем приложении!</p>"
                + "<br>"
                + "<p>С уважением, RENTIT!</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        javaMailSender.send(message);
    }
}
