# Erro java.io.CharConversionException com DB2 

### Erro 
~~~LOG
2024-08-29 11:32:36.734 [E]  de.icongmbh.dope.dbserver.exceptions.DOPDbReplySQLException 13.1.0 (6139210e) preSerialize Thread-59 
  de.icongmbh.dope.dbserver.exceptions.DOPDbReplyException SNAPSHOT MC='99';RC='E';Message=SQL-Fehler: [jcc][t4][1065][12306][4.16.53] Bedingung java.io.CharConversionException abgefangen.  Details finden Sie im angehängten Element der Throwable-Klasse. ERRORCODE=-4220, SQLSTATE=null. RC=-4220 SQL-State=null
	at de.icongmbh.dope.dbserver.exceptions.DOPDbReplySQLException.createDOPDbReplySQLException22108(DOPDbReplySQLException.java:295)
	at de.icongmbh.dope.dbserver.process.DOPDbProcessGL.processRepository(DOPDbProcessGL.java:747)
	at de.icongmbh.dope.dbserver.process.DOPDbProcessGL.process(DOPDbProcessGL.java:347)
	at de.icongmbh.dope.dbserver.DOPDbRequestProcessor.executeProcess(DOPDbRequestProcessor.java:1230)
	at de.icongmbh.dope.dbserver.DOPDbRequestProcessor.worker(DOPDbRequestProcessor.java:474)
	at de.icongmbh.dope.dbserver.DOPDbRequestProcessorDefault.run(DOPDbRequestProcessorDefault.java:168)
Caused by: com.ibm.db2.jcc.am.SqlException: [jcc][t4][1065][12306][4.16.53] Bedingung java.io.CharConversionException abgefangen.  Details finden Sie im angehängten Element der Throwable-Klasse. ERRORCODE=-4220, SQLSTATE=null
	at com.ibm.db2.jcc.am.fd.a(fd.java:723)
	at com.ibm.db2.jcc.am.fd.a(fd.java:60)
	at com.ibm.db2.jcc.am.fd.a(fd.java:112)
	at com.ibm.db2.jcc.am.jc.a(jc.java:2870)
	at com.ibm.db2.jcc.am.jc.p(jc.java:527)
	at com.ibm.db2.jcc.am.jc.N(jc.java:1563)
	at com.ibm.db2.jcc.am.ResultSet.getStringX(ResultSet.java:1153)
	at com.ibm.db2.jcc.am.ResultSet.getString(ResultSet.java:1128)
	at de.icongmbh.dope.dbserver.MapDataFactory.MapColumnObject.getItem(MapColumnObject.java:538)
	at de.icongmbh.dope.dbserver.DataFactory.DataObject.AbsDataObject.copyFromResultSet(AbsDataObject.java:462)
	at de.icongmbh.dope.dbserver.DataFactory.DataObject.AbsDataObject.copyFromResultSet(AbsDataObject.java:483)
	at de.icongmbh.dope.dbserver.DbDataFactory.DbDataObject.AbsDbDataObject.load(AbsDbDataObject.java:266)
	at de.icongmbh.dope.dbserver.DbDataFactory.DbDataObject.RepositoryHeaderDbDataObject.load(RepositoryHeaderDbDataObject.java:204)
	at de.icongmbh.dope.dbserver.DbDataFactory.DbDataObject.RepositoryBaseDbDataObject.load(RepositoryBaseDbDataObject.java:339)
	at de.icongmbh.dope.dbserver.DbDataFactory.Container.AbsGLDbDataContainer.fillChildren(AbsGLDbDataContainer.java:393)
	at de.icongmbh.dope.dbserver.DbDataFactory.Container.AbsGLDbDataContainer.load(AbsGLDbDataContainer.java:229)
	at de.icongmbh.dope.dbserver.process.DOPDbProcessGL.processRepository(DOPDbProcessGL.java:740)
	... 4 more
Caused by: java.nio.charset.MalformedInputException: Input length = 292
	at com.ibm.db2.jcc.am.r.a(r.java:19)
	at com.ibm.db2.jcc.am.jc.a(jc.java:2862)
	... 17 more
Caused by: sun.io.MalformedInputException
	at sun.io.ByteToCharUTF8.convert(ByteToCharUTF8.java:105)
	at com.ibm.db2.jcc.am.r.a(r.java:16)
	... 18 more
~~~

### Análise

- No caso do cliente, foi feita uma carga de dados exportados de uma instalacao DB2 antiga e importada numa instalacao DB2 versao 11.5. No processo de descarga e carga de dados houve alguma incompatibilidade de tabela de caracteres o que fez com que o Diver JDBC na instalacao nova nao pudesse interpretar caracteres com tremas. 


- Documentacao IBM: 
https://www.ibm.com/support/pages/sqlexception-message-caught-javaiocharconversionexception-and-errorcode-4220

https://www.ibm.com/support/pages/db2-jdbc-driver-versions-and-downloads




~~~
  Alternative 1. Tabele löschen und alles wieder neu anlegen/laden.   
------------


  Alternative 2. Den Felerhaften Select-Befehl mit die funktion Hex ausführen, resultate andisch analysieren und mit Update-Befehl aktualisieren. 

 
  SELECT distinct H.DGIL_REC_IDE,
         H.DGIL_PROD_STAT,
		 H.DGIL_REC_VRS, 
		 H.DGIL_REC_TYPE,
		 H.ID, 
		 H.MAINTAIN_COPY
	FROM DOP_GL_BO H
	WHERE (H.DGIL_REC_TYPE = 'P  ')  
	  AND (H.DGIL_REC_IDE LIKE '%') 
	  AND ( MAINTAIN_COPY = '0' OR MAINTAIN_OWNER = 'ADMIN' )
	ORDER BY H.DGIL_REC_IDE ASC, 
	         H.DGIL_PROD_STAT ASC, 
			 H.DGIL_REC_TYPE ASC, 
			 H.DGIL_REC_VRS ASC
			 
-----

  SELECT distinct HEX(H.DGIL_REC_IDE),
         HEX(H.DGIL_PROD_STAT),
		 HEX(H.DGIL_REC_VRS), 
		 HEX(H.DGIL_REC_TYPE),
		 H.ID, 
		 HEX(H.MAINTAIN_COPY)
		 HEX(MAINTAIN_OWNER)
	FROM DOP_GL_BO H
	WHERE (H.DGIL_REC_TYPE = 'P  ')  
	  AND (H.DGIL_REC_IDE LIKE '%') 
	  AND ( MAINTAIN_COPY = '0' OR MAINTAIN_OWNER = 'ADMIN' )
	ORDER BY H.DGIL_REC_IDE ASC, 
	         H.DGIL_PROD_STAT ASC, 
			 H.DGIL_REC_TYPE ASC, 
			 H.DGIL_REC_VRS ASC
			 
-------------

  Alternative 3. Den JDBC Treiber swingen um die invaliden Characteren automatisch in unicode anzeigen (convertiert aber nur die anzeige, die gespeicherte daten bleiben weiterhin falsch)
  
  java -Ddb2.jcc.charsetDecoderEncoder=3 MyApp
  
  - beste stelle um die config. anzubringen? 
  
  dopixdb\config\wrapper\wrapper_environment_customer.conf
~~~

### Solucao no DOPIX: 

~~~
Dazu im \mobile\dopixdb\config\wrapper\mobile\wrapper_mobile_DB.conf
In Zeile 122 folgenden Eintrag einstellen.
wrapper.java.additional.4003=-Ddb2.jcc.charsetDecoderEncoder=3
~~~