package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private List<Task> history;

    public HistoryHandler(InMemoryTaskManager manager) {
        this.history = manager.getHistory();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = BaseHttpHandler.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "history");
        switch (endpoint) {
            case GET: {
                handleGetHistoryTasks(exchange);
                break;
            }
            default:
                BaseHttpHandler.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetHistoryTasks(HttpExchange exchange) throws IOException {
        String response = history.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        BaseHttpHandler.writeResponse(exchange, response, 200);
    }
}
