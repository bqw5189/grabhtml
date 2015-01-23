package com.grab.html;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by bqw on 14-3-24.
 */
public class Grab {
    public static final Set<String> GRAB_PAGE = new HashSet<String>();

    private static final SynchronousQueue<Runnable> QUEUE = new SynchronousQueue<Runnable>();

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        String pageUrl = GrabUtils.HTTP_HOST + GrabUtils.INDEX_PAGE;
        grabPage(pageUrl);

        while (true){
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("EXECUTOR_SERVICE..." + EXECUTOR_SERVICE.isShutdown());
        }

    }


    public static void grabPage(String pageUrl) {
        System.out.println("grabPage:" + pageUrl);

        if (GRAB_PAGE.contains(pageUrl)) {
            System.out.println("grabed page url:" + pageUrl + " already grabed page number: " + GRAB_PAGE.size());
            return;
        }

        GRAB_PAGE.add(pageUrl);

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

        PageFileEntity pageFileEntity = GrabUtils.mkdirs(pageUrl);

        GrabUtils.pageToFile(pageFileEntity);

        GrabPage grabPage = null;

        if (pageFileEntity.getFileName().endsWith(".css")) {
            grabPage = new CssGrabPage(pageFileEntity);
        }

        if (pageFileEntity.getFileName().endsWith(GrabUtils.PAGE_EXTRA)) {
            grabPage = new HtmlGrabPage(pageFileEntity);

        }


        if (null != grabPage){
            EXECUTOR_SERVICE.execute(grabPage);
        }

    }

}
