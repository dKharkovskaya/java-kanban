package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.util.stream.Collectors;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "history");
        switch (endpoint) {
            case GET: {
                handleGetHistoryTasks(exchange);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetHistoryTasks(HttpExchange exchange) throws IOException {
        String response = manager.getHistory().stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        writeResponse(exchange, response, 200);
    }
}
