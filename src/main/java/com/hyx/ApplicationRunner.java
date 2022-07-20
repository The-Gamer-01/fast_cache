package com.hyx;

import com.hyx.remoting.server.NettyServer;

/**
 * @author hyx
 **/

public class ApplicationRunner {
    
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.open();
    }
}
