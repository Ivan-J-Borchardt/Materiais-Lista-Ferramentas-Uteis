1. [Caso Necessário] Instalar ActiveMQ

- Download 
~~~
    https://activemq.apache.org/components/classic/download/classic-05-15-16
~~~

- Configuracao de usuários: 
~~~
    C:\apache-activemq-5.15.16\conf\users.properties
~~~

- Executando o Activemq: Abra a pasta C:\apache-activemq-5.15.16\bin em um terminal de comando e execute o seguinte comando: 
~~~
    activemq.bat start
~~~


- **Referencias ActiveMQ** 

https://activemq.apache.org/

https://activemq.apache.org/components/classic/download/classic-05-15-16

https://activemq.apache.org/components/classic/documentation/web-console#:~:text=Go%20to%20the%20%24%7BACTIVEMQ_HOME%7D%2Fconf%2Fjetty.xml%20and%20find%20the%20following,it%20to%20%3Cproperty%20name%3D%22authenticate%22%20value%3D%22true%22%20%2F%3E%20That%E2%80%99s%20it.

https://activemq.apache.org/components/classic/documentation/version-5-initial-configuration

- Tutorial Spring Boot  with Apache ActiveMQ
https://www.youtube.com/watch?v=c-Bfp3Zo2u0&t=84s


2. Copie o arquivo .jar do ActiveMQ para a pasta lib do Dopix:

De:
~~~
    C:\apache-activemq-5.15.16\activemq-all-5.15.16.jar
~~~

Para:
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\lib\activemq-all-5.15.16.jar
~~~


3. Crie um arquivo de propriedades com as propriedades do JMS-Connector 

~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\config\CB1536308ThinListener.properties
~~~

~~~
# ------------------------------------------------------------------------------
# JMS Environment
# ------------------------------------------------------------------------------
JMSConnector.dope.client.connector.2.class                          = de.icongmbh.dope.appserv.connection.QueueBasedJmsConnector
JMSConnector.dope.client.connector.2.prefix                         = jms
JMSConnector.dope.client.connector.2.jms.initialContextFactory      = org.apache.activemq.jndi.ActiveMQInitialContextFactory
JMSConnector.dope.client.connector.2.jms.providerUrl                = tcp://localhost:61616
JMSConnector.dope.client.connector.2.jms.queueConnectionFactoryName = ConnectionFactory
JMSConnector.dope.client.connector.2.jms.queueReceive               = dynamicQueues/JMSQueueToDOPE
JMSConnector.dope.client.connector.2.jms.queueSend                  = dynamicQueues/JMSQueueFromDOPE
JMSConnector.dope.client.connector.2.jms.correlationId              = Horrible
JMSConnector.dope.client.connector.2.jms.selector                   = JMSCorrelationID = 'Horrible' and JMSPriority > 3
JMSConnector.dope.client.connector.2.jms.timeout                    = 20
JMSConnector.dope.client.connector.2.jms.expiryTime                 = 0
JMSConnector.dope.client.connector.2.jms.err.initialContextFactory  = org.apache.activemq.jndi.ActiveMQInitialContextFactory
JMSConnector.dope.client.connector.2.jms.err.providerUrl            = tcp://localhost:61616
JMSConnector.dope.client.connector.2.jms.err.queueConnectionFactoryName = ConnectionFactory
JMSConnector.dope.client.connector.2.jms.err.queueReceive               = dynamicQueues/JMSQueueError
JMSConnector.dope.client.connector.2.jms.err.queueSend                  = dynamicQueues/JMSQueueError
JMSConnector.dope.client.connector.2.jms.err.correlationId              = Horrible
JMSConnector.dope.client.connector.2.jms.err.timeout                    = 20
JMSConnector.dope.client.connector.2.jms.err.expiryTime                 = 0   
~~~

4. Ative as propriedades no arquivo login_mobile.bat
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\bin\mobile\login_mobile.bat
~~~

~~~
    set DOPENV=login,%DOPSTAGE%,logConsoleFile,licFile,connIp,JMSConnector
    set DOPJVM_OPTS=%DOPJVM_OPTS% -Ddope.properties.filename=C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\config\CB1536308ThinListener.properties
~~~

5. Execute o dopix 

- DBManager 
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopixdb\bin\mobile
~~~

- Dopix App 
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\bin\mobile\login_mobile.bat
~~~


6. Crie as filas MQ no ActiveMQ

http://localhost:8161/admin/

- Informe o nome da Queue a ser criada no Textbox "Queue Name" abaixo da aba "Queues" e clique "Create"

O Dopix trabalha com 3 filas:

- JMSQueueToDOPE
- JMSQueueFromDOPE
- JMSQueueError

7. Envie um Request ao Dopix através da Queue JMSQueueToDOPE

- CorrelationID = 'Horrible' and Priority > 3

- Request: 
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.47-SNAPSHOT\dopix_mobile\mobile\dopix\util\examples\documentation
~~~

~~~XML
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!-- ###BUILDINFORMATION### -->
<DOPE-DIALOG version="1.0" xmlns="http://www.icongmbh.de">
  <!-- Anfrage mit DOPiX Benutzer 'ADMIN' -->
  <login>
    <loginId>ADMIN</loginId>
    <password><!-- verschlüsseltes und Base64-codiertes Passwort--></password>
  </login>
  <request>
    <!-- Texterstellung mit der Dokumentvorlage 'ICVLTD' starten. -->
    <!-- Der Modus 'EXEC' kennzeichnet eine neue Texterstellung.  -->
    <app id=""
             mode="INTERN"
			 function="NOP">
      <!-- Aktiviert den Batchdruck in der Versandsteuerung. -->
      <var id="DOPTIMEOUT" v="1000"/>
    </app>
  </request>
</DOPE-DIALOG>
~~~

- Verifique que o Response do Dopix estará disponível na fila JMSQueueFromDOPE



### Documentacao complementar 
https://dope.icongmbh.de/cb/issue/1030288