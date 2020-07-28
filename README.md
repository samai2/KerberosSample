# Kerberos authentication sample
Application for console output contents of the protected with Kerberos URL.

For authorization, the application can use a ticket stored in the cache
 or request a new ticket from the KDC defined in krb5. conf

## Install
Place the auth.config configuration file next to the executable file. 

If the application runned not on Windows, or runned under a user other than the user who should log in on the remote server, an additional krb5.conf configuration file is required to determine the KDC for ticket request.
(MIT Kerberos Documentation [krb5.conf](https://web.mit.edu/kerberos/krb5-1.12/doc/admin/conf_files/krb5_conf.html).) File krb5.conf have to locate next to the executable jar.
 
## Run
Application could be runned in two modes:
 1)	Gets content for a specific URL
 2)	Gets AlcUserID from specified OpenTouch server

If there are no tickets on cache or tickets are not valid, application will ask input username and password for obtaining valid ticket.  
### Gets content for a specific URL
Run jar file with argument URL
```
java -jar app.jar https://url.secured.with.kerb
```

### Gets AlcUserID from specified OpenTouch server
Run jar file with no arguments 
```
java -jar app.jar

Test of LOGIN to URL
 please input server's DNS name (eg ot3net.ecc.tlab.alcatel.ru)
```
If you input empty string, aplication will use ot3net.ecc.tlab.alcatel.ru by default
 
## Troubleshooting
 
### If the application can't get a ticket from the cache, try these steps
 
 * For Java 7 and higher and Oracle JRE Install Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files [JCE 8 Download](https://www.oracle.com/java/technologies/javase-jce8-downloads.html)
 * Change registry Key to Allow Session Keys to Be Sent in Kerberos Ticket-Granting-Ticket ([Microsoft Support](https://support.microsoft.com/en-us/help/308339/registry-key-to-allow-session-keys-to-be-sent-in-kerberos-ticket-grant))
 
 * Delete the krb5cc* file in a folder with the current user name under C:\Users
 
    
 
