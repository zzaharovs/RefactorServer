package ru.netology;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    public static final int PORT = 9999;


    public static void main(String[] args) throws IOException {

        Server server = new Server(PORT);

        server.addHandler("GET", "/spring.png", (request, responseStream) -> {

            try {

                final var filePath = Path.of(".", "public", request.getPath());
                final var mimeType = Files.probeContentType(filePath);
                final var length = Files.size(filePath);

                responseStream.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, responseStream);
                responseStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        server.start();

    }

}
