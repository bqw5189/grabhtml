package com.grab.html;

import java.net.URISyntaxException;

/**
 * Created by bqw on 14-3-25.
 */
public interface GrabPage extends Runnable {
    public void grab() throws URISyntaxException;
}
