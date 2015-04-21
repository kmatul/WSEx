package com.ig.queue;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

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


public class JBQueueOperations {
	
	InitialContext jndi = null;
	String destinationName = "qGCD";
	 public JBQueueOperations() throws JMSException, NamingException {

		 Properties env = new Properties( ); 
		 env.put(Context.INITIAL_CONTEXT_FACTORY, 
		 "org.jnp.interfaces.NamingContextFactory"); 
		 env.put(Context.URL_PKG_PREFIXES,
			        " org.jboss.naming:org.jnp.interfaces");
		 env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		 
		 
	  // Obtain a JNDI connection
	  jndi = new InitialContext(env);	  
	 }
	 


	 public boolean pushToQueue(int num1,int num2) throws JMSException, NamingException{
		// Look up a JMS connection factory
		  ConnectionFactory conFactory = (ConnectionFactory) jndi
		    .lookup("/ConnectionFactory");
		  Connection connection;

		  // Getting JMS connection from the server and starting it
		  connection = conFactory.createConnection();
		  try {
			  Queue queue = (Queue)jndi.lookup(destinationName);
			  
	            connection = conFactory.createConnection();
	            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	            MessageProducer producer = session.createProducer(queue);
//	            MessageConsumer subscriber = session.createConsumer(queue);
	 
	            connection.start();
	 		   // We will send a small text message saying 'Hello World!'
		   TextMessage message = session.createTextMessage(String.valueOf(num1));

		   // Here we are sending the message!
		   producer.send(message);
		   
		   message = session.createTextMessage(String.valueOf(num2));

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
		  		  
		  Queue queue = (Queue) jndi.lookup("GCDQ");
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
	 
	 public int gcd() throws JMSException, NamingException, MalformedObjectNameException, OpenDataException,MalformedURLException
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

		  // There are many types of Message and TextMessage
		  // is just one of them. Producer sent us a TextMessage
		  // so we must cast to it to get access to its .getText()
		  // method.
		  if (message instanceof TextMessage) {
		   TextMessage textMessage = (TextMessage) message;
		   System.out.println("Received message '" + textMessage.getText()
		     + "'");
		  }
		  return 0;
		  }
		  finally{
		  connection.close();
		  
		  }
	}
	 
	 public static void main(String[] args) throws JMSException {
		 try {
			 
			 JBQueueOperations temp = new JBQueueOperations();
			 temp.pushToQueue(4545747, 575);
//			 temp.pushToQueue(62, 883);
			 //temp.getList();
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 
	
}
