package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.Endpoint;
import manager.InMemoryTaskManager;
import task.Task;
import http.BaseHttpHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private InMemoryTaskManager manager;
    private final List<Task> tasks;
    GsonBuilder gsonBuilder = new GsonBuilder();
    Gson gson;

    public TasksHandler(InMemoryTaskManager manager) {
        this.tasks = manager.getListTasks();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = BaseHttpHandler.getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod(), "tasks");
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
                BaseHttpHandler.handleDeleteTaskById(exchange, manager);
                break;
            }
            default:
                BaseHttpHandler.writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        String response = tasks.stream().map(Task::toString).collect(Collectors.joining("\n"));
        BaseHttpHandler.writeResponse(exchange, response, 200);
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        Optional<Task> taskBody = BaseHttpHandler.parseTask(exchange.getRequestBody(), manager);
        Task taskNew = taskBody.get();
        exchange.sendResponseHeaders(201, 0);
        BaseHttpHandler.writeResponse(exchange, "Задача добавлена", 201);
        for (Task task : tasks) {
            if (taskNew.getId() == task.getId()) {
                BaseHttpHandler.writeResponse(exchange, "Задача с идентификатором " + task.getId() + "уже существует", 404);
            }
        }
        manager.getListTasks().add(taskNew);
        BaseHttpHandler.writeResponse(exchange, "Задача добавлена", 201);
    }
}
