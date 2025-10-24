package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.NotebookDAO;
import com.example.webjavahibernate.Models.Notebook;
import com.example.webjavahibernate.Enums.CoverType;
import com.example.webjavahibernate.Enums.PageLayout;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/update-notebook")
public class UpdateNotebookServlet extends HttpServlet {

    private final NotebookDAO notebookDao = new NotebookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");

        out.println("<!DOCTYPE html><html lang='uk'>");
        out.println("<head><title>Оновлення даних блокнота</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} input, select," +
                "button {padding: 8px; margin: 5px 0; width: 300px;}</style>");
        out.println("</head><body>");

        if (idParam != null && !idParam.isEmpty()) {
            displayEditForm(request, response, out, Long.parseLong(idParam));
        }
        else {
            displaySelectionList(request, response, out);
        }

        out.println("<p><a href='" + request.getContextPath() + "/notebook'>Повернутися до звітів" +
                "</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String manufacturerName = request.getParameter("manufacturerName");
            String notebookCode = request.getParameter("notebookCode");
            int pageCount = Integer.parseInt(request.getParameter("pageCount"));
            String country = request.getParameter("country");
            int circulation = Integer.parseInt(request.getParameter("circulation"));
            CoverType coverType = CoverType.valueOf(request.getParameter("coverType"));
            PageLayout pageLayout = PageLayout.valueOf(request.getParameter("pageLayout"));

            Notebook updatedNotebook = new Notebook();
            updatedNotebook.setId(id);
            updatedNotebook.setManufacturerName(manufacturerName);
            updatedNotebook.setNotebookCode(notebookCode);
            updatedNotebook.setPageCount(pageCount);
            updatedNotebook.setCoverType(coverType);
            updatedNotebook.setCountry(country);
            updatedNotebook.setCirculation(circulation);
            updatedNotebook.setPageLayout(pageLayout);

            notebookDao.update(updatedNotebook);

            response.sendRedirect(request.getContextPath() + "/notebook?status=updated");

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=bad_data_update");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=db_update_fail");
        }
    }

    private void displaySelectionList(HttpServletRequest request, HttpServletResponse response,
                                      PrintWriter out) {
        out.println("<h1>Виберіть блокнот для редагування</h1>");
        List<Notebook> allNotebooks = notebookDao.findAll();

        if (allNotebooks.isEmpty()) {
            out.println("<p>Список блокнотів порожній. Нічого редагувати.</p>");
            return;
        }

        out.println("<form action='update-notebook' method='GET'>");
        out.println("<table><thead><tr><th>Вибрати</th><th>ID</th><th>Виробник</th><th>Код</th>" +
                "<th>Країна</th></tr></thead><tbody>");

        for (Notebook n : allNotebooks) {
            out.printf("<tr><td><input type='radio' name='id' value='%d' required></td><td>%d</td>" +
                            "<td>%s</td><td>%s</td><td>%s</td></tr>",
                    n.getId(), n.getId(), n.getManufacturerName(), n.getNotebookCode(), n.getCountry());
        }

        out.println("</tbody></table><br>");
        out.println("<button type='submit'>Редагувати вибраний блокнот</button>");
        out.println("</form>");
    }

    private void displayEditForm(HttpServletRequest request, HttpServletResponse response,
                                 PrintWriter out, Long id) throws IOException {

        Notebook notebook = notebookDao.findById(id);

        if (notebook == null) {
            out.println("<p>Блокнот з ID " + id + " не знайдено.</p>");
            return;
        }

        out.println("<h1>Редагування блокнота (ID: " + id + ")</h1>");

        out.println("<form action='update-notebook' method='POST'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");

        out.println("<label for='manufacturerName'>Виробник:</label><br>");
        out.printf("<input type='text' id='manufacturerName' name='manufacturerName' value='%s'" +
                "required><br>", notebook.getManufacturerName());

        out.println("<label for='notebookCode'>Шифр/Код блокнота (Унікальний):</label><br>");
        out.printf("<input type='text' id='notebookCode' name='notebookCode' value='%s' required><br>",
                notebook.getNotebookCode());

        out.println("<label for='pageCount'>Кількість сторінок:</label><br>");
        out.printf("<input type='number' id='pageCount' name='pageCount' min='1' value='%d'" +
                "required><br>", notebook.getPageCount());

        out.println("<label for='coverType'>Тип обкладинки:</label><br>");
        out.println("<select id='coverType' name='coverType' required>");
        for (CoverType ct : CoverType.values()) {
            String selected = (ct == notebook.getCoverType()) ? "selected" : "";
            out.printf("<option value='%s' %s>%s</option>", ct.name(), selected, ct.name());
        }
        out.println("</select><br>");

        out.println("<label for='country'>Країна-виробник:</label><br>");
        out.printf("<input type='text' id='country' name='country' value='%s' required><br>",
                notebook.getCountry());

        out.println("<label for='circulation'>Тираж:</label><br>");
        out.printf("<input type='number' id='circulation' name='circulation' min='1' value='%d'" +
                "required><br>", notebook.getCirculation());

        out.println("<label for='pageLayout'>Розграфлення:</label><br>");
        out.println("<select id='pageLayout' name='pageLayout' required>");
        for (PageLayout pl : PageLayout.values()) {
            String selected = (pl == notebook.getPageLayout()) ? "selected" : "";
            out.printf("<option value='%s' %s>%s</option>", pl.name(), selected, pl.name());
        }
        out.println("</select><br><br>");

        out.println("<input type='submit' value='Зберегти зміни'>");
        out.println("</form>");
    }
}