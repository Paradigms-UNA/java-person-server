
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PersonServlet", urlPatterns = { "/persons" })
public class PersonServlet extends HttpServlet {
    private static final String PERSONS_FILE_PROPERTY = "persons";
    private static final String PROPS_FILE = "/WEB-INF/props/configuration.properties";
    private static final String ERROR_PAGE = "404.html";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static List<Person> persons;

    public void readPersonJSON(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String json = br.lines().collect(Collectors.joining(""));
        br.lines().toList().forEach(str -> System.out.println(str));
        Person.idCounter = 1;
        persons = objectMapper.readValue(json, new TypeReference<List<Person>>() {
        });
    }

    public String getPersonsLocation(ServletContext context) throws IOException {
        InputStream propsFile = context.getResourceAsStream(PROPS_FILE);
        Properties props = new Properties();
        props.load(propsFile);
        return props.getProperty(PERSONS_FILE_PROPERTY);
    }

    public String filterById(Integer id) throws JsonProcessingException {
        Person found = persons.stream()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
        return objectMapper.writeValueAsString(found);
    }

    public String filterByAge(Integer ageMin, Integer ageMax) throws JsonProcessingException {
        List<Person> filterPersons = persons.stream()
                .filter(p -> p.getAge() >= ageMin && p.getAge() <= ageMax)
                .collect(Collectors.toList());

        return objectMapper.writeValueAsString(filterPersons);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get parameter, TODO: use it to filter
        String id = request.getParameter("id");
        System.out.println("PersonServlet::doGet:: id=" + id);

        String ageMin = request.getParameter("agemin");
        String ageMax = request.getParameter("agemax");

        String json = null;
        try {
            ServletContext context = getServletContext();
            String personsJsonLocation = getPersonsLocation(context);
            readPersonJSON(context.getResourceAsStream(personsJsonLocation));

            if (id != null) {
                json = filterById(Integer.valueOf(id));
            } else if (ageMin != null && ageMax != null) {
                json = filterByAge(Integer.valueOf(ageMin), Integer.valueOf(ageMax));
            } else {
                json = objectMapper.writeValueAsString(persons);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.getRequestDispatcher(ERROR_PAGE)
                    .forward(request, response);
            return;
        }
        // Build response and reply
        PrintWriter writer = response.getWriter();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        writer.print(json);
        writer.flush();
    }
}
