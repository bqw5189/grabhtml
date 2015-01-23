package com.grab.html;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bqw on 14-3-25.
 */
public class CssGrabPage implements GrabPage {
    private PageFileEntity pageFileEntity;

    public CssGrabPage(PageFileEntity pageFileEntity) {
        this.pageFileEntity = pageFileEntity;
    }

    public void grab() {

        File pageFile = GrabUtils.pageToFile(pageFileEntity);

        String pageResource = "";

        try {
            pageResource = FileUtils.readFileToString(pageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cssSource = pageResource.replaceAll(";", ";\n").replaceAll("}", "}\n");

        Pattern pat = Pattern.compile("url\\([\\s\\S]*?(\\))");
        Matcher mat = pat.matcher(cssSource);

        while (mat.find()) {
            String url = mat.group();
            System.out.println("css url:" + url);

            String indexChart = "'";

            if (url.indexOf("\"") > -1){
                indexChart = "\"";
            }

            int start = url.indexOf(indexChart) != -1 ? url.indexOf(indexChart) + 1:4;
            int end = url.lastIndexOf(indexChart) != -1 ? url.lastIndexOf(indexChart):url.length()-1;

            url = url.substring(start, end);
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.indexOf("#") > -1) {
                url = url.substring(0, url.indexOf("#"));
            }
            System.out.println("css urls is : " + url);

            if (url.startsWith("http://") || url.startsWith("https://")){
                Grab.grabPage(url);
            }else{
                Grab.grabPage(pageFileEntity.getCurrentUrl() + "/" + url);
            }
        }
        com.grab.netty.Grab.NOT_FINSH_GRAB_PAGE.remove(pageFileEntity.getPageUrl());

    }

    @Override
    public void run() {
        this.grab();
    }
}
