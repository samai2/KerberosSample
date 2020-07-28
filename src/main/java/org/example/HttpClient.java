package org.example;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;


public class HttpClient {
    private static final String charset = "utf-8";
    private static String requestMethod;


    public static String doGet(String urlAdders) throws Exception {
        requestMethod = "GET";
        return performRequest(urlAdders);
    }

    private static String performRequest(String urlAdders) throws Exception {
        URLConnection connection = openConnection(urlAdders);

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setDoOutput(false);
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setRequestProperty("Accept-Charset", charset);

        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(0));

        return readData(httpURLConnection);
    }


    static String readData(HttpURLConnection httpURLConnection) throws Exception {
        StringBuilder resultBuffer = new StringBuilder();
        String tempLine;
        try (InputStream inputStream = httpURLConnection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            System.out.println("CONNECTION SUCCESSFUL");
        } catch (Exception e) {
            System.out.println("CONNECTION FAILED");
        }
        System.out.println(httpURLConnection.getResponseCode() +" " + httpURLConnection.getResponseMessage());
        for (Map.Entry<String, List<String>> entry : httpURLConnection.getHeaderFields().entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            System.out.println("Response header: " + key + " value = " + value);
        }
        return resultBuffer.toString();
    }

    static private URLConnection openConnection(String address) throws IOException {
        return new URL(address).openConnection();
    }

}
