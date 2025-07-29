
# Copiar um Schema dentro do mesmo Banco de dados  


**Descrição:** Como posso mover objetos DB2 de um esquema para outro esquema no mesmo banco de dados? Quando migrei um banco de dados para uma nova instância do DB2 com novo logon, não consegui acessar as tabelas.

**Resposta:** O que você realmente gostaria de fazer é renomear o esquema. O procedimento armazenado ADMIN_COPY_SCHEMA gerencia o processo. Nos bastidores, ADMIN_COPY_SCHEMA está, na verdade, copiando os objetos para outro esquema, em vez de apenas renomear o esquema.
Este exemplo de teste ilustra as etapas básicas. Existem outras opções mais avançadas, consulte a documentação para mais detalhes. Leia o manual do usuário do DB2 para obter notas de uso. Além disso, muito importante, sempre faça um BACKUP COMPLETO antes de usar ADMIN_COPY_SCHEMA.
Este exemplo copia o esquema 'MYSCHEM1' para 'MYSCHEM2'
 

~~~SQL
db2 connect to staccdb user db2admin using IconDBA$2024IconDBA

db2 "call SYSPROC.ADMIN_COPY_SCHEMA ('MYSCHEM1','MYSCHEM2','COPY', NULL, NULL, NULL, 'COPYSCHEMA', 'COPYERROR')"
db2 "call SYSPROC.ADMIN_COPY_SCHEMA ('DOPIXSTACC','CISTACC','COPY', NULL, NULL, NULL, 'COPYSCHEMA', 'COPYERROR')"
~~~

~~~ 
Value of output parameters
--------------------------
Parameter Name : ERRORTABSCHEMA
Parameter Value : COPYSCHEMA
 
Parameter Name : ERRORTABNAME
Parameter Value : COPYERROR

Return Status = 0
~~~ 

O uso de ADMIN_COPY_SCHEMA requer espaço de tabela systoolspace. Se não existir, crie-o antes de usar o procedimento ADMIN_COPY_SCHEMA. Um exemplo com ARMAZENAMENTO AUTOMÁTICO e saída esperada
 
~~~SQL
db2 "CREATE TABLESPACE SYSTOOLSPACE MANAGED BY AUTOMATIC STORAGE EXTENTSIZE 4"
~~~
~~~
DB20000I The SQL command completed successfully
~~~

Quando estiver satisfeito com o novo esquema, remova o esquema antigo usando o procedimento armazenado ADMIN_DROP_SCHEMA. Leia mais sobre DB2 - ADMIN_DROP_SCHEMA para descartar um esquema - DBA DB2 . Aqui está um exemplo:
 
~~~SQL
db2 "CALL SYSPROC.ADMIN_DROP_SCHEMA('TEST', NULL, 'ERRORSCHEMA', 'ERRORTABLE')"
~~~

~~~ 
Value of output parameters
--------------------------
Parameter Name : ERRORTABSCHEMA
Parameter Value : ERRORSCHEMA
 
Parameter Name : ERRORTAB
Parameter Value : ERRORTABLE 
Return Status = 0
~~~

### Obs., Erros e Problemas

- A procedure ADMIN_COPY_SCHEMA retornou erro ao rodar no **DBVisializer**. O erro informado faz referência ao parametro 7 da procedure. O problema foi resolvido rodando a procedure diretamente no "DB2 Command Window - Administrator". A aplicacao estava conectada ao Banco de dados 

- SQL0443N. O problema foi resolvido entrando com o nome do Schema de origem totalmente em maiusculo (DOPIXFLOW). 
~~~
C:\Program Files\IBM\SQLLIB\BIN>db2 ""call SYSPROC.ADMIN_COPY_SCHEMA ('dopixflow','dopixflowdb','COPY', NULL, NULL, NULL, 'COPYSCHEMA2', 'COPYERROR')""
SQL0443N  Routine "ADMIN_COPY_SCHEMA" (specific name "*COPY_SCHEMA") has
returned an error SQLSTATE with diagnostic text "Schema doesn't exist at
SYSCAT.SCHEMATA".  SQLSTATE=38000
~~~



### Referências: 
https://www.ibm.com/docs/en/db2/11.5?topic=procedure-admin-copy-schema-copy-schema

https://www.dba-db2.com/2013/06/move-db2-schema-objects-to-another-schema-using-admin_copy_schema.html
