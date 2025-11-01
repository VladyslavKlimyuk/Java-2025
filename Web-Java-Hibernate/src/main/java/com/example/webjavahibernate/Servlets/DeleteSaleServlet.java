package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.SaleDAO;
import com.example.webjavahibernate.Models.Sale;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "delete-sale", value = "/delete-sale")
public class DeleteSaleServlet extends HttpServlet {

    private final SaleDAO saleDao = new SaleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Sale> allSales = saleDao.findAll();

        out.println("<!DOCTYPE html><html lang='uk'><head><title>Вибір угоди для видалення</title>" +
                "<style>/* styles from your example */</style></head><body>");
        out.println("<h1>Виберіть угоду для видалення</h1>");

        if (allSales.isEmpty()) {
            out.println("<p>Список угод порожній.</p>");
        } else {
            out.println("<form action='delete-sale' method='POST'>");
            out.println("<table><thead><tr><th>Вибрати</th><th>ID</th><th>Продавець</th><th>Покупець</th>" +
                    "<th>Товар</th><th>Дата</th></tr></thead><tbody>");

            for (Sale s : allSales) {
                String sellerName = s.getSeller() != null ? s.getSeller().getName() : "N/A";
                String customerName = s.getCustomer() != null ? s.getCustomer().getName() : "N/A";
                String productName = s.getProduct() != null ? s.getProduct().getName() : "N/A";

                out.printf("<tr><td><input type='radio' name='id' value='%d' required></td><td>%d</td>" +
                                "<td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                        s.getId(), s.getId(), sellerName, customerName, productName, s.getSaleDate());
            }
            out.println("</tbody></table><br>");
            out.println("<button type='submit' onclick=\"return confirm" +
                    "('Ви справді хочете видалити цю угоду?');\">Видалити вибрану угоду</button>");
            out.println("</form>");
        }

        out.println("<p><a href='" + request.getContextPath() +
                "/sale'>Повернутися до звітів</a></p></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String idParam = request.getParameter("id");
        try {
            if (idParam != null && !idParam.isEmpty()) {
                Long saleId = Long.parseLong(idParam);
                saleDao.delete(saleId);
                response.sendRedirect(request.getContextPath() + "/sale?status=deleted");
            } else {
                response.sendRedirect(request.getContextPath() +
                        "/sale?status=error&message=Не_вибрано_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=db_delete_fail");
        }
    }
}