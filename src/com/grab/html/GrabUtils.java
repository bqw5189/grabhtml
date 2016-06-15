package com.grab.html;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 * Created by bqw on 14-3-25.
 */
public class GrabUtils {
    public static final String ROOT_DIR = "/work/001_code/github/java/grabhtml/html/petclient";
    public static final String HTTP_HOST = "http://amxui.novalx.com";
    public static final String INDEX_PAGE = "/ui.html";
    public static final String PAGE_EXTRA = ".html";
    public static final String SAVE_PAGE_EXTRA = ".html";

    /**
     * 将页面结果存储到文件
     *
     * @param pageUrl
     * @param filePath
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public static File pageToFile(String pageUrl, String filePath, String fileName) {
        if (fileName.endsWith(PAGE_EXTRA)) {
            fileName = fileName.replaceAll(PAGE_EXTRA, SAVE_PAGE_EXTRA);
        }
        File pageFile = new File(filePath + File.separator + fileName);

        try {
            if (!pageFile.exists()) {
                HttpEntity entity = getPageEntity(pageUrl);
//                String pageResource = EntityUtils.toString(entity);
//                System.out.println(pageResource);
//                pageResource = pageResource.replaceAll("\r\n", " ");
//                pageResource = pageResource.replaceAll("> ", "> ");
                entity.writeTo(new FileOutputStream(pageFile));
//                FileUtils.writeStringToFile(pageFile, pageResource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pageFile;
    }
    /**
     * 获取页面实体
     *
     * @param pageUrl
     * @return
     * @throws IOException
     */
    private static HttpEntity getPageEntity(String pageUrl) {
        try{
            Thread.sleep(3000l);
        }catch (Exception es){};
        // 创建HttpClient实例
        HttpClient  httpclient = new DefaultHttpClient();

        // 创建Get方法实例
        HttpGet httpgets = new HttpGet(pageUrl);
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpgets);
        } catch (IOException e) {
//            e.printStackTrace();
            try{
                Thread.sleep(3000l);
            }catch (Exception es){};
            System.out.println("Connection reset, recall :" + pageUrl);
            return getPageEntity(pageUrl);
        }

        HttpEntity entity = response.getEntity();

        return entity;
    }

    public static PageFileEntity mkdirs(String pageUrl) {
        PageFileEntity pageFileEntity = new PageFileEntity();
        pageFileEntity.setPageUrl(pageUrl);

        //获取 uri index.html
        String uri = pageUrl.replaceAll(HTTP_HOST, "");
        String filePath = ROOT_DIR;
        String fileName = uri;

        if (uri.indexOf("http://") > -1){
            uri = uri.replaceFirst("http://", "");
        }
        if (uri.indexOf("https://") > -1){
            uri = uri.replaceFirst("https://", "");
        }

        //包含目录
        if (uri.indexOf("/") > -1) {
            int fileNameIndex = uri.lastIndexOf("/");
            fileName = uri.substring(fileNameIndex);

            //创建目录
            filePath = ROOT_DIR + File.separator + uri.substring(0, fileNameIndex);

            File pageFile = new File(filePath);
            if (!pageFile.exists()) {
                pageFile.mkdirs();
            }

            System.out.println("mkdir :" + filePath);
        }

        pageFileEntity.setFileName(fileName);
        pageFileEntity.setFilePath(filePath);
        pageFileEntity.setUri(uri);

        return pageFileEntity;

    }

    public static File pageToFile(PageFileEntity pageFileEntity) {
        File file = pageToFile(pageFileEntity.getPageUrl(), pageFileEntity.getFilePath(), pageFileEntity.getFileName());
        pageFileEntity.setFile(file);
        return file;
    }

    /**
     * 获取页面源码
     *
     * @param pageUrl
     * @return
     */
    private static String getPageResource(String pageUrl) {
        String str = "";
        try {
            // 创建HttpClient实例
            HttpClient httpclient = new DefaultHttpClient();
            // 创建Get方法实例
            HttpGet httpgets = new HttpGet(pageUrl);
            HttpResponse response = httpclient.execute(httpgets);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                str = convertStreamToString(instreams);
                System.out.println("Do something");
                // Do not need the rest
                httpgets.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * 将页面流 转成 字符流
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
