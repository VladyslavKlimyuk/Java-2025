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

@WebServlet(name = "add-notebook", value = "/add-notebook")
public class AddNotebookServlet extends HttpServlet {

    private final NotebookDAO notebookDao = new NotebookDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='uk'>");
        out.println("<head><title>Додати новий блокнот</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} input, select," +
                "button {padding: 8px; margin: 5px 0; width: 300px;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Додавання нового блокнота</h1>");

        out.println("<form action='add-notebook' method='POST'>");

        out.println("<label for='manufacturerName'>Виробник:</label><br>");
        out.println("<input type='text' id='manufacturerName' name='manufacturerName' required><br>");

        out.println("<label for='notebookCode'>Шифр/Код блокнота (Унікальний):</label><br>");
        out.println("<input type='text' id='notebookCode' name='notebookCode' required><br>");

        out.println("<label for='pageCount'>Кількість сторінок:</label><br>");
        out.println("<input type='number' id='pageCount' name='pageCount' min='1' required><br>");

        out.println("<label for='coverType'>Тип обкладинки:</label><br>");
        out.println("<select id='coverType' name='coverType' required>");
        out.println("<option value='HARD'>Тверда</option>");
        out.println("<option value='SOFT'>М'яка</option>");
        out.println("</select><br>");

        out.println("<label for='country'>Країна-виробник:</label><br>");
        out.println("<input type='text' id='country' name='country' required><br>");

        out.println("<label for='circulation'>Тираж:</label><br>");
        out.println("<input type='number' id='circulation' name='circulation' min='1' required><br>");

        out.println("<label for='pageLayout'>Розграфлення:</label><br>");
        out.println("<select id='pageLayout' name='pageLayout' required>");
        out.println("<option value='SQUARE'>Клітинка</option>");
        out.println("<option value='LINE'>Лінійка</option>");
        out.println("</select><br><br>");

        out.println("<input type='submit' value='Додати блокнот'>");
        out.println("</form>");

        out.println("<p><a href='" + request.getContextPath() + "/notebook'>" +
                "Повернутися до звітів</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        try {
            String manufacturerName = request.getParameter("manufacturerName");
            String notebookCode = request.getParameter("notebookCode");
            int pageCount = Integer.parseInt(request.getParameter("pageCount"));
            String country = request.getParameter("country");
            int circulation = Integer.parseInt(request.getParameter("circulation"));

            CoverType coverType = CoverType.valueOf(request.getParameter("coverType"));
            PageLayout pageLayout = PageLayout.valueOf(request.getParameter("pageLayout"));

            Notebook newNotebook = new Notebook();
            newNotebook.setManufacturerName(manufacturerName);
            newNotebook.setNotebookCode(notebookCode);
            newNotebook.setPageCount(pageCount);
            newNotebook.setCoverType(coverType);
            newNotebook.setCountry(country);
            newNotebook.setCirculation(circulation);
            newNotebook.setPageLayout(pageLayout);

            notebookDao.create(newNotebook);

            response.sendRedirect(request.getContextPath() + "/notebook?status=added");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=bad_number");
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=bad_enum");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/notebook?status=error&code=db_add_fail");
        }
    }
}