package ru.osipov.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.osipov.config.Config;
import ru.osipov.controller.PostController;

import javax.servlet.http.*;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String PATH = "/api/posts";
    private static final String PATH_ID = PATH+"/\\d+";
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    @Override
    public void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        controller = context.getBean(PostController.class);
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
                final var id = Long.parseLong(path.substring(1 + path.lastIndexOf("/")));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals(PATH)) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(PATH_ID)) {
                // easy way
                final var id = Long.parseLong(path.substring(1 + path.lastIndexOf("/")));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
