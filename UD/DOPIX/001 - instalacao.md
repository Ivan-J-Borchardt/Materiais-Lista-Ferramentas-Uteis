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

    - fatclient_create.bat 
    - flow_create.bat 
    - mobile_create.bat 

- O script *_create.bat irácriar uma nova pasta (neste exemplo foi executado o script mobile_create.bat), esta nova pasta é o Dopix "Instalado":
~~~
C:\IJB\Tests\dopix_mobile\mobile
~~~

5. Para fins de organizacao, copiar a pasta criada para dentro da pasta do pacote SNAPSHOT

~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile
~~~

6. JAVA 

- Método 1: Copiar o Java 8
    - de: 
    ~~~
        C:\IJB\Resources\JAVA\1.8\jdk8u352-b08
    ~~~

    - para: 
    ~~~
        C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\java_jdk_windows-x86-64
    ~~~

- Método 2: (incompleto)


~~~
    \dopix_mobile\mobile\dopixdb\config\wrapper\wrapper_environment_customer.conf
~~~

Habilitar a linha "wrapper.java.command=java"
~~~
# -------------------------------------------------------------------------------------------
# ---      _                   ___        _   _
# ---     | | __ ___   ____ _ / _ \ _ __ | |_(_) ___  _ __  ___
# ---  _  | |/ _` \ \ / / _` | | | | '_ \| __| |/ _ \| '_ \/ __|
# --- | |_| | (_| |\ V / (_| | |_| | |_) | |_| | (_) | | | \__ \
# ---  \___/ \__,_| \_/ \__,_|\___/| .__/ \__|_|\___/|_| |_|___/
# ---                              |_|

# --- NOTE:
# ---    This settings overwrites the main configuration "wrapper_base.conf"!
# ---    Some additional parameters will be set in other Configuration-Files!
# ---    Continues with next free counter "wrapper.<property>.<counter>".
# ---    See also Homepage for unescaped quotes.

# -- JAVA_HOME
# Relative path from current wrapper.working.dir to <JAVA_HOME>/bin/java
# NOTE:
#    Properties with absolute path: not recommanded
#    Properties with relative path: recommanded
# DEFAULT:
#    wrapper.java.command=../java_jdk_%WRAPPER_OS%-%WRAPPER_ARCH%-%WRAPPER_BITS%/bin/java

wrapper.java.command=java
~~~



7. Start do DBManager - Executar o script w_start_DB_mobile.bat:
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopixdb\bin\mobile\w_start_DB_mobile.bat
~~~

9. License

- Buscar uma cópia do arquivo dopix.lic:  
~~~
    https://dope.icongmbh.de/cb/dir/238314
~~~

- Copiar o arquivo dopix.lic para:
~~~
    C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\config
~~~

- Habilitar a licenca no arquivo dope.properties adicionando o nome do arquivo de licenca à propriedade license: 
~~~
    licFile.dope.formatter.intern.dope.license = /dopix.lic
~~~

- Habilitar o prefixo licFile no arquivo login_mobile.bat:
~~~
    set DOPENV=login,%DOPSTAGE%,logConsoleFile,licFile
~~~


10. Start - Executar o script login_mobile.bat em: 

~~~
C:\IJB\Tests\de.icongmbh.dopix.release.package-6.0.49-SNAPSHOT\dopix_mobile\mobile\dopix\bin\mobile
~~~