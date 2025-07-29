# DOPE Dialog Request


### Exemplos de Requests: 
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


### Executando o Request

1. Ativando o connIp (prefixo das propriedades (dope.properties) usadas para parametrizar a execucao do Request).

- Edite o Script login_mobile.bat 
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\bin\mobile\login_mobile.bat
~~~

- Adicione o prefixo "connIp" na lista DOPENV: 
~~~
    set DOPENV=login,%DOPSTAGE%,logConsoleFile,licFile,connIp
~~~

2. Iniciar o Dopix executando script login_mobile.bat
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\bin\mobile\login_mobile.bat
~~~

3. Iniciar o Driver TCP/IP executando o script DriverTooIp.bat
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\util\DriverToolIp.bat
~~~

4. Ajustar os parametros de conexão do Driver de forma que reflitam as propriedades connIp em dope.properties. 

- clique em Settings no canto inferior direito, na caixa de dialogo que se abrirá, ajuste o "Server IP Address" e "Server IP Port".

5. Abrir o XML do request File>Open file.

6. Clique Send no canto inferiror direito.
- Em Reply será exibido o Response do Request. 