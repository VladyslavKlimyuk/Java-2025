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

@WebServlet(name = "add-sale", value = "/add-sale")
public class AddSaleServlet extends HttpServlet {

    private final SaleDAO saleDao = new SaleDAO();
    private final SellerDAO sellerDao = new SellerDAO();
    private final CustomerDAO customerDao = new CustomerDAO();
    private final ProductDAO productDao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        List<Seller> sellers = sellerDao.findAll();
        List<Customer> customers = customerDao.findAll();
        List<Product> products = productDao.findAll();

        out.println("<!DOCTYPE html><html lang='uk'>");
        out.println("<head><title>Додати нову угоду</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} input, select," +
                "button {padding: 8px; margin: 5px 0; width: 300px;}</style>");
        out.println("</head><body>");
        out.println("<h1>Додавання нової угоди</h1>");

        if (sellers.isEmpty() || customers.isEmpty() || products.isEmpty()) {
            out.println("<p style='color: red;'>Неможливо додати угоду. " +
                    "Переконайтеся, що ви додали хоча б " +
                    "одного продавця, покупця та товар!</p>");
            out.println("<p><a href='" + request.getContextPath() + "/manage-entities'>" +
                    "Перейти до керування сутностями</a></p>");
        } else {
            out.println("<form action='add-sale' method='POST'>");

            out.println("<label for='sellerId'>Продавець:</label><br>");
            out.println("<select id='sellerId' name='sellerId' required>");
            out.println("<option value='' disabled selected>Виберіть продавця</option>");
            sellers.forEach(s -> out.printf("<option value='%d'>%s (Email: %s)</option>",
                    s.getId(), s.getName(), s.getEmail()));
            out.println("</select><br>");

            out.println("<label for='customerId'>Покупець:</label><br>");
            out.println("<select id='customerId' name='customerId' required>");
            out.println("<option value='' disabled selected>Виберіть покупця</option>");
            customers.forEach(c -> out.printf("<option value='%d'>%s (Email: %s)</option>",
                    c.getId(), c.getName(), c.getEmail()));
            out.println("</select><br>");

            out.println("<label for='productId'>Товар:</label><br>");
            out.println("<select id='productId' name='productId' required>");
            out.println("<option value='' disabled selected>Виберіть товар</option>");
            products.forEach(p -> out.printf("<option value='%d'>%s (Ціна ($): %.2f)</option>",
                    p.getId(), p.getName(), p.getPrice()));
            out.println("</select><br>");

            out.println("<label for='salePrice'>Фактична ціна продажу ($):</label><br>");
            out.printf("<input type='number' id='salePrice' name='salePrice' step='0.01' min='0.01'" +
                    "required><br>");

            out.println("<label for='saleDate'>Дата продажу:</label><br>");
            out.println("<input type='date' id='saleDate' name='saleDate' value='" + LocalDate.now() +
                    "' required><br><br>");

            out.println("<input type='submit' value='Зареєструвати угоду'>");
            out.println("</form>");
        }

        out.println("<p><a href='" + request.getContextPath() + "/sale'>Повернутися до звітів</a></p>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        try {
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
                        "/sale?status=error&code=missing_entity");
                return;
            }

            Sale newSale = new Sale();
            newSale.setSeller(seller);
            newSale.setCustomer(customer);
            newSale.setProduct(product);
            newSale.setSalePrice(salePrice);
            newSale.setSaleDate(saleDate);

            saleDao.create(newSale);

            response.sendRedirect(request.getContextPath() + "/sale?status=added");

        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=bad_data_input");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() +
                    "/sale?status=error&code=db_add_fail");
        }
    }
}