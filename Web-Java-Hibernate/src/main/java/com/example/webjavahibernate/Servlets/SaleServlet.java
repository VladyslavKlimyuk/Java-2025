package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.SaleDAO;
import com.example.webjavahibernate.DAOs.SellerDAO;
import com.example.webjavahibernate.DAOs.CustomerDAO;
import com.example.webjavahibernate.Models.Sale;
import com.example.webjavahibernate.Models.Seller;
import com.example.webjavahibernate.Models.Customer;
import com.example.webjavahibernate.Models.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "sale", value = "/sale")
public class SaleServlet extends HttpServlet {

    private final SaleDAO saleDao = new SaleDAO();
    private final SellerDAO sellerDao = new SellerDAO();
    private final CustomerDAO customerDao = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        request.setCharacterEncoding("UTF-8");

        out.println("<!DOCTYPE html>");
        out.println("<html lang='uk'>");
        out.println("<head><title>Продажі: звіти та аналітика</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;} table {border-collapse:" +
                "collapse; width: 90%; margin: 20px 0;} th, td {border: 1px solid #ddd; padding: 8px;" +
                "text-align: left;} th {background-color: #f2f2f2;} .filter-block {border: 1px solid #ccc;" +
                "padding: 15px; margin-bottom: 20px; width: 450px; display: inline-block;" +
                "margin-right: 20px;} .filter-block input, .filter-block select" +
                "{padding: 5px; margin: 5px 0;} button {padding: 10px; margin-right: 10px;" +
                "cursor: pointer;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Звіти по базі даних 'Продажі'</h1>");

        out.println("<div>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/add-sale'\">Додати нову угоду</button>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/update-sale'\">Оновити угоду</button>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/delete-sale'\">Видалити угоду</button>");
        out.println("<button onclick=\"window.location.href='" + request.getContextPath() +
                "/manage'\">Керувати продавцями/покупцями/товарами</button>");
        out.println("</div>");
        out.println("<hr>");

        printAllSales(out);
        printTopStats(out);
        out.println("<hr>");

        displayInteractiveFilters(request, out);

        processFilters(request, out);

