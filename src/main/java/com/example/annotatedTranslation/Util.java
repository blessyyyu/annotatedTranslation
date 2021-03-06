package com.example.annotatedTranslation;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Util {

    public static String getUrlWithQueryString(String url, Map<String, String> params) {
        if (params == null) {
            return url;
        }

        StringBuilder builder = new StringBuilder(url);
        if (url.contains("?")) {
            builder.append("&");
        } else {
            builder.append("?");
        }

        int index = 0;
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) {
                continue;
            }
            if (index != 0) {
                builder.append("&");
            }
            index++;
            builder.append(key);
            builder.append("=");
            builder.append(encode(value));
        }
        return builder.toString();
    }

    private static String encode(String input) {
        if (input == null) {
            return "";
        }

        try {
            return URLEncoder.encode(input, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String md5(String input) {
        if (input == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = input.getBytes("utf-8");
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String md5(File file) {
        try {
            if (!file.isFile()) {
                System.err.println("??????" + file.getAbsolutePath() + "???????????????????????????");
                return null;
            }

            FileInputStream in = new FileInputStream(file);
            String result = md5(in);
            in.close();

            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5(InputStream in) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, read);
            }
            in.close();
            return byteArrayToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }


    public static String getResponse( String url)throws URISyntaxException, IOException, InterruptedException{
        URL url_x = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url_x.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(5000); // ????????????5???
        // ??????HTTP???:
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36");
        // ???????????????HTTP??????:
        conn.connect();
        // ??????HTTP????????????200:
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("bad response");
        }

        // ??????????????????:
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream input = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            input.close();
        }catch (Exception e){
            throw e;
        }
        return stringBuilder.toString();
    }

    /**
     *
     * @param url ?????????????????????ip
     * @param port ????????????????????????port
     * @return
     */
    public static SimpleClientHttpRequestFactory getFactory(String url, int port) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //?????????ms
        factory.setReadTimeout(10 * 1000);
        //?????????ms
        factory.setConnectTimeout(30 * 1000);
        // ?????????url?????????ip, port??????
        InetSocketAddress address = new InetSocketAddress(url, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        factory.setProxy(proxy);
        return factory;
    }


    /**
     * ????????????ip????????????
     *
     * @param ipBean
     * @return
     */
    public static boolean isValid(IPBean ipBean) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipBean.getIp(), ipBean.getPort()));
        try {
            URLConnection httpCon = new URL("https://www.baidu.com/").openConnection(proxy);
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(5000);
            int code = ((HttpURLConnection) httpCon).getResponseCode();
            System.out.println("????????????????????????" + code);
            return code == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
