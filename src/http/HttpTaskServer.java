package http;

import com.sun.net.httpserver.HttpServer;
import handler.EpicsHandler;
import handler.HistoryHandler;
import handler.SubtasksHandler;
import handler.TasksHandler;
import manager.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    InMemoryTaskManager manager;

    public HttpTaskServer(InMemoryTaskManager manager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicsHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));

    }

    public void start() {
        httpServer.start(); // запускаем сервер
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }


}
