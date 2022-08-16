package com.ivey.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hang.lv
 * @version 1.0.0
 * @since 2022/8/12 9:33
 */
public class BioServer {

    public static void main(String[] args) throws IOException {
        // 思路：
        // 1. 创建一个线程池
        // 2. 如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true) {
            System.out.println("线程信息 Id =" + Thread.currentThread().getId() + " 名字 =" + Thread.currentThread().getName());
            // 监听，等待客户端连接
            System.out.println("等待连接");

            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            // 就创建一个线程，与之通讯（单独写一个方法）
            newCachedThreadPool.execute(() -> {
                // 可以和客户端通讯
                handler(socket);
            });
        }
    }

    /**
     * 编写一个handler方法，和客户端通讯
     *
     * @param socket socket
     */
    private static void handler(Socket socket) {
        try {
            System.out.println("线程信息 Id =" + Thread.currentThread().getId() + " 名字 =" + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];

            // 通过 Socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            // 循环的读取客户端发送的数据
            while (true) {
                System.out.println("线程信息 Id =" + Thread.currentThread().getId() + " 名字 =" + Thread.currentThread().getName());
                System.out.println("read...");

                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                // 输出客户端发送的数据
                System.out.println(new String(bytes, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和Client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
