package ru.osipov.servlet;

import ru.osipov.controller.PostController;
import ru.osipov.repository.PostRepository;
import ru.osipov.service.PostService;

import javax.servlet.http.*;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String PATH = "/api/posts";
    private static final String PATH_ID = PATH + "/\\d+";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals(PATH)) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(PATH_ID)) { // например /api/posts/3
                // easy way
                controller.getById(id(path), resp);
                return;
            }
            if (method.equals(POST) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(PATH_ID)) {
                // easy way
                controller.removeById(id(path), resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long id(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
