package net.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Sender {
	private String from;
	private String to;
	private String title;
	private String content;
	public static void main( String[] args ) throws Exception{
		//从html表单中获取邮件信息
		String tfrom="fangdj@dgie.net";
		String tto="s-k-y@126.com";
		String ttitle="TEST Mail";
		String tcontent="Give me fire~";
		
		//JavaMail需要Properties来创建一个session对象。它将寻找字符串"mail.smtp.host"，属性值就是发送邮件的主机.
		//Properties对象获取诸如邮件服务器、用户名、密码等信息，以及其他可在整个应用程序中 共享的信息。
		
		Properties props=new Properties();
		props.put("mail.smtp.host","www.dgie.net");//存储发送邮件服务器的信息
		props.put("mail.smtp.auth","true");//同时通过验证
		
		
		//这个Session类代表JavaMail 中的一个邮件session. 每一个基于 JavaMail的应用程序至少有一个session但是可以有任意多的session。
		//Session类定义全局和每个用户的与邮件相关的属性。这此属性说明了客房机和服务器如何交流信息。
		
		
		Session s=Session.getInstance(props,null);//根据属性新建一个邮件会话，null参数是一种Authenticator(验证程序) 对象
		s.setDebug(true);//设置调试标志,要查看经过邮件服务器邮件命令，可以用该方法
		
		//  一旦创建了自己的Session对象，就是该去创建要发送的消息的 时候了。这时就要用到消息类型(MimeMessage是其中一种类型)。
		//　Message对象将存储我们实际发送的电子邮件信息，Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail session。
		//  Message类表示单个邮件消息，它的属性包括类型，地址信息和所定义的目录结构。
		
		Message message=new MimeMessage(s);//由邮件会话新建一个消息对象
		
		//message.setContent("hello","test/plain");//设置消息的内容类型,如果发送的格式有HTML格式就必须设置，
		//message.setText("Hello");//发送一般文本格式的消息
		
		//设置邮件,一旦您创建了 Session 和 Message，并将内容填入消息后，就可以用Address确定信件地址了。
		//如果想让一个名字出现在电子邮件地址后，也可以将其传递给构造器：
		//Address from=new InternetAddress("xmqds@21cn.com","qdison");//发件人的邮件地址
		
		Address from=new InternetAddress(tfrom);//发件人的邮件地址
		message.setFrom(from);//设置发件人
		
		Address to=new InternetAddress(tto);//收件人的邮件地址
		message.setRecipient(Message.RecipientType.TO,to);//设置收件人,并设置其接收类型为TO,还有3种预定义类型如下：
		
		
		message.setSubject(ttitle);//设置主题
		message.setText(tcontent);//设置信件内容
		message.setSentDate(new Date());//设置发信时间
		
		
		message.saveChanges();//存储邮件信息
		
		
		// Transport 是用来发送信息的，
		// 用于邮件的收发打操作。
		Transport transport=s.getTransport("smtp");
		transport.connect("www.dgie.net","fangdj","1111");//以smtp方式登录邮箱
		transport.sendMessage(message,message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址
		transport.close();

	}
}