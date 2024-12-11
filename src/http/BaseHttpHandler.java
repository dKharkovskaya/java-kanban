package http;

import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class BaseHttpHandler {

    public static Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static void writeResponse(HttpExchange exchange,
                                     String responseString,
                                     int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes());
        }
        exchange.close();
    }

    public static Endpoint getEndpoint(String requestPath, String requestMethod, String userPath) {
        String[] pathParts = requestPath.split("/");

        if (pathParts.length == 2 && pathParts[1].equals(userPath) && requestMethod.equals("GET")) {
            return Endpoint.GET;
        } else if (pathParts.length == 2 && pathParts[1].equals(userPath) && requestMethod.equals("POST")) {
            return Endpoint.POST;
        } else if (pathParts.length == 3 && pathParts[1].equals(userPath) && requestMethod.equals("DELETE")) {
            return Endpoint.DELETE;
        }
        return Endpoint.UNKNOWN;
    }

    public static void handleDeleteTaskById(HttpExchange exchange, InMemoryTaskManager manager) throws IOException {
        Optional<Integer> postIdOpt = BaseHttpHandler.getTaskId(exchange);
        if (postIdOpt.isEmpty()) {
            BaseHttpHandler.writeResponse(exchange, "Некорректный id", 400);
            return;
        }
        long taskId = postIdOpt.get();
        manager.deleteTaskById(taskId);
        exchange.sendResponseHeaders(200, 0);
        BaseHttpHandler.writeResponse(exchange, "Задача успешно удалена", 200);
    }

    public static Optional<Task> parseTask(InputStream bodyInputStream, InMemoryTaskManager manager) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);
        String name;
        String description;
        long duration;
        String startTime;
        if (body.isEmpty()) {
            return Optional.empty();
        } else {
            String[] newLineInd = body.split(",");
            name = newLineInd[2];
            description = newLineInd[4];
            duration = Long.parseLong(newLineInd[6]);
            startTime = newLineInd[7];
            return Optional.of(manager.createTask(name, description, duration, startTime));
        }
    }
}
