
# Copiar um Schema de um banco de dados para o outro dentro da mesma instância DB2  

- Rodar o camndo abaixo no "DB2 Command Window - Administrator"

~~~
    db2move dopixdb COPY -sn dopix -co target_db dopixtes -u dope -p IconDBA$2024IconDBA
    db2move dopix COPY -sn flo -co target_db dopixtes -u dope -p IconDBA$2024IconDBA
~~~


### Obs., Erros e Problemas

~~~
    **ERROR** SYSTOOLSPACE doesn't exist on the source database.
~~~

~~~SQL
    DB2 "CREATE TABLESPACE SYSTOOLSPACE MANAGED BY AUTOMATIC STORAGE EXTENTSIZE 4"
~~~


### Referencias

~~~
    db2move -help
~~~