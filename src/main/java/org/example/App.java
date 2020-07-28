package org.example;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;

/**
 * Kerberos authentication sample
 */
public class App {

    public static void main(String[] args) throws Exception {
        String url = getURLForTest(args);

        System.out.println("Trying retrieve data from " + url);
        acceptAllCookies();
        allowUnSecuredSSLConnections();
        KerberosExecutor.init();
        KerberosExecutor.executePrivilegedAction((PrivilegedAction<Object>) () -> {
            try {
                String response = HttpClient.doGet(url);
                System.out.println("Server response: \n" + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        System.out.println("\n \n Done");
    }

    /**
     * Generates a URL from args, otherwise constructs a login URL with a user's input
     *
     * @param args - application arguments
     * @return - URL for perform connection
     */
    private static String getURLForTest(String[] args) {
        String url = null;
        if (args.length == 0 || args[0] == null) {
            try {
                url = requestUserInput();
            } catch (Exception e) {
                System.err.println("Error during URL retriving " + e.getLocalizedMessage());
                System.exit(0);
            }
        } else {
            url = args[0];
        }
        return url;
    }

    //the tries of user input
    static int getConnectionCounter = 0;

    /**
     * Creates the credential request url based on the OpenTouch server dns name
     *
     * @return - the credential request url
     * @throws Exception - Malformed URL
     */
    static String requestUserInput() throws Exception {
        getConnectionCounter++;
        while (getConnectionCounter < 4) {
            try {
                System.out.println("Test of LOGIN to URL \n" + " please input server's DNS name (eg ot3net.ecc.tlab.alcatel.ru)");
                String userInput = new BufferedReader(new InputStreamReader(System.in)).readLine();
                userInput = userInput.isEmpty() ? "ot3net.ecc.tlab.alcatel.ru" : userInput;
                if (userInput.matches("\\b([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}\\b")) {
                    return "https://" + userInput + "/authenticationform/login?AlcApplicationUrl=/api/rest/authenticate%3fversion=1.0";
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                return requestUserInput();
            }
        }
        throw new Exception("The try limit exceeded");
    }

    /**
     * Allows store cookies during request with redirection.
     *
     *  For purposes when a requested URL do redirect to URL with cookies authentication.
     *  For example http://authrithationServer.company.com/authentication/login?redirect=/api/sessions
     */
    static private void acceptAllCookies() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }


    /**
     * Allows perform connection to remote server without ssl certificate validation
     */
    static private void allowUnSecuredSSLConnections() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {


                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = null;

            sc = SSLContext.getInstance("SSL");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
