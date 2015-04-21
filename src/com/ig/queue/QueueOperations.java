package com.ig.queue;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.ig.gcd.GcdCalc;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.OpenDataException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.*;


public class QueueOperations {
	
	InitialContext jndi = null;
	 public QueueOperations() throws JMSException, NamingException {

		 Properties env = new Properties( ); 
		 env.put(Context.INITIAL_CONTEXT_FACTORY, 
		 "org.apache.activemq.jndi.ActiveMQInitialContextFactory"); 
		 env.put(Context.PROVIDER_URL, 
				 ActiveMQConnection.DEFAULT_BROKER_URL); 
		 
		 
	  // Obtain a JNDI connection
	  jndi = new InitialContext(env);	  
	 }
	 


	 public boolean pushToQueue(int num1,int num2) throws JMSException, NamingException{
		// Look up a JMS connection factory
		  ActiveMQConnectionFactory conFactory = (ActiveMQConnectionFactory) jndi
		    .lookup("ConnectionFactory");
		  Connection connection;

		  // Getting JMS connection from the server and starting it
		  connection = conFactory.createConnection();
		  try {
		   connection.start();

		   // JMS messages are sent and received using a Session. We will
		   // create here a non-transactional session object. If you want
		   // to use transactions you should set the first parameter to 'true'
		   Session session = connection.createSession(false,
		     Session.AUTO_ACKNOWLEDGE);

//		   Destination destination = (Destination) jndi.lookup("GCDQ");
		   Destination destination = session.createQueue("GCDQ");

		   // MessageProducer is used for sending messages (as opposed
		   // to MessageConsumer which is used for receiving them)
		   MessageProducer producer = session.createProducer(destination);

		   // We will send a small text message saying 'Hello World!'
		   TextMessage message = session.createTextMessage(String.valueOf(num1));

		   // Here we are sending the message!
		   producer.send(message);
		   
		   message = session.createTextMessage(String.valueOf(num2));
		   ObjectMessage objMsg = session.createObjectMessage();

		   // Here we are sending the message!
		   producer.send(message);
		   System.out.println("Sent message '" + message.getText() + "'");
		   return true;
		   
		  }catch(Exception ex){
			  return false;
		  }
		  finally {		
		   connection.close();
		  }
	 }
	 
	 public ArrayList<String> getList()
			 throws JMSException, NamingException, MalformedObjectNameException, OpenDataException,MalformedURLException
	 {
		 QueueConnectionFactory connectionFactory = (QueueConnectionFactory) new  
				 ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
		  Connection connection = connectionFactory.createConnection();
		  
		  try{
		  connection.start();

		  // Creating session for seding messages
		  Session session = connection.createSession(false,
		    Session.AUTO_ACKNOWLEDGE);

		  // Getting the queue
		  Queue queue = null;
		  try{
		  queue = (Queue) jndi.lookup("GCDQ");
		  }catch(Exception ex){
			  
		  }
		  if(queue == null){
			  queue = session.createQueue("GCDQ");
		  }
		  
		  QueueBrowser qBrowser = session.createBrowser(queue);
		  
		  Enumeration msgs = qBrowser.getEnumeration();
			ArrayList<String> list = new ArrayList<String>();
	 		if ( !msgs.hasMoreElements() ) { 
			    return null;
			} else { 
			    while (msgs.hasMoreElements()) { 
			        Message tempMsg = (Message)msgs.nextElement();
			        try{
			        	String text = ((TextMessage)tempMsg).getText();
			        	list.add(text);
			        } catch(NumberFormatException e) {
			        	return null;
			        }
			    }
			}
			return list;
		  
		  }
		  finally{
		  connection.close();
		  }
	}
	 
	 public ArrayList<Integer> getGCDList()
			 throws JMSException, NamingException, MalformedObjectNameException, OpenDataException,MalformedURLException
	 {
		 QueueConnectionFactory connectionFactory = (QueueConnectionFactory) new  
				 ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
		  Connection connection = connectionFactory.createConnection();
		  
		  try{
		  connection.start();

		  // Creating session for seding messages
		  Session session = connection.createSession(false,
		    Session.AUTO_ACKNOWLEDGE);

		  // Getting the queue
		  Queue queue = null;
		  try{
		  queue = (Queue) jndi.lookup("GCDQ");
		  }catch(Exception ex){
			  
		  }
		  if(queue == null){
			  queue = session.createQueue("GCDQ");
		  }
		  
		  QueueBrowser qBrowser = session.createBrowser(queue);
		  
		  Enumeration msgs = qBrowser.getEnumeration();
			ArrayList<Integer> list = new ArrayList<>();
	 		if ( !msgs.hasMoreElements() ) { 
			    return null;
			} else { 
				
			    while (msgs.hasMoreElements()) { 
			        Message tempMsg = (Message)msgs.nextElement();
			        try{
			        	int num1 = Integer.parseInt(((TextMessage)tempMsg).getText());
			        	tempMsg = (Message)msgs.nextElement();
			        	if(tempMsg == null){
			        		list.add(num1);
			        		continue;
			        	}
			        	int num2 = Integer.parseInt(((TextMessage)tempMsg).getText());
			        	int gcdVal = GcdCalc.findGCD(num1, num2);
			        	list.add(gcdVal);
			        	
			        	
			        } catch(NumberFormatException e) {
			        	return null;
			        }
			    }
			}
			return list;
		  
		  }
		  finally{
		  connection.close();
		  }
	}
	 
