package com.grab.html;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tom on 16/11/9.
 */
public class CssGrabPageTest {

    @Test
    public void grab(){
        String pageResource = "";

        try {
            pageResource = FileUtils.readFileToString(new File("/work/001_code/github/java/grabhtml/html/taurus/css/stylesheets.css"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cssSource = pageResource.replaceAll(";", ";\n").replaceAll("}", "}\n");

//        process("url\\([\\s\\S]*?(\\))", cssSource);
        process("@import (\"[\\s\\S]*?\")", cssSource);

    }

    private void process(String express, String cssSource) {
        Pattern pat = Pattern.compile(express);
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

//            if (url.startsWith("http://") || url.startsWith("https://")){
//                Grab.grabPage(url);
//            }else{
//                Grab.grabPage(pageFileEntity.getCurrentUrl() + "/" + url);
//            }
        }
    }
}
