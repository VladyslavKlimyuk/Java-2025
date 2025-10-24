package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.NotebookDAO;
import com.example.webjavahibernate.Models.Notebook;
import com.example.webjavahibernate.Enums.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;

@WebServlet(name = "notebook", value = "/notebook")
public class NotebookServlet extends HttpServlet {

    private final NotebookDAO notebookDao = new NotebookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='uk'>");
        out.println("<head><title>Блокноти: Звіти та Запити</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} table {border-collapse:" +
                "collapse; width: 80%; margin: 20px 0;} th, td {border: 1px solid #ddd; padding: 8px;" +
                "text-align: left;} th {background-color: #f2f2f2;} .filter-block {border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; width: 400px;} .filter-block input, .filter-block select {padding: 5px; margin: 5px 0;} button {padding: 10px; margin-right: 10px; cursor: pointer;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Звіти по базі даних 'Блокноти'</h1>");


        out.println("<div>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/add-notebook'\">Додати новий блокнот</button>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/delete-notebook'\">Видалити блокнот (вибрати зі списку)</button>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/update-notebook'\">Редагувати блокнот</button>");
        out.println("</div>");
        out.println("<hr>");

        printAllNotebooks(out);

        printCountryStats(out);
        printManufacturerStats(out);

        printMinMaxStats(out);

        displayInteractiveFilters(request, out);

        processFilters(request, out);