	 public int gcd() throws Exception
	 {
		 ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
		  Connection connection = connectionFactory.createConnection();
		  
		  try{
		  connection.start();

		  // Creating session for seding messages
		  Session session = connection.createSession(false,
		    Session.AUTO_ACKNOWLEDGE);

		  // Getting the queue
		  Destination destination = session.createQueue("GCDQ");

		  
		  // MessageConsumer is used for receiving (consuming) messages
		  MessageConsumer consumer = session.createConsumer(destination);

		  // Here we receive the message.
		  // By default this call is blocking, which means it will wait
		  // for a message to arrive on the queue.
		  Message message = consumer.receive();
		  
		  ArrayList<Integer> list = new ArrayList<>();

		  // There are many types of Message and TextMessage
		  // is just one of them. Producer sent us a TextMessage
		  // so we must cast to it to get access to its .getText()
		  // method.
		  if (message instanceof TextMessage) {
		   TextMessage textMessage = (TextMessage) message;
		   System.out.println("Received message '" + textMessage.getText()
		     + "'");
		   list.add(Integer.parseInt(textMessage.getText()));
		  }
		  message = consumer.receive();
		  
		  if (message instanceof TextMessage) {
			   TextMessage textMessage = (TextMessage) message;
			   System.out.println("Received message '" + textMessage.getText()
			     + "'");
			   list.add(Integer.parseInt(textMessage.getText()));
		  }
		  if(list == null || list.size() != 2){
			  return -1;
		  }else{
			  return GcdCalc.findGCD(list.get(0), list.get(1));
		  }
		  }
		  finally{
			  connection.close();		  
		  }
	}
	 
	 public static void main(String[] args) throws JMSException {
		 try {
			 //System.out.println(GcdCalc.findGCD(7, 10));
			 			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 @Override
	protected void finalize() throws Throwable {
		
		super.finalize();
		try{
			 jndi.close();
		 }catch(Exception ex){
			 
		 }
	}
	 
//	class GcdNumber implements Serializable{
//		public int num1;
//		public int num2;
//		public GcdNumber(int a1, int a2) {
//			this.num1 = a1;
//			this.num2 = a2;
//		}
//		
//		public boolean pushToQueue(int num1,int num2) throws JMSException, NamingException{
//			// Look up a JMS connection factory
//			  ActiveMQConnectionFactory conFactory = (ActiveMQConnectionFactory) jndi
//			    .lookup("ConnectionFactory");
//			  Connection connection;
//
//			  // Getting JMS connection from the server and starting it
//			  connection = conFactory.createConnection();
//			  try {
//			   connection.start();
//
//			   // JMS messages are sent and received using a Session. We will
//			   // create here a non-transactional session object. If you want
//			   // to use transactions you should set the first parameter to 'true'
//			   Session session = connection.createSession(false,
//			     Session.AUTO_ACKNOWLEDGE);
//
////			   Destination destination = (Destination) jndi.lookup("GCDQ");
//			   Destination destination = session.createQueue("GCDQ");
//
//			   // MessageProducer is used for sending messages (as opposed
//			   // to MessageConsumer which is used for receiving them)
//			   MessageProducer producer = session.createProducer(destination);
//
//			   // We will send a small text message saying 'Hello World!'
//			   GcdNumber gcdObj = new GcdNumber(num1, num2);
////			   TextMessage message = session.createTextMessage(String.valueOf(num1));
//
//			   // Here we are sending the message!
////			   producer.send(message);
//			   
////			   message = session.createTextMessage(String.valueOf(num2));
//			   ObjectMessage objMsg = session.createObjectMessage(gcdObj);
//
//			   // Here we are sending the message!
//			   producer.send(objMsg);
//			   System.out.println("Sent message '" + objMsg.getIntProperty("num1") + "'");
//			   return true;
//			   
//			  }catch(Exception ex){
//				  return false;
//			  }
//			  finally {		
//			   connection.close();
//			  }
//		 }
//	}
	 
	
}
