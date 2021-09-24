package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Server {


    final int port;
    ExecutorService threadPool;
    final List<String> validPaths;

    public Server(List<String> validPaths, int port, ExecutorService threadPool) {

        this.validPaths = validPaths;
        this.port = port;
        this.threadPool = threadPool;


    }

    public void start() {

        try {
            final var serverSocket = new ServerSocket(port);
            while (true) {

                /*
                Можно было сделать как указано в задаче, просто передать метод класса Server,
                реализующий логику обработки подключения
                Примерно вот так
                 */
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        connectionProcess();
                    }
                });

                /*
                Но мне показалось изящнее вынести логику обработки подключения в отдельный класс
                имплементирующий runnable
                 */
                var socket = serverSocket.accept();
                threadPool.execute(new Connection(validPaths, socket));

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void connectionProcess() {
        System.out.println("Запрос обработан");
    }


}
