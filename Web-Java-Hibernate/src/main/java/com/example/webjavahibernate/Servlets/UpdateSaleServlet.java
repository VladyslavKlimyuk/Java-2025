package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.*;
import com.example.webjavahibernate.Models.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "update-sale", value = "/update-sale")
public class UpdateSaleServlet extends HttpServlet {

    private final SaleDAO saleDao = new SaleDAO();
    private final SellerDAO sellerDao = new SellerDAO();
    private final CustomerDAO customerDao = new CustomerDAO();
    private final ProductDAO productDao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String idParam = request.getParameter("id");

        out.println("<!DOCTYPE html><html lang='uk'><head><title>Оновлення угоди</title>" +
                "<style>/* styles from your example */</style></head><body>");

        if (idParam != null && !idParam.isEmpty()) {
            displayEditForm(request, out, Long.parseLong(idParam));
        } else {
            displaySelectionList(request, out);
        }

        out.println("<p><a href='" + request.getContextPath() + "/sale'>Повернутися до звітів</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            Long id = Long.parseLong(request.getParameter("id"));

            Sale existingSale = saleDao.findById(id);

            if (existingSale == null) {
                response.sendRedirect(request.getContextPath() +
                        "/sale?status=error&code=sale_not_found");
                return;
            }

            Long sellerId = Long.parseLong(request.getParameter("sellerId"));
            Long customerId = Long.parseLong(request.getParameter("customerId"));
            Long productId = Long.parseLong(request.getParameter("productId"));
            Double salePrice = Double.parseDouble(request.getParameter("salePrice"));
            LocalDate saleDate = LocalDate.parse(request.getParameter("saleDate"));

            Seller seller = sellerDao.findById(sellerId);
            Customer customer = customerDao.findById(customerId);
            Product product = productDao.findById(productId);

            if (seller == null || customer == null || product == null) {
                response.sendRedirect(request.getContextPath() +
                        "/sale?status=error&code=missing_entity_update");
                return;
            }

            existingSale.setSeller(seller);
            existingSale.setCustomer(customer);
            existingSale.setProduct(product);
            existingSale.setSalePrice(salePrice);
            existingSale.setSaleDate(saleDate);

            saleDao.update(existingSale);

            response.sendRedirect(request.getContextPath() + "/sale?status=updated");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=bad_data_format");
        } catch (NullPointerException e) {
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=missing_parameters");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=db_update_fail");
        }
    }

    private void displaySelectionList(HttpServletRequest request, PrintWriter out) {
        out.println("<h1>Виберіть угоду для редагування</h1>");
        List<Sale> allSales = saleDao.findAll();

        if (allSales.isEmpty()) {
            out.println("<p>Список угод порожній.</p>");
            return;
        }

        out.println("<form action='update-sale' method='GET'>");
        out.println("<table><thead><tr><th>Вибрати</th><th>ID</th><th>Продавець</th><th>Покупець</th>" +
                "<th>Товар</th></tr></thead><tbody>");

        for (Sale s : allSales) {
            String sellerName = s.getSeller() != null ? s.getSeller().getName() : "N/A";
            String customerName = s.getCustomer() != null ? s.getCustomer().getName() : "N/A";
            String productName = s.getProduct() != null ? s.getProduct().getName() : "N/A";

            out.printf("<tr><td><input type='radio' name='id' value='%d' required></td><td>%d</td>" +
                            "<td>%s</td><td>%s</td><td>%s</td></tr>",
                    s.getId(), s.getId(), sellerName, customerName, productName);
        }

        out.println("</tbody></table><br>");
        out.println("<button type='submit'>Редагувати вибрану угоду</button>");
        out.println("</form>");
    }

    private void displayEditForm(HttpServletRequest request, PrintWriter out, Long id) {
        Sale sale = saleDao.findById(id);
        List<Seller> sellers = sellerDao.findAll();
        List<Customer> customers = customerDao.findAll();
        List<Product> products = productDao.findAll();

        if (sale == null) {
            out.println("<p>Угоду з ID " + id + " не знайдено.</p>");
            return;
        }

        out.println("<h1>Редагування угоди (ID: " + id + ")</h1>");

        out.println("<form action='update-sale' method='POST'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");

        out.println("<label for='sellerId'>Продавець:</label><br>");
        out.println("<select id='sellerId' name='sellerId' required>");
        sellers.forEach(s -> {
            String selected = (s.getId().equals(sale.getSeller().getId())) ? "selected" : "";
            out.printf("<option value='%d' %s>%s</option>", s.getId(), selected, s.getName());
        });
        out.println("</select><br>");

        out.println("<label for='customerId'>Покупець:</label><br>");
        out.println("<select id='customerId' name='customerId' required>");
        customers.forEach(c -> {
            String selected = (c.getId().equals(sale.getCustomer().getId())) ? "selected" : "";
            out.printf("<option value='%d' %s>%s</option>", c.getId(), selected, c.getName());
        });
        out.println("</select><br>");

        out.println("<label for='productId'>Товар:</label><br>");
        out.println("<select id='productId' name='productId' required>");
        products.forEach(p -> {
            String selected = (p.getId().equals(sale.getProduct().getId())) ? "selected" : "";
            out.printf("<option value='%d' %s>%s (Ціна: %.2f)</option>", p.getId(), selected, p.getName(), p.getPrice());
        });
        out.println("</select><br>");

        out.println("<label for='salePrice'>Фактична ціна продажу ($):</label><br>");
        out.printf("<input type='number' id='salePrice' name='salePrice' step='0.01' min='0.01'" +
                "value='%.2f' required><br>", sale.getSalePrice());

        out.println("<label for='saleDate'>Дата продажу:</label><br>");
        out.printf("<input type='date' id='saleDate' name='saleDate' value='%s' required><br><br>",
                sale.getSaleDate().toString());

        out.println("<input type='submit' value='Зберегти зміни'>");
        out.println("</form>");
    }
}