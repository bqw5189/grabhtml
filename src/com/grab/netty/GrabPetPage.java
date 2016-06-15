package com.grab.netty;


import com.grab.html.CssGrabPage;
import com.grab.html.GrabPage;
import com.grab.html.GrabUtils;
import com.grab.html.PageFileEntity;
import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springside.modules.test.selenium.Selenium2;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bqw on 14-3-24.
 */
public class GrabPetPage {
    public static final Set<String> GRAB_PAGE = new TreeSet<String>();

    public static final Set<String> NOT_FINSH_GRAB_PAGE = new TreeSet<String>();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final String HTTP_HOST = "http://yun.tuoruimed.com/help/";
    private static HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(true);
    private static Selenium2 selenium = new Selenium2(htmlUnitDriver,HTTP_HOST);
    private static final Set<String> FILENAMES = new TreeSet<String>();


    public static void main(String[] args) throws IOException {
        String pageUrl = HTTP_HOST + "index.html";
        selenium.open(pageUrl);
        selenium.waitForVisible(By.className("LableItem"), 5);

        List<WebElement> spans = selenium.findElements(By.className("LableItem"));

        for (WebElement span: spans){
            span.click();
        }

        selenium.waitForTextPresent(By.id("675796974213"), "短信与微信营销视频", 10);

        spans = selenium.findElements(By.className("LableItem"));

        for (WebElement span: spans){
            if ("http://yun.tuoruimed.com/help/image/p.ico".equals(span.getAttribute("src")))
            span.click();
        }

        selenium.waitForTextPresent(By.id("699674929725"), "应用参数配置", 10);

        try {
            FileUtils.writeStringToFile(new File("/work/001_code/github/java/grabhtml/html/petclient/yun.tuoruimed.com/help/" + "index" + ".html"),selenium.getDriver().getPageSource(),"gbk");
            FILENAMES.add("index");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("pageSource=" + selenium.getDriver().getPageSource());

        List<WebElement> divSpans = selenium.findElements(By.className("span"));
        clickSpan(divSpans);
    }

    public synchronized static void clickSpan(List<WebElement> spans){
        int i = 1;
        for (WebElement span: spans){
            String pageName = null;
            try {
                pageName = span.getText().trim();
            }catch (Exception e){
                continue;
            }

            try {
                if (FILENAMES.contains(pageName)) {
                    continue;
                }
            }catch (Exception e){
                continue;
            }

            span.click();


            System.out.println("--------------------------"+pageName+"--------------------------");
            try {
                selenium.waitForTextPresent(By.id("Title"), pageName, 5);

//                System.out.println("pageSource=" + selenium.getDriver().getPageSource());

            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                Document document = Jsoup.parse(selenium.getDriver().getPageSource());
                Elements imgs = document.select("img");
                for (Element img: imgs){
                    if (img.attr("src").startsWith("http")){
                        img.attr("src", "data:image/png;base64," + GetImageStr(img.attr("src")));
                    }
                }
                FileUtils.writeStringToFile(new File("/work/001_code/github/java/grabhtml/html/petclient/yun.tuoruimed.com/help/" + i++ + "-" + pageName + ".html"),document.html(),"gbk");
                FILENAMES.add(pageName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("--------------------------"+pageName+"--------------------------");
        }
    }

    public static String GetImageStr(String url) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            data = IOUtils.toByteArray(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }



}