        out.println("</body></html>");
    }

    private void printAllSales(PrintWriter out) {
        List<Sale> allSales = saleDao.findAll();
        out.println("<h2>1. Усі угоди в БД (" + allSales.size() + ")</h2>");
        if (allSales.isEmpty()) {
            out.println("<p>База даних порожня. Додайте дані!</p>");
            return;
        }

        out.println("<table><thead><tr><th>ID</th><th>Товар</th><th>Продавець</th><th>Покупець</th>" +
                "<th>Ціна ($)</th><th>Дата</th></tr></thead><tbody>");
        for (Sale s : allSales) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%s</td></tr>",
                    s.getId(), s.getProduct().getName(), s.getSeller().getName(),
                    s.getCustomer().getName(),
                    s.getSalePrice(), s.getSaleDate());
        }
        out.println("</tbody></table>");
        out.println("<hr>");
    }

    private void printTopStats(PrintWriter out) {
        out.println("<h2>2. Аналітика та Топ-результати</h2>");
        Seller topSeller = saleDao.findMostSuccessfulSeller();
        if (topSeller != null) {
            out.printf("<p><strong>Найуспішніший продавець (за сумою):</strong> %s (ID: %d)</p>",
                    topSeller.getName(), topSeller.getId());
        }

        Customer topCustomer = saleDao.findMostSuccessfulCustomer();
        if (topCustomer != null) {
            out.printf("<p><strong>Найбільший покупець (за сумою):</strong> %s (ID: %d)</p>",
                    topCustomer.getName(), topCustomer.getId());
        }

        Double avgSale = saleDao.findAverageSaleAmount();
        if (avgSale != null) {
            out.printf("<p><strong>Середня сума покупки:</strong> %.2f</p>", avgSale);
        }

        Product topProduct = saleDao.findMostPopularProduct();
        if (topProduct != null) {
            out.printf("<p><strong>Найпопулярніший товар (за кількістю):</strong> %s (Ціна: %.2f)</p>",
                    topProduct.getName(), topProduct.getPrice());
        }

        out.println("<hr>");
    }

    private void displayInteractiveFilters(HttpServletRequest request, PrintWriter out) {
        out.println("<h2>3. Фільтрація угод</h2>");

        out.println("<div class='filter-block'><h3>3.1. Фільтр за конкретною датою</h3>");
        out.println("<form action='sale' method='GET'>");
        out.println("<input type='hidden' name='filter' value='date'>");
        out.println("<label for='search_date'>Дата (YYYY-MM-DD):</label>");
        out.println("<input type='date' id='search_date' name='value' required>");
        out.println("<button type='submit'>Знайти</button>");
        out.println("</form></div>");

        out.println("<div class='filter-block'><h3>3.2. Фільтр за діапазоном дат</h3>");
        out.println("<form action='sale' method='GET'>");
        out.println("<input type='hidden' name='filter' value='date_range'>");
        out.println("<label for='start_date'>Від:</label>");
        out.println("<input type='date' id='start_date' name='start' required><br>");
        out.println("<label for='end_date'>До:</label>");
        out.println("<input type='date' id='end_date' name='end' required>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        List<Seller> sellers = sellerDao.findAll();
        out.println("<div class='filter-block'><h3>3.3. Фільтр за продавцем</h3>");
        out.println("<form action='sale' method='GET'>");
        out.println("<input type='hidden' name='filter' value='seller'>");
        out.println("<label for='select_seller'>Продавець:</label>");
        out.println("<select id='select_seller' name='id' required>");
        out.println("<option value='' disabled selected>Виберіть продавця</option>");
        sellers.forEach(s -> out.printf("<option value='%d'>%s (ID: %d)</option>",
                s.getId(), s.getName(), s.getId()));
        out.println("</select>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        List<Customer> customers = customerDao.findAll();
        out.println("<div class='filter-block'><h3>3.4. Фільтр за покупцем</h3>");
        out.println("<form action='sale' method='GET'>");
        out.println("<input type='hidden' name='filter' value='customer'>");
        out.println("<label for='select_customer'>Покупець:</label>");
        out.println("<select id='select_customer' name='id' required>");
        out.println("<option value='' disabled selected>Виберіть покупця</option>");
        customers.forEach(c -> out.printf("<option value='%d'>%s (ID: %d)</option>",
                c.getId(), c.getName(), c.getId()));
        out.println("</select>");
        out.println("<button type='submit'>Фільтрувати</button>");
        out.println("</form></div>");

        out.println("<hr style='clear: both;'>");
    }

    private void processFilters(HttpServletRequest request, PrintWriter out) {
        String filterType = request.getParameter("filter");
        List<Sale> results = Collections.emptyList();
        String title = "Результати фільтрації";

        if (filterType == null || filterType.isEmpty()) {
            return;
        }

        try {
            switch (filterType) {
                case "date":
                    LocalDate date = LocalDate.parse(request.getParameter("value"));
                    results = saleDao.findByDate(date);
                    title = "Угоди за конкретну дату: " + date;
                    break;
                case "date_range":
                    LocalDate startDate = LocalDate.parse(request.getParameter("start"));
                    LocalDate endDate = LocalDate.parse(request.getParameter("end"));
                    results = saleDao.findByDateRange(startDate, endDate);
                    title = String.format("Угоди в діапазоні дат: від %s до %s", startDate, endDate);
                    break;
                case "seller":
                    Long sellerId = Long.parseLong(request.getParameter("id"));
                    Seller seller = sellerDao.findById(sellerId);
                    results = saleDao.findBySeller(sellerId);
                    title = "Угоди продавця: " + (seller != null ? seller.getName() : "Невідомий");
                    break;
                case "customer":
                    Long customerId = Long.parseLong(request.getParameter("id"));
                    Customer customer = customerDao.findById(customerId);
                    results = saleDao.findByCustomer(customerId);
                    title = "Покупки покупця: " + (customer != null ? customer.getName() : "Невідомий");
                    break;
                default:
                    out.println("<p>Невідомий тип фільтра.</p>");
                    return;
            }
        } catch (NumberFormatException | NullPointerException e) {
            out.println("<p style='color: red;'>Помилка: Некоректні ID або значення для фільтрації.</p>");
            return;
        } catch (Exception e) {
            out.println("<p style='color: red;'>Помилка при виконанні фільтрації: " +
                    e.getMessage() + "</p>");
            e.printStackTrace();
            return;
        }

        out.println("<h2>" + title + " (" + results.size() + " шт.)</h2>");
        if (results.isEmpty()) {
            out.println("<p>За заданими критеріями угод не знайдено.</p>");
        } else {
            out.println("<table><thead><tr><th>ID</th><th>Товар</th><th>Продавець</th><th>Покупець</th>" +
                    "<th>Ціна</th><th>Дата</th></tr></thead><tbody>");
            results.forEach(s -> out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td>" +
                            "<td>%.2f</td><td>%s</td></tr>",
                    s.getId(), s.getProduct().getName(), s.getSeller().getName(),
                    s.getCustomer().getName(),
                    s.getSalePrice(), s.getSaleDate()));
            out.println("</tbody></table>");
        }
        out.println("<hr>");
    }
}