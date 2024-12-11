package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EpicsHandler implements HttpHandler {
    private InMemoryTaskManager manager;
    private final List<Task> epics;

    public EpicsHandler(InMemoryTaskManager manager) {
        this.epics = manager.getListEpicTasks();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = BaseHttpHandler.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "epics");
        switch (endpoint) {
            case GET: {
                handleGetSubTasks(exchange);
                break;
            }
            case POST: {
                handlePostSubTasks(exchange);
                break;
            }
            case DELETE: {
                BaseHttpHandler.handleDeleteTaskById(exchange, manager);
                break;
            }
            default:
                BaseHttpHandler.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        String response = epics.stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        BaseHttpHandler.writeResponse(exchange, response, 200);
    }

    private void handlePostSubTasks(HttpExchange exchange) throws IOException {
        Optional<Task> taskBody = BaseHttpHandler.parseTask(exchange.getRequestBody(), manager);
        Task taskNew = taskBody.get();
        exchange.sendResponseHeaders(201, 0);
        BaseHttpHandler.writeResponse(exchange, "Задача добавлена", 201);
        for (Task task : epics) {
            if (taskNew.getId() == task.getId()) {
                BaseHttpHandler.writeResponse(exchange, "Задача с идентификатором " + task.getId() + "уже существует", 404);
            }
        }
        manager.getListTasks().add(taskNew);
        BaseHttpHandler.writeResponse(exchange, "Задача добавлена", 201);
    }

}
