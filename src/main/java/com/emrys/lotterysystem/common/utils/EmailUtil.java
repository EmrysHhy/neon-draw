package com.emrys.lotterysystem.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 *
 * @author Emrys
 * content:
 */
@Component
public class EmailUtil {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送验证码邮件
     */
    public void sendVerificationCode(String to, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        // 设置发件人（含中文别名）
        message.setFrom(new InternetAddress(from, "抽奖系统", "UTF-8"));
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("登录验证码");

        // 使用HTML模板
        String content = buildEmailContent(code);
        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * 构建美观的邮件内容
     */
    private String buildEmailContent(String code) {
        return "<!DOCTYPE html>" +
                "<html lang=\"zh-CN\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>验证码邮件</title>" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Microsoft YaHei', sans-serif; background-color: #f5f5f5;\">" +
                "    <div style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1);\">" +
                "        <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 20px; text-align: center; color: white;\">" +
                "            <h1 style=\"margin: 0; font-size: 28px;\">验证码通知</h1>" +
                "        </div>" +
                "        <div style=\"padding: 30px;\">" +
                "            <p style=\"font-size: 16px; color: #333; line-height: 1.6;\">尊敬的客户，您好！</p>" +
                "            <p style=\"font-size: 16px; color: #333; line-height: 1.6;\">您正在登录抽奖系统，您的验证码为：</p>" +
                "            <div style=\"text-align: center; margin: 30px 0;\">" +
                "                <span style=\"display: inline-block; padding: 12px 30px; font-size: 24px; font-weight: bold; letter-spacing: 5px; background-color: #f0f0f0; border-radius: 5px; color: #e74c3c;\">" + code + "</span>" +
                "            </div>" +
                "            <p style=\"font-size: 14px; color: #999; line-height: 1.6;\">验证码有效期5分钟，请及时使用。</p>" +
                "            <p style=\"font-size: 14px; color: #999; line-height: 1.6;\">如非本人操作，请忽略此邮件。</p>" +
                "        </div>" +
                "        <div style=\"background-color: #f9f9f9; padding: 20px; text-align: center; font-size: 12px; color: #999; border-top: 1px solid #eee;\">" +
                "            <p>此为系统邮件，请勿回复</p>" +
                "            <p>© 2024 您的公司名称 版权所有</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}


