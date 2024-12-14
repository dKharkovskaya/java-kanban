package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.BaseHttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    public SubtasksHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "subtasks");
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
                handleDeleteTaskById(exchange, manager);
                break;
            }
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        String response = manager.getListSubTasks().stream()
                .map(Task::toString)
                .collect(Collectors.joining("\n"));
        writeResponse(exchange, response, 200);
    }

    private void handlePostSubTasks(HttpExchange exchange) throws IOException {
        Optional<Task> taskBody = parseTask(exchange.getRequestBody(), manager);
        Task taskNew = taskBody.get();
        exchange.sendResponseHeaders(201, 0);
        writeResponse(exchange, "Задача добавлена", 201);
        for (Task task : manager.getListSubTasks()) {
            if (taskNew.getId() == task.getId()) {
                writeResponse(exchange, "Задача с идентификатором " + task.getId() + "уже существует", 404);
            }
        }
        manager.getListTasks().add(taskNew);
        writeResponse(exchange, "Задача добавлена", 201);
    }

}
