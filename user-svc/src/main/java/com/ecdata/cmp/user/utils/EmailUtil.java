package com.ecdata.cmp.user.utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {

	public static String PERSONAL = "上海城投"; //邮件昵称
	//public static  String SMTPPORT = properties.getProperty("smtp.port");   //端口号 465  465  465   不是456



	/**
	 * @Author: Gaspar
	 * @Param: personal 邮件昵称
	 * @Param: smtpServer 发件人的邮箱的 SMTP 服务器地址
	 * @Param: usersEmail 送给多个用户多个用户用都好分割xxx@xx.com,xxx@xx.com
	 * @Description:
	 * @Date: 2020/3/6 17:03
	 */
	public static void  sendEmail(String title, String content, String personal, String smtpServer, String account, String pwd, String usersEmail) throws Exception{

			// 创建邮件配置
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
			props.setProperty("mail.smtp.host", smtpServer); // 发件人的邮箱的 SMTP 服务器地址
			//props.setProperty("mail.smtp.port", SMTPPORT);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.auth", "true"); // 需要请求认证
			props.setProperty("mail.smtp.ssl.enable", "true");// 开启ssl
			// 根据邮件配置创建会话，注意session别导错包
			Session session = Session.getDefaultInstance(props);
			// 开启debug模式，可以看到更多详细的输入日志
			session.setDebug(true);
			//创建邮件
			MimeMessage message = createEmail(personal, account, title, content, usersEmail, session);   //将用户和内容传递过来
			//获取传输通道
			Transport transport = session.getTransport();
			transport.connect(smtpServer,account, pwd);
			//连接，并发送邮件
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
	}

	public static MimeMessage createEmail(String personal, String account, String title, String content, String users, Session session) throws Exception {
		// 根据会话创建邮件
		MimeMessage msg = new MimeMessage(session);
		// address邮件地址, personal邮件昵称, charset编码方式
		InternetAddress fromAddress = new InternetAddress(account,
				PERSONAL, "utf-8");
		// 设置发送邮件方
		msg.setFrom(fromAddress);
//        单个可以直接这样创建
//        InternetAddress receiveAddress = new InternetAddress();
		// 设置邮件接收方
		Address[] internetAddressTo = new InternetAddress().parse(users);
		//type:
//		要被设置为 TO, CC 或者 BCC，这里 CC 代表抄送、BCC 代表秘密抄送。举例：Message.RecipientType.TO

		msg.setRecipients(MimeMessage.RecipientType.TO,  internetAddressTo);
		// 设置邮件标题
		msg.setSubject(title, "utf-8");
		msg.setText(content);
		// 设置显示的发件时间
		msg.setSentDate(new Date());
		// 保存设置
		msg.saveChanges();
		return msg;
	}

}