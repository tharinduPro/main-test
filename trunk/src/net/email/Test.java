package net.email;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Test {
	public static void main( String[] args ) throws Exception{
		//��html���л�ȡ�ʼ���Ϣ
		String tfrom="fangdj@dgie.net";
		String tto="s-k-y@126.com";
		String ttitle="TEST Mail";
		String tcontent="Give me fire~";
		
		//JavaMail��ҪProperties������һ��session��������Ѱ���ַ���"mail.smtp.host"������ֵ���Ƿ����ʼ�������.
		//Properties�����ȡ�����ʼ����������û������������Ϣ���Լ�������������Ӧ�ó����� �������Ϣ��
		
		Properties props=new Properties();
		props.put("mail.smtp.host","www.dgie.net");//�洢�����ʼ�����������Ϣ
		props.put("mail.smtp.auth","true");//ͬʱͨ����֤
		
		
		//���Session�����JavaMail �е�һ���ʼ�session. ÿһ������ JavaMail��Ӧ�ó���������һ��session���ǿ�����������session��
		//Session�ඨ��ȫ�ֺ�ÿ���û������ʼ���ص����ԡ��������˵���˿ͷ����ͷ�������ν�����Ϣ��
		
		
		Session s=Session.getInstance(props,null);//���������½�һ���ʼ��Ự��null������һ��Authenticator(��֤����) ����
		s.setDebug(true);//���õ��Ա�־,Ҫ�鿴�����ʼ��������ʼ���������ø÷���
		
		//  һ���������Լ���Session���󣬾��Ǹ�ȥ����Ҫ���͵���Ϣ�� ʱ���ˡ���ʱ��Ҫ�õ���Ϣ����(MimeMessage������һ������)��
		//��Message���󽫴洢����ʵ�ʷ��͵ĵ����ʼ���Ϣ��Message������Ϊһ��MimeMessage����������������Ҫ֪��Ӧ��ѡ����һ��JavaMail session��
		//  Message���ʾ�����ʼ���Ϣ���������԰������ͣ���ַ��Ϣ���������Ŀ¼�ṹ��
		
		Message message=new MimeMessage(s);//���ʼ��Ự�½�һ����Ϣ����
		
		//message.setContent("hello","test/plain");//������Ϣ����������,������͵ĸ�ʽ��HTML��ʽ�ͱ������ã�
		//message.setText("Hello");//����һ���ı���ʽ����Ϣ
		
		//�����ʼ�,һ���������� Session �� Message����������������Ϣ�󣬾Ϳ�����Addressȷ���ż���ַ�ˡ�
		//�������һ�����ֳ����ڵ����ʼ���ַ��Ҳ���Խ��䴫�ݸ���������
		//Address from=new InternetAddress("xmqds@21cn.com","qdison");//�����˵��ʼ���ַ
		
		Address from=new InternetAddress(tfrom);//�����˵��ʼ���ַ
		message.setFrom(from);//���÷�����
		
		Address to=new InternetAddress(tto);//�ռ��˵��ʼ���ַ
		message.setRecipient(Message.RecipientType.TO,to);//�����ռ���,���������������ΪTO,����3��Ԥ�����������£�
		
		
		message.setSubject(ttitle);//��������
		message.setText(tcontent);//�����ż�����
		message.setSentDate(new Date());//���÷���ʱ��
		
		
		message.saveChanges();//�洢�ʼ���Ϣ
		
		
		// Transport ������������Ϣ�ģ�
		// �����ʼ����շ��������
		Transport transport=s.getTransport("smtp");
		transport.connect("www.dgie.net","fangdj","1111");//��smtp��ʽ��¼����
		transport.sendMessage(message,message.getAllRecipients());//�����ʼ�,���еڶ�����������������õ��ռ��˵�ַ
		transport.close();

	}
}