        out.println("</body></html>");
    }

    private void printAllNotebooks(PrintWriter out) {
        List<Notebook> allNotebooks = notebookDao.findAll();
        out.println("<h2>1. Усі блокноти в БД (" + allNotebooks.size() + ")</h2>");
        if (allNotebooks.isEmpty()) {
            out.println("<p>База даних порожня. Додайте дані для тестування!</p>");
            return;
        }

        out.println("<table><thead><tr><th>ID</th><th>Виробник</th><th>Країна</th><th>Обкладинка" +
                "</th><th>Сторінки</th><th>Тираж</th></tr></thead><tbody>");
        for (Notebook n : allNotebooks) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%d (%s)</td><td>%d</td></tr>",
                    n.getId(), n.getManufacturerName(), n.getCountry(), n.getCoverType(),
                    n.getPageCount(), n.getPageLayout(), n.getCirculation());
        }
        out.println("</tbody></table>");
        out.println("<hr>");
    }

    private void printCountryStats(PrintWriter out) {
        Map<String, Long> counts = notebookDao.countByCountry();
        out.println("<h2>2. Статистика по країнах</h2>");
        out.println("<table><thead><tr><th>Країна</th><th>Кількість блокнотів</th></tr></thead><tbody>");
        counts.forEach((country, count) -> out.printf("<tr><td>%s</td><td>%d</td></tr>",
                country, count));
        out.println("</tbody></table>");
    }

    private void printManufacturerStats(PrintWriter out) {
        Map<String, Long> counts = notebookDao.countByManufacturer();
        out.println("<h2>3. Статистика по виробниках</h2>");
        out.println("<table><thead><tr><th>Виробник</th><th>Кількість блокнотів</th></tr></thead><tbody>");
        counts.forEach((manufacturer, count) -> out.printf
                ("<tr><td>%s</td><td>%d</td></tr>", manufacturer, count));
        out.println("</tbody></table>");
    }

    private void printMinMaxStats(PrintWriter out) {
        out.println("<h2>4. Min/Max Статистика</h2>");
        Entry<String, Long> maxCountry = notebookDao.getCountryWithMaxNotebooks();
        Entry<String, Long> minCountry = notebookDao.getCountryWithMinNotebooks();
        Entry<String, Long> maxManufacturer = notebookDao.getManufacturerWithMaxNotebooks();
        Entry<String, Long> minManufacturer = notebookDao.getManufacturerWithMinNotebooks();

        if (maxCountry != null) {
            out.printf("<p><strong>Країна з найбільшою кількістю:</strong> %s (%d шт.)</p>",
                    maxCountry.getKey(), maxCountry.getValue());
        }
        if (minCountry != null) {
            out.printf("<p><strong>Країна з найменшою кількістю:</strong> %s (%d шт.)</p>",
                    minCountry.getKey(), minCountry.getValue());
        }
        if (maxManufacturer != null) {
            out.printf("<p><strong>Виробник з найбільшою кількістю:</strong> %s (%d шт.)</p>",
                    maxManufacturer.getKey(), maxManufacturer.getValue());
        }
        if (minManufacturer != null) {
            out.printf("<p><strong>Виробник з найменшою кількістю:</strong> %s (%d шт.)</p>",
                    minManufacturer.getKey(), minManufacturer.getValue());
        }
        out.println("<hr>");
    }

    private void displayInteractiveFilters(HttpServletRequest request, PrintWriter out) {
        out.println("<h2>5. Інтерактивна Фільтрація</h2>");

        out.println("<div class='filter-block'><h3>5.1. Фільтр за виробником</h3>");
        out.println("<form action='notebook' method='GET'>");
        out.println("<input type='hidden' name='filter' value='manufacturer'>");
        out.println("<label for='search_manufacturer'>Введіть назву:</label>");
        out.println("<input type='text' id='search_manufacturer' name='value' placeholder='Наприклад," +
                "Moleskine' required>");
        out.println("<button type='submit'>Знайти</button>");
        out.println("</form></div>");

        out.println("<div class='filter-block'><h3>5.2. Фільтр за країною</h3>");
        out.println("<form action='notebook' method='GET'>");
        out.println("<input type='hidden' name='filter' value='country'>");
        out.println("<label for='select_country'>Країна:</label>");
        out.println("<select id='select_country' name='value' required>");
        out.println("<option value='' disabled selected>Виберіть країну</option>");

        List<String> countries = notebookDao.findDistinctCountries();
        countries.forEach(c -> out.printf("<option value='%s'>%s</option>", c, c));

        out.println("</select>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        out.println("<div class='filter-block'><h3>5.3. Фільтр за типом обкладинки</h3>");
        out.println("<form action='notebook' method='GET'>");
        out.println("<input type='hidden' name='filter' value='cover_type'>");
        out.println("<label for='select_cover_type'>Тип:</label>");
        out.println("<select id='select_cover_type' name='value' required>");
        out.println("<option value='' disabled selected>Виберіть тип</option>");
        Arrays.stream(CoverType.values())
                .forEach(ct -> out.printf("<option value='%s'>%s</option>", ct.name(),
                        ct.name()));
        out.println("</select>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        out.println("<div class='filter-block'><h3>5.4. Фільтр за кількістю сторінок</h3>");
        out.println("<form action='notebook' method='GET'>");
        out.println("<input type='hidden' name='filter' value='page_count_range'>");
        out.println("<label for='min_pages'>Від:</label>");
        out.println("<input type='number' id='min_pages' name='min' min='1' placeholder='100' required>");
        out.println("<label for='max_pages'>До:</label>");
        out.println("<input type='number' id='max_pages' name='max' min='1' placeholder='200' required>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        out.println("<div class='filter-block'><h3>5.5. Фільтр за тиражем</h3>");
        out.println("<form action='notebook' method='GET'>");
        out.println("<input type='hidden' name='filter' value='circulation_range'>");
        out.println("<label for='min_circulation'>Від:</label>");
        out.println("<input type='number' id='min_circulation' name='min' min='1' placeholder='1000'" +
                "required>");
        out.println("<label for='max_circulation'>До:</label>");
        out.println("<input type='number' id='max_circulation' name='max' min='1' placeholder='50000'" +
                "required>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        out.println("<hr>");
    }

    private void processFilters(HttpServletRequest request, PrintWriter out) {
        String filterType = request.getParameter("filter");
        List<Notebook> results = Collections.emptyList();
        String title = "Результати фільтрації";

        if (filterType == null || filterType.isEmpty()) {
            return;
        }

        try {
            switch (filterType) {
                case "manufacturer":
                    String manufacturerValue = request.getParameter("value");
                    results = notebookDao.findByManufacturerName(manufacturerValue);
                    title = "Результати пошуку за Виробником: " + manufacturerValue;
                    break;
                case "country":
                    String countryValue = request.getParameter("value");
                    results = notebookDao.findByCountry(countryValue);
                    title = "Результати фільтрації за Країною: " + countryValue;
                    break;
                case "cover_type":
                    String coverTypeValue = request.getParameter("value");
                    CoverType coverType = CoverType.valueOf(coverTypeValue);
                    results = notebookDao.findByCoverType(coverType);
                    title = "Результати фільтрації за Обкладинкою: " + coverType.name();
                    break;
                case "page_count_range":
                    int minPages = Integer.parseInt(request.getParameter("min"));
                    int maxPages = Integer.parseInt(request.getParameter("max"));
                    results = notebookDao.filterByPageCount(minPages, maxPages);
                    title = String.format("Результати фільтрації за Сторінками (від %d до %d)",
                            minPages, maxPages);
                    break;
                case "circulation_range":
                    int minCirculation = Integer.parseInt(request.getParameter("min"));
                    int maxCirculation = Integer.parseInt(request.getParameter("max"));
                    results = notebookDao.filterByCirculation(minCirculation, maxCirculation);
                    title = String.format("Результати фільтрації за Тиражем (від %d до %d)",
                            minCirculation, maxCirculation);
                    break;
                default:
                    out.println("<p>Невідомий тип фільтра.</p>");
                    return;
            }
        } catch (NumberFormatException e) {
            out.println("<p style='color: red;'>Помилка: Некоректні числові значення для фільтрації.</p>");
            return;
        } catch (IllegalArgumentException e) {
            out.println("<p style='color: red;'>Помилка: Некоректне значення для випадаючого списку. " +
                    "Переконайтеся, що вибраний тип існує.</p>");
            return;
        } catch (Exception e) {
            out.println("<p style='color: red;'>Помилка при виконанні фільтрації: " +
                    e.getMessage() + "</p>");
            return;
        }

        out.println("<h2>" + title + " (" + results.size() + " шт.)</h2>");
        if (results.isEmpty()) {
            out.println("<p>За заданими критеріями блокнотів не знайдено.</p>");
        } else {
            out.println("<table><thead><tr><th>Код</th><th>Виробник</th><th>Країна</th><th>" +
                    "Обкладинка</th><th>Сторінок</th><th>Тираж</th></tr></thead><tbody>");
            results.forEach(n -> out.printf("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s" +
                            "</td><td>%d (%s)</td><td>%d</td></tr>",
                    n.getNotebookCode(), n.getManufacturerName(), n.getCountry(), n.getCoverType(),
                    n.getPageCount(), n.getPageLayout(), n.getCirculation()));
            out.println("</tbody></table>");
        }
        out.println("<hr>");
    }
}