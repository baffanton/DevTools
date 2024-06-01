import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestServlet extends HttpServlet {
    private H2Connector connector;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.connector = H2Connector.getInstance();
        this.addCounting(this.getPaths());

        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String word = req.getParameter("word");

        Map<String, Long> data = connector.getData(word.toLowerCase());

        req.setAttribute("files", data);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private List<Path> getPaths() {
        return Stream.of(
                "files/spring.txt",
                "files/summer.txt",
                "files/autumn.txt",
                "files/winter.txt"
        ).map(this::getPath).filter(Objects::nonNull).toList();
    }

    private Path getPath(String fileName) {
        try {
            return Path.of(getClass().getClassLoader().getResource(fileName).toURI());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private void addCounting(List<Path> paths) {
        paths.forEach(path -> {
            Map<String, Long> result = counting(path);
            separateData(path.toString(), result);
        });
    }

    private Map<String, Long> counting(Path path) {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.parallel().flatMap(line -> Stream.of(line.split(" ")))
                    .map(word -> word.replaceAll("\\p{Punct}", ""))
                    .filter(word -> !word.isBlank())
                    .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()));
        } catch (IOException ignored) {
            return new HashMap<>();
        }
    }

    private void separateData(String path, Map<String, Long> data) {
        data.forEach((word, count) -> connector.insertWord(word, count, path));
    }
}
