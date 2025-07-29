# Instalacao DOPIX 

1. Copiar o pacote que se deseja testar

- de: 
~~~
    Q:\DOPiX_4\snapshot
~~~

- para: 
~~~
    C:\IJB\Tests
~~~

- IMPORTANTE: Copiar tanto o arquivo *-SNAPSHOT.zip quanto o *-SNAPSHOT.-customizable_initial.zip !!! 


2. Descompactar os dois pacotes baixados 

3. Copiar o pacote customizable para dentro do pacote SNAPSHOT:

- de:
~~~ 
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT-customizable_initial\customizable
~~~

- para: 
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\customizable
~~~

4. Build

- Entrar em:
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\build\bin
~~~

- Executar o create.bat desejado: 

    webWAR_create.bat

- O script webWAR_create.bat irá criar uma nova pasta /dopix_web, esta nova pasta contém o pacore dope.war:
~~~
C:\IJB\Tests\dopix_mobile\mobile
~~~

5. Para fins de organizacao, copiar a pasta criada para dentro da pasta do pacote SNAPSHOT

~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_web
~~~

6. [Caso Necessário] Instalar o Apache Tomcat 

- https://tomcat.apache.org/download-90.cgi


7. Copiar o pacote dope.war para a pasta webapp do Tomcat. É esperado que o Tomcat desempacote o dope.war, dessa forma deve ser criada uma pasta /dope dentro da webapp
~~~
    C:\apache-tomcat-9.0.89\webapps
~~~


8. License

- Buscar uma cópia do arquivo dopix.lic:  
~~~
    https://dope.icongmbh.de/cb/dir/238314
~~~

- Copiar o arquivo dopix.lic para dentro de webapps/dope/WEB-INF/classes:
~~~
    C:\apache-tomcat-9.0.89\webapps\dope\WEB-INF\classes
~~~

- Habilitar a licenca no arquivo dope.properties adicionando o nome do arquivo de licenca à propriedade license: 
~~~
    licFile.dope.formatter.intern.dope.license = /dopix.lic
~~~

- Habilitar o Environment Prefix "licFile":  
Para habilitar um Environment Prefix é necessário adicioná-lo ao conteúdo da variável de ambiente da JavaVM "dope.env".  
No ambiente de testes isso foi feito alterando o Script startup.bat (foi criado um novo Script startupDOPIXWeb.bat). O Script Startup.bat básicamente apenas chama o script catalina.bat (este script se encarrega de rodar o Applet Container Tomcat).
Ao analisar os comentários de cabecalho do catalina.bat percebeu-se que ele lê o conteúdo da variável de ambiente JAVA_OPTS (esta é uma veriável de ambiente do SO) e entrega seu conteúdo para os argumentos da JVM
~~~
    set JAVA_OPTS=-Ddope.env=web,mobile,recording,licFile,JMSConnector
~~~


9. JMS-Connector 
-  [Caso Necessário] Instalar ActiveMQ
~~~
    https://activemq.apache.org/components/classic/download/classic-05-15-16
~~~
    
- Copiar o arquivo activemq-all-5.15.16.jar
De:  
~~~
    C:\apache-activemq-5.15.16\activemq-all-5.15.16.jar
~~~

Para:
~~~
  C:\apache-tomcat-9.0.89\webapps\dope\WEB-INF\lib
~~~

- Criar um arquivo de propriedades em /webapps/dope/WEB-INF/classes/ThinListener.properties para configurar o ThinListener do dopix: 

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

- Habilitar o Environment Prefix "JMSConnector": no Script startupDOPIXWeb.bat: 
~~~
    set JAVA_OPTS=-Ddope.env=web,mobile,recording,licFile,JMSConnector
~~~

- Adicionar o path do arquivo de propriedades à variável de ambiente JVM dope.properties.filename
~~~
    set JAVA_OPTS=-Ddope.properties.filename=/CB1536308ThinListener.properties -Ddope.env=web,mobile,recording,licFile,JMSConnector
~~~

10. Conteúdo final do Script startupDOPIXWeb.bat: 

~~~
    set JAVA_OPTS=-Ddope.properties.filename=/CB1536308ThinListener.properties -Ddope.env=web,mobile,recording,licFile,JMSConnector
    call catalina.bat start
~~~


11. Start - Executar o script startupDOPIXWeb.bat em: 

~~~
    C:\apache-tomcat-9.0.89\bin\startupDOPIXWeb.bat
~~~


12. Verificar o log de execucao e o properties resultante em: 

~~~
    C:\apache-tomcat-9.0.89\temp\dopix\log
~~~


### Anotacoes e dicas do Axel

Guten Morgen Ivan! Ich musste gestern über zweieinhalb Stunden warten, bis das Paket aus deinem Transferverzeichnis heruntergeladen war - möglicherweise hatte da Mathias bereits einen seiner berüchtigten, abendlichen Transfers von/nach Marseille angestoßen.
Wie dem auch sei, bei mir läuft dein Paket nun. Woran lag es? Letztlich an denselben Dingen, über die wir schon bei der Installation/Konfiguration des FatClient gestolpert waren (hätten wir uns die doch bloß notiert!):
- Passende Konfiguration für das DOPiX-Szenario mit JMS erstellen (wir hatten das für die FatClient-Installation in eine separate DOPE-Propertysdatei ausgelagert)
- Diese zusätzliche Konfigurationsdatei einbinden ...
- ... und auch wirksam machen (Stichwort: "Environment-Präfixe")
- Client-Jars für ActiveMQ hinzufügen
- Eine gültige DOPiX-Lizenz bereit stellen

Die von DOPiX geschriebenen Logs findet man übrigens in <installation_folder_of_tomcat>/temp/dopix/log/, d. h. das in unserer log4j2.xml referenzierte java.io.tmpdir wird von den Tomcat-Skripten auf das Unterverzeichnis temp im Root-Verzeichis der Tomcat-Installation gesetzt.
Du kannst ja erst einmal selbst versuchen, obige Änderungen in deiner Installation anzubringen.  Solltest du nicht weiterkommen, dann melde dich. Noch ein paar Tipps:
Lies den Kommentar im Kopf des Skripts catalina.bat (das wird von startup.bat aufgerufen) und überlege dir, wie und wo die die zusätzlichen Propertys einbinden und aktivieren kannst
Librarys mit disjunktem Code kannst du in ./webapps/dope/WEB-INF/classes ablegen
Weitere Konfigurationsdateien kannst du in ./webapps/dope/WEB-INF/config ablegen, ebenso die Lizenz