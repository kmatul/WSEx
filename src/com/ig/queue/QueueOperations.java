package com.ig.queue;


import java.net.MalformedURLException;
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
	 
	 public void getList() throws JMSException, NamingException, MalformedObjectNameException, OpenDataException,MalformedURLException
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
		  
		  ObjectName mbeanNameQueue = new ObjectName("org.apache.activemq:type=Broker,brokerName=static-broker1,destinationType=Queue,destinationName=GCDQ");
		  org.apache.activemq.broker.jmx.QueueViewMBean queueView = JMX.newMBeanProxy(connect(), mbeanNameQueue, org.apache.activemq.broker.jmx.QueueViewMBean.class);
		  System.out.println(queueView.browseAsTable());
		  
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
		  }
		  finally{
		  connection.close();
		  }
		 }
	 
	 public static void main(String[] args) throws JMSException {
		 try {
			 
			 QueueOperations temp = new QueueOperations();
//			 temp.pushToQueue(4545747, 575);
//			 temp.pushToQueue(62, 883);
			 temp.getList();
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	 
	 public MBeanServerConnection connect() 
		         {
		        JMXConnector connector = null;
		        MBeanServerConnection connection = null;

		        String username = "admin";

		        String password = "admin";

		        Map env = new HashMap();
		        String[] credentials = new String[] { username, password };
		        env.put(JMXConnector.CREDENTIALS, credentials);

		        try {
		            connector = JMXConnectorFactory.newJMXConnector(new 
		JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"), env);
		            connector.connect();
		            connection = connector.getMBeanServerConnection();
		        } catch (MalformedURLException e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        } catch (Exception e) {
		            // TODO Auto-generated catch block
		            e.printStackTrace();
		        }
		        
		        return connection; 
		    }
	
}
