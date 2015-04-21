Project -: Unico WebService Example
Dependencies -: Active MQ libs, JBoss EAP 6.2, Jersy libs


Rest Services

1.public String push(int i1,int i2):

 use:

	http://localhost:8080/RESTex/JMSService/push/122/2

For looking at queue status, Goto Active MQ JMS console
	http://localhost:8161/admin/queues.jsp


2.public List<Integer>list():

use:

	http://localhost:8080/RESTex/JMSService/list

It will return list of current values in the queue.


3.public int gcd():

Use SOAP UI to test it


4.public List<Integer> gcdList():


Use SOAP UI to test it

5.public int gcdSum():

Use SOAP UI to test it


Soap:

WSDL:
http://<server-ip>:8080/RESTEx/services/GCD?wsdl
