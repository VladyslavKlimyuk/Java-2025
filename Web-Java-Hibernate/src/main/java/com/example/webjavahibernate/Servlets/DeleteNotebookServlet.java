package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.NotebookDAO;
import com.example.webjavahibernate.Models.Notebook;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/delete-notebook")
public class DeleteNotebookServlet extends HttpServlet {

    private final NotebookDAO notebookDao = new NotebookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Notebook> allNotebooks = notebookDao.findAll();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='uk'>");
        out.println("<head><title>Вибір блокнота для видалення</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} button {padding: 10px;" +
                "margin-top: 15px;} .notebook-item {margin-bottom: 8px;} table {border-collapse: collapse;" +
                "width: 70%; margin-top: 20px;} th, td {border: 1px solid #ddd; padding: 8px;" +
                "text-align: left;} th {background-color: #f2f2f2;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Виберіть блокнот для видалення</h1>");

        if (allNotebooks.isEmpty()) {
            out.println("<p>Список блокнотів порожній. Нічого видаляти.</p>");
        } else {
            out.println("<form action='delete-notebook' method='POST'>");

            out.println("<table>");
            out.println("<thead><tr><th>Вибрати</th><th>ID</th><th>Виробник</th><th>Код</th>" +
                    "<th>Країна</th><th>Сторінок</th></tr></thead>");
            out.println("<tbody>");

            for (Notebook n : allNotebooks) {
                out.printf("<tr>");
                out.printf("<td><input type='radio' name='id' value='%d' required></td>", n.getId());
                out.printf("<td>%d</td>", n.getId());
                out.printf("<td>%s</td>", n.getManufacturerName());
                out.printf("<td>%s</td>", n.getNotebookCode());
                out.printf("<td>%s</td>", n.getCountry());
                out.printf("<td>%d</td>", n.getPageCount());
                out.printf("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");

            out.println("<br>");
            out.println("<button type='submit' onclick=\"return confirm" +
                    "('Ви справді хочете видалити цей блокнот?');\">Видалити вибраний блокнот</button>");
            out.println("</form>");
        }

        out.println("<p><a href='" + request.getContextPath() +
                "/notebook'>Повернутися до звітів</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        String idParam = request.getParameter("id");
        Long notebookId = null;

        try {
            if (idParam != null && !idParam.isEmpty()) {
                notebookId = Long.parseLong(idParam);
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/notebook?status=error&message=Не_вибрано_ID");
                return;
            }

            notebookDao.delete(notebookId);

            response.sendRedirect(request.getContextPath() + "/notebook?status=deleted");

        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=bad_data_delete");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=db_delete_fail");
        }
    }
}