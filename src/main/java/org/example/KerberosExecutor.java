package org.example;

import com.sun.security.auth.callback.TextCallbackHandler;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivilegedAction;

public class KerberosExecutor {

    static Subject subject;

    /**
     * Prepared network configuration for test request with Kerberos authentication based
     *
     * @throws Exception
     */

    public static void init() throws Exception {
        propertiesSetup();

        subject = getAuthContext();
    }

    /**
     * Execute actions with retrieved during init() credentials
     *
     * @param action - action to perform
     */
    public static void executePrivilegedAction(PrivilegedAction action) {
        Subject.doAs(subject, action);
    }

    /**
     * Sets environment for JAAS.
     *
     * @throws FileNotFoundException - if no config file found next to the executable JAR
     */
    private static void propertiesSetup() throws FileNotFoundException {

        String PATH_TO_CONFIG = "auth.config";

        File file = new File(PATH_TO_CONFIG);
        if (!file.exists()) {
            throw new FileNotFoundException("Please ensure auth.config in the same directory as the executable jar");
        }

        System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("java.security.auth.login.config", PATH_TO_CONFIG);
        System.setProperty("java.security.krb5.conf", "krb5.conf");
    }

    static Subject getAuthContext() throws Exception {
        try {
            LoginContext lc = new LoginContext("Client", new TextCallbackHandler());
            lc.login();
            System.out.println("Making Context done");
            return lc.getSubject();
        } catch (Exception le) {
            System.out.println("LoginContext wasn't created. Kerberos authentication impossible.");
            throw le;
        }
    }
}
