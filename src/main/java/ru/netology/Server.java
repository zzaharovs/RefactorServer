package ru.netology;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int NUMBER_THREADS = 64;
    private final int port;
    private final ExecutorService threadPool;

    private final ConcurrentMap <String, ConcurrentMap<String, Handler>> handlersMap;


    public Server(int port) {

        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(NUMBER_THREADS);
        handlersMap = new ConcurrentHashMap<>();

    }

    public void start() {

        try {
            final var serverSocket = new ServerSocket(port);

            while (true) {

                /*
                Поменял все таки логику на второй вариант, оказалось неудобно :D
                 */
                var socket = serverSocket.accept();

                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        connection(socket);
                        connectionProcess();
                    }
                });

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void connectionProcess() {
        System.out.println("Запрос обработан");
    }

    public void connection(Socket socket) {

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

            final var requestLine = in.readLine();

            if (requestLine == null) {
                // just close socket
                return;
            }

            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                // just close socket
                return;
            }

            Request request = new Request(parts[0], parts[1], parts[2]);

            Handler currentHandler = searchHandler(request);

            if (currentHandler == null) {
                out.write((
                        "HTTP/1.1 404 Not Found\r\n" +
                                "Content-Length: 0\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                out.flush();
                return;
            }

            currentHandler.handle(request, out);

        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private Handler searchHandler(Request request) {

        return handlersMap.get(request.getStatus())
                .get(request.getPath());


    }

    public void addHandler(String method, String path, Handler handler) {

        if (!handlersMap.containsKey(method)) {
            handlersMap.put(method, new ConcurrentHashMap<>());
        }

        handlersMap.get(method)
                .put(path, handler);

    }

}
