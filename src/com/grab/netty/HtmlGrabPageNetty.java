package com.grab.netty;

import com.grab.html.GrabPage;
import com.grab.html.GrabUtils;
import com.grab.html.PageFileEntity;
import org.apache.commons.io.FileUtils;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

/**
 * Created by bqw on 14-3-25.
 */
public class HtmlGrabPageNetty implements GrabPage {
    private PageFileEntity pageFileEntity;

    public HtmlGrabPageNetty(PageFileEntity pageFileEntity) {
        this.pageFileEntity = pageFileEntity;
    }

    public void grab() {
        try {
            String resource = FileUtils.readFileToString(pageFileEntity.getFile());
            Parser parser = new Parser(resource);

            OrFilter orFilter = new OrFilter(new OrFilter(new TagNameFilter("script"), new TagNameFilter("img")), new OrFilter(new TagNameFilter("link"), new TagNameFilter("a")));

            NodeList nodeList = parser.extractAllNodesThatMatch(orFilter);

            for (NodeIterator i = nodeList.elements(); i.hasMoreNodes(); ) {
                TagNode node = (TagNode) i.nextNode();
                String link = null;
                if ("link".equalsIgnoreCase(node.getTagName())) {
                    link = node.getAttribute("href");
                } else if ("script".equalsIgnoreCase(node.getTagName()) || "img".equalsIgnoreCase(node.getTagName())) {
                    link = node.getAttribute("src");
                } else if ("a".equalsIgnoreCase(node.getTagName())) {
                    link = node.getAttribute("href");
                    if (null == link){
                        continue;
                    }
                    if (!link.endsWith(GrabUtils.PAGE_EXTRA)) {
                        link = null;
                    }
                    String dataTheme = node.getAttribute("data-theme");
                    if (null != dataTheme) {
                        link = node.getAttribute("data-theme");
                    }
                }

                if (null == link) {
                    continue;
                }

                System.out.println("link href: " + link);

                if (link.startsWith("http")) {
                    Grab.grabPage(link.replaceAll("&amp;", "&"));
                } else {
                    Grab.grabPage(pageFileEntity.getCurrentUrl() + "/" + link);
                }
            }

            com.grab.netty.Grab.NOT_FINSH_GRAB_PAGE.remove(pageFileEntity.getPageUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.grab();
    }
}
