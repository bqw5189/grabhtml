package com.grab.netty;


import com.grab.html.CssGrabPage;
import com.grab.html.GrabPage;
import com.grab.html.GrabUtils;
import com.grab.html.PageFileEntity;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bqw on 14-3-24.
 */
public class Grab {
    public static final Set<String> GRAB_PAGE = new TreeSet<String>();

    public static final Set<String> NOT_FINSH_GRAB_PAGE = new TreeSet<String>();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {

        String pageUrl = GrabUtils.HTTP_HOST + GrabUtils.INDEX_PAGE;
        grabPage(pageUrl);

        while (true) {
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (NOT_FINSH_GRAB_PAGE.size() == 0) {
                EXECUTOR_SERVICE.shutdown();
                break;
            }

            System.out.println("garb... page :" + NOT_FINSH_GRAB_PAGE);

        }

    }


    public static void grabPage(String pageUrl) {
        System.out.println("grabPage:" + pageUrl);

        if (GRAB_PAGE.contains(pageUrl)) {
            System.out.println("grabed page url:" + pageUrl + " already grabed page number: " + GRAB_PAGE.size());
            return;
        }

        if (null == pageUrl) return;

        if (!(pageUrl.endsWith(".css") || pageUrl.endsWith(GrabUtils.PAGE_EXTRA)
                || pageUrl.endsWith(".js")
                || pageUrl.endsWith(".woff")
                || pageUrl.endsWith(".ttf")
                || pageUrl.endsWith(".svg")
                || pageUrl.endsWith(".png")
                || pageUrl.endsWith(".jpg")
                || pageUrl.endsWith(".gif")
                || pageUrl.endsWith(".eot"))) {
            System.out.println("error extent name!!!!!!!");
            return;
        }

        GRAB_PAGE.add(pageUrl);
        NOT_FINSH_GRAB_PAGE.add(pageUrl);

        PageFileEntity pageFileEntity = GrabUtils.mkdirs(pageUrl);

        GrabUtils.pageToFile(pageFileEntity);

        GrabPage grabPage = null;


        if (pageFileEntity.getFileName().endsWith(".css")) {
            grabPage = new CssGrabPage(pageFileEntity);
        } else if (pageFileEntity.getFileName().endsWith(GrabUtils.PAGE_EXTRA)) {
            grabPage = new HtmlGrabPageNetty(pageFileEntity);
        } else {
            NOT_FINSH_GRAB_PAGE.remove(pageFileEntity.getPageUrl());
        }

        if (null != grabPage) {
            EXECUTOR_SERVICE.execute(grabPage);
        }

    }

}
