package httpTest;

import manager.InMemoryTaskManager;
import http.*;
import handler.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static final InMemoryTaskManager manager = new InMemoryTaskManager();
    static HttpTaskServer httpTaskServer;

    @BeforeAll
    static void beforeAll() {
        manager.createTask("Task1", "description task 1", 60, "12:00 26.11.2024");
        Epic epic1 = manager.createEpicTask("Epic1", "description epic 1", 60, "12:00 27.11.2024");
        manager.createSubTask(epic1, "SubTask1", "description task 1", 60, "19:00 26.11.2024");
        try {
            httpTaskServer = new HttpTaskServer(manager);
            httpTaskServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    static void afterAll() {
        httpTaskServer.stop();
    }

    @Test
    public void shouldBeEquals200ByTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = stringRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void shouldBeEquals200BySubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = stringRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void shouldBeEquals200ByEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = stringRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void shouldBeEquals404ByUnknownEndPoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/test");
        HttpRequest request = stringRequest(url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 404);
    }



    public HttpRequest stringRequest(URI url) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
    }
}
