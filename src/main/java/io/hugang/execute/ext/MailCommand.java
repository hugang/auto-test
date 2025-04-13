package io.hugang.execute.ext;

import cn.hutool.log.Log;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import io.hugang.bean.OriginalCommand;
import io.hugang.execute.Command;
import io.hugang.util.CommandExecuteUtil;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * mail command
 */
public class MailCommand extends Command {
    private static final Log log = Log.get();

    public MailCommand(OriginalCommand originalCommand) {
        super(originalCommand);
    }

    @Override
    public String getCommand() {
        return "mail";
    }

    @Override
    public boolean _execute() {
        String provider = getTarget();
        Setting setting = SettingUtil.get(CommandExecuteUtil.getFilePathWithBaseDir("conf/mail.conf"));

        MailSettings mailSettings = readMailSettings(setting, provider);
        if (mailSettings == null) {
            log.error("Failed to read mail settings for provider: " + provider);
            return false;
        }

        String mailTo = this.getDictStr("mailTo");
        String ccTo = this.getDictStr("mailCc");
        String subject = this.getDictStr("subject");
        String body = this.getDictStr("body");

        return sendMail(mailSettings, mailTo, ccTo, subject, body);
    }

    private MailSettings readMailSettings(Setting setting, String provider) {
        try {
            String username = setting.get(provider, "username");
            String password = setting.get(provider, "password");
            String host = setting.get(provider, "smtp.host");
            String port = setting.get(provider, "smtp.port");
            String ssl = setting.get(provider, "smtp.ssl");
            String auth = setting.get(provider, "smtp.auth");

            return new MailSettings(username, password, host, port, ssl, auth);
        } catch (Exception e) {
            log.error("Error reading mail settings: " + e.getMessage());
            return null;
        }
    }

    private boolean sendMail(MailSettings mailSettings, String mailTo, String ccTo, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailSettings.host);
        props.put("mail.smtp.port", mailSettings.port);
        props.put("mail.smtp.auth", mailSettings.auth);
        props.put("mail.smtp.ssl.enable", mailSettings.ssl);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSettings.username, mailSettings.password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSettings.username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            if (ccTo != null && !ccTo.isEmpty()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccTo));
            }
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            log.info("send mail success");
            return true;
        } catch (MessagingException e) {
            log.error("send mail error: " + e.getMessage());
            return false;
        }
    }

    private static class MailSettings {
        String username;
        String password;
        String host;
        String port;
        String ssl;
        String auth;

        MailSettings(String username, String password, String host, String port, String ssl, String auth) {
            this.username = username;
            this.password = password;
            this.host = host;
            this.port = port;
            this.ssl = ssl;
            this.auth = auth;
        }
    }
}
