package org.example;


import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;


public class HttpClient {
    private static final String charset = "utf-8";
    private static String requestMethod;

    public static String doPost(String urlAdders, String content) throws Exception {
        requestMethod = "POST";
        return performRequest(urlAdders, content);
    }

    public static String doGet(String urlAdders) throws Exception {
        requestMethod = "GET";
        return performRequest(urlAdders, null);
    }

    private static String performRequest(String urlAdders, String content) throws Exception {
        URLConnection connection = openConnection(urlAdders);

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setUseCaches(true);
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(0));

        if (content != null) {
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(content.length()));
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            writeContent(httpURLConnection, content);
        } else {
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(0));
        }

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
        printConnectionProperties(httpURLConnection);
        return resultBuffer.toString();
    }

    private static void printConnectionProperties(HttpURLConnection httpURLConnection) throws IOException {
        System.out.println(httpURLConnection.getResponseCode() +" " + httpURLConnection.getResponseMessage());
        for (Map.Entry<String, List<String>> entry : httpURLConnection.getHeaderFields().entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            System.out.println("Response header: " + key + " value = " + value);
        }
    }

    static private URLConnection openConnection(String address) throws IOException {
        return new URL(address).openConnection();
    }

    private static void writeContent(HttpURLConnection connection, String content) throws HTTPException, IOException {
        try (OutputStream outputStream = connection.getOutputStream();
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {

            outputStreamWriter.write(content);
            outputStreamWriter.flush();

            if (connection.getResponseCode() >= 300) {
                printConnectionProperties(connection);
                throw new HTTPException(connection.getResponseCode());
            }
        }
    }
}
