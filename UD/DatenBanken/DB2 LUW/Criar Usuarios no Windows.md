# Criar usuarios para o DB2 no Windows

## Adding your user ID to the DB2ADMNS and DB2USERS user groups (Windows)
Last Updated: 2021-03-01

After successfully completing a DB2® installation, you now have to add users to the DB2ADMNS or the DB2USERS groups for users that need to run local DB2 applications and tools on the machine. The DB2 installer creates two new groups. You can either use a new name or accept the default names. The default group names are DB2ADMNS and DB2USERS.

**Before you begin**

- You must have installed a DB2 database product.
- You must have selected the Enable operating system security check box on the Enable operating system security for DB2 object panel during the installation of your DB2 database product.

**Procedure**

To add users to the appropriate group:

1. Click **Start** and select **Run**.
2. Type **lusrmgr.msc** and click **OK**.
3. Select **Local Users and Groups**.
4. Select **Users**.
5. Select the user you want to add.
6. Click **Properties**.
7. Click the **Member Of tab**.
8. Click **Add**.
9. Select the appropriate group.
10. Click **OK**.

**What to do next**  

If you did the install and chose not to enable the new security feature you can still do so post-install by running the **db2extsec.exe** command. Adding a user to a group takes effect the first time the user logs on after the user has been added. For example, if you add you user ID to the DB2ADMNS group, you need to log out and then log in again for this change to take effect.


### Referências

https://www.ibm.com/docs/en/db2/9.7?topic=pitw-adding-your-user-id-db2admns-db2users-user-groups-windows 

https://www.ibm.com/docs/en/db2/11.1?topic=windows-authentication-groups-domain-security 