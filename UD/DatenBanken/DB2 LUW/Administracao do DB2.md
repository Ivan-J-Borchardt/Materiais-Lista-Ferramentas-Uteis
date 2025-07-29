### Listar instâncias do DB2 
>$ db2ilist 

### Verificar configuracoes do DB2
>$ db2 get dbm cfg

### Listar todos os bancs de dados de uma instancia
>$ db2 list database directory

### Alterar a instancia do db2
>$ set DB2INSTANCE=instname

### Alterar a porta IP do BD2Server

1. Edite o arquivo services com um editor de textos
~~~
    Linux® and UNIX operating systems
        /etc/services
    Windows operating systems
        %SystemRoot%\system32\drivers\etc\services
~~~

2. Altere a Pora IP da instância DB2 conforme exemplo abaixo 
~~~
    db2c_DB2	50000/tcp
~~~

3. Reinicie o DB2 

>$ db2stop force
>$ db2start

