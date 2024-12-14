package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;
import http.BaseHttpHandler;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(InMemoryTaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "tasks");
        switch (endpoint) {
            case GET: {
                handleGetTasks(exchange);
                break;
            }
            case POST: {
                handlePostTasks(exchange);
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

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = manager.getListTasks().stream().map(Task::toString).collect(Collectors.joining("\n"));
        writeResponse(exchange, response, 200);
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        Optional<Task> taskBody = parseTask(exchange.getRequestBody(), manager);
        Task taskNew = taskBody.get();
        exchange.sendResponseHeaders(201, 0);
        writeResponse(exchange, "Задача добавлена", 201);
        for (Task task : manager.getListTasks()) {
            if (taskNew.getId() == task.getId()) {
                writeResponse(exchange, "Задача с идентификатором " + task.getId() + "уже существует", 404);
            }
        }
        manager.getListTasks().add(taskNew);
        writeResponse(exchange, "Задача добавлена", 201);
    }
}