package com.grab.html;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;

/**
 * Created by bqw on 14-3-25.
 */
public class HtmlGrabPage implements GrabPage {
    private PageFileEntity pageFileEntity;

    public HtmlGrabPage(PageFileEntity pageFileEntity) {
        this.pageFileEntity = pageFileEntity;
    }

    public void grab() {

        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(false);
        System.out.println("start get page:" + pageFileEntity.getPageUrl());
        driver.get(pageFileEntity.getPageUrl());
        System.out.println("end get page:" + pageFileEntity.getPageUrl());

        //抓取 CSS
        List<WebElement> links = driver.findElements(By.tagName("link"));

        for (WebElement link : links) {
            final String linkAttribute = link.getAttribute("href");

            System.out.println("link href: " + linkAttribute);

            Grab.grabPage(linkAttribute);

        }

        //抓取 JS
        List<WebElement> scripts = driver.findElements(By.tagName("script"));

        for (WebElement script : scripts) {
            String scriptAttribute = script.getAttribute("src");

            System.out.println("script src: " + scriptAttribute);

            Grab.grabPage(scriptAttribute);
        }

        if (pageFileEntity.getPageUrl().endsWith(GrabUtils.INDEX_PAGE)) {
            //抓去 本站页面
            List<WebElement> aHrefs = driver.findElements(By.tagName("a"));
            for (WebElement a : aHrefs) {
                String href = a.getAttribute("href");

                if (null != href && href.endsWith(GrabUtils.PAGE_EXTRA)) {
                    Grab.grabPage(a.getAttribute("href"));
                }

                String dataTheme = a.getAttribute("data-theme");
                if (null != dataTheme) {
                    Grab.grabPage(pageFileEntity.getCurrentUrl() + a.getAttribute("data-theme"));
                }
            }

            //抓取 图片
            List<WebElement> imgs = driver.findElements(By.tagName("img"));

            for (WebElement img : imgs) {
                String imgAttribute = img.getAttribute("src");

                System.out.println("img src: " + imgAttribute);

                Grab.grabPage(imgAttribute);
            }
        }

        driver.close();
        driver.quit();

    }

    @Override
    public void run() {
        this.grab();
    }
}
