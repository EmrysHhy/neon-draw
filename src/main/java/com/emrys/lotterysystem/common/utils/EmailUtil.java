package com.emrys.lotterysystem.common.utils;

import com.emrys.lotterysystem.controller.result.SendWinnerEmailResult;
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
        String content = buildLoginEmailContent(code);
        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * 发送中奖通知邮件
     */
    public void sendWinningNotification(SendWinnerEmailResult result)
            throws MessagingException, UnsupportedEncodingException {
        if (result == null) {
            return;
        }
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(from, "抽奖系统", "UTF-8"));
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(result.getTo());
        helper.setSubject("🎉 恭喜您中奖啦！— " + result.getActivityName());

        String content = buildWinningEmailContent(
                result.getWinnerName(),
                result.getActivityName(),
                result.getPrizeName(),
                result.getPrizeTierMessage(),
                result.getWinningTime());
        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
     * 构建美观的中奖通知邮件内容
     */
    private String buildWinningEmailContent(String winnerName, String activityName,
                                            String prizeName, String prizeTierMessage, String winningTime) {
        winnerName = escapeHtml(winnerName);
        activityName = escapeHtml(activityName);
        prizeName = escapeHtml(prizeName);
        prizeTierMessage = escapeHtml(prizeTierMessage);
        winningTime = escapeHtml(winningTime);

        return "<!DOCTYPE html>" +
                "<html lang=\"zh-CN\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>中奖通知</title>" +
                "</head>" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Microsoft YaHei', 'PingFang SC', sans-serif; background: linear-gradient(180deg, #fff8e7 0%, #f5f5f5 100%);\">" +
                "    <div style=\"max-width: 600px; margin: 24px auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 8px 32px rgba(255, 107, 53, 0.15);\">" +
                "        <div style=\"background: linear-gradient(135deg, #ff6b35 0%, #f7931e 50%, #ffd700 100%); padding: 40px 24px; text-align: center; position: relative;\">" +
                "            <div style=\"font-size: 48px; line-height: 1; margin-bottom: 12px;\">🏆</div>" +
                "            <h1 style=\"margin: 0; font-size: 32px; color: #ffffff; text-shadow: 0 2px 8px rgba(0,0,0,0.15); letter-spacing: 2px;\">恭喜中奖！</h1>" +
                "            <p style=\"margin: 12px 0 0; font-size: 16px; color: rgba(255,255,255,0.95);\">好运降临，幸运属于您</p>" +
                "        </div>" +
                "        <div style=\"padding: 36px 32px;\">" +
                "            <p style=\"font-size: 17px; color: #333; line-height: 1.8; margin: 0 0 8px;\">亲爱的 <strong style=\"color: #ff6b35;\">" + winnerName + "</strong>，您好！</p>" +
                "            <p style=\"font-size: 16px; color: #555; line-height: 1.8; margin: 0 0 28px;\">恭喜您在 <strong>" + activityName + "</strong> 中幸运中奖，以下是您的中奖详情：</p>" +
                "            <div style=\"background: linear-gradient(135deg, #fff9f0 0%, #fff3e0 100%); border: 2px solid #ffd700; border-radius: 12px; padding: 28px 24px; margin-bottom: 28px;\">" +
                "                <table style=\"width: 100%; border-collapse: collapse;\">" +
                "                    <tr>" +
                "                        <td style=\"padding: 10px 0; font-size: 14px; color: #999; width: 80px;\">奖品等级</td>" +
                "                        <td style=\"padding: 10px 0; font-size: 18px; font-weight: bold; color: #e65100;\">" + prizeTierMessage + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style=\"padding: 10px 0; font-size: 14px; color: #999; border-top: 1px dashed #ffe0b2;\">奖品名称</td>" +
                "                        <td style=\"padding: 10px 0; font-size: 18px; font-weight: bold; color: #333; border-top: 1px dashed #ffe0b2;\">" + prizeName + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style=\"padding: 10px 0; font-size: 14px; color: #999; border-top: 1px dashed #ffe0b2;\">活动时间</td>" +
                "                        <td style=\"padding: 10px 0; font-size: 15px; color: #666; border-top: 1px dashed #ffe0b2;\">" + activityName + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style=\"padding: 10px 0; font-size: 14px; color: #999; border-top: 1px dashed #ffe0b2;\">中奖时间</td>" +
                "                        <td style=\"padding: 10px 0; font-size: 15px; color: #666; border-top: 1px dashed #ffe0b2;\">" + winningTime + "</td>" +
                "                    </tr>" +
                "                </table>" +
                "            </div>" +
                "            <div style=\"text-align: center; margin: 24px 0;\">" +
                "                <span style=\"display: inline-block; padding: 14px 36px; font-size: 16px; font-weight: bold; color: #ffffff; background: linear-gradient(135deg, #ff6b35, #f7931e); border-radius: 50px; box-shadow: 0 4px 15px rgba(255, 107, 53, 0.4);\">🎊 再次恭喜您！</span>" +
                "            </div>" +
                "            <p style=\"font-size: 14px; color: #999; line-height: 1.7; margin: 0;\">请妥善保存此邮件，后续领奖凭证可能需要用到。如有疑问，请联系活动主办方。</p>" +
                "        </div>" +
                "        <div style=\"background-color: #fafafa; padding: 20px 24px; text-align: center; font-size: 12px; color: #bbb; border-top: 1px solid #f0f0f0;\">" +
                "            <p style=\"margin: 0 0 6px;\">此为系统自动发送的邮件，请勿直接回复</p>" +
                "            <p style=\"margin: 0;\">© 2026 抽奖系统 版权所有</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /**
     * 构建美观的登录验证码邮件内容
     */
    private String buildLoginEmailContent(String code) {
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
                "            <p>© 2026 您的公司名称 版权所有</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }
}


