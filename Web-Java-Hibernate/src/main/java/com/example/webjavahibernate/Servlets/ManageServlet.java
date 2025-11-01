package com.example.webjavahibernate.Servlets;

import com.example.webjavahibernate.DAOs.SellerDAO;
import com.example.webjavahibernate.DAOs.CustomerDAO;
import com.example.webjavahibernate.DAOs.ProductDAO;
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
import java.util.List;

@WebServlet(name = "manage", value = "/manage")
public class ManageServlet extends HttpServlet {

    private final SellerDAO sellerDao = new SellerDAO();
    private final CustomerDAO customerDao = new CustomerDAO();
    private final ProductDAO productDao = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String entity = request.getParameter("entity");
        String action = request.getParameter("action");

        out.println("<!DOCTYPE html><html lang='uk'>");
        out.println("<head><title>Керування даними продавців, покупців та товарами</title>");
        out.println("<style>body {font-family: Arial, sans-serif; margin: 20px;}" +
                "table {border-collapse: collapse; width: 80%; margin-top: 20px;} th," +
                "td {border: 1px solid #ddd; padding: 8px; text-align: left;}" +
                "th {background-color: #f2f2f2;} button, input, select {padding: 8px; margin: 5px 0;}" +
                ".navbar button {width: 150px;}</style>");
        out.println("</head><body>");

        out.println("<h1>Керування даними продавців, покупців та товарами</h1>");

        out.println("<div class='navbar'>");
        out.println("<button onclick=\"window.location.href='manage?entity=seller'\">Продавці</button>");
        out.println("<button onclick=\"window.location.href='manage?entity=customer'\">Покупці</button>");
        out.println("<button onclick=\"window.location.href='manage?entity=product'\">Товари</button>");
        out.println("</div><hr>");

        try {
            if (entity == null || entity.isEmpty()) {
                out.println("<p>Оберіть сутність для керування.</p>");
            } else if (action != null && action.equals("edit_form")) {
                displayEditForm(request, out, entity);
            } else if (entity.equalsIgnoreCase("seller")) {
                displaySellerManagement(request, out);
            } else if (entity.equalsIgnoreCase("customer")) {
                displayCustomerManagement(request, out);
            } else if (entity.equalsIgnoreCase("product")) {
                displayProductManagement(request, out);
            }
        } catch (Exception e) {
            out.println("<p style='color: red;'>Помилка відображення: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.println("<p><a href='" + request.getContextPath() + "/sale'>Повернутися до звітів</a>" +
                "</p></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        String entity = request.getParameter("entity");
        String action = request.getParameter("action");
        String redirectUrl = request.getContextPath() + "/manage?entity=" + entity;

        try {
            if (entity == null || action == null) {
                response.sendRedirect(request.getContextPath() +
                        "/manage?status=error&code=invalid_request");
                return;
            }

            request.setCharacterEncoding("UTF-8");

            switch (entity) {
                case "seller":
                    processSellerPost(request, action);
                    break;
                case "customer":
                    processCustomerPost(request, action);
                    break;
                case "product":
                    processProductPost(request, action);
                    break;
            }
            response.sendRedirect(redirectUrl + "&status=" + action + "_success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(redirectUrl + "&status=error&code=db_" + action + "_fail");
        }
    }

    private void displaySellerManagement(HttpServletRequest request, PrintWriter out) {
        List<Seller> sellers = sellerDao.findAll();
        out.println("<h2>Керування продавцями (" + sellers.size() + ")</h2>");

        out.println("<h3>Додати продавця</h3>" +
                "<form action='manage?entity=seller&action=add' method='POST'>");
        out.println("<input type='text' name='name' placeholder='Ім`я' required><br>");
        out.println("<input type='text' name='phone' placeholder='Телефон'><br>");
        out.println("<input type='email' name='email' placeholder='Email' required><br>");
        out.println("<button type='submit'>Додати</button></form><hr>");

        displayListForCrud(out, sellers, "seller", "ID", "Ім`я", "Email");
    }

    private void displayCustomerManagement(HttpServletRequest request, PrintWriter out) {
        List<Customer> customers = customerDao.findAll();
        out.println("<h2>Керування покупцями (" + customers.size() + ")</h2>");

        out.println("<h3>Додати покупця</h3>" +
                "<form action='manage?entity=customer&action=add' method='POST'>");
        out.println("<input type='text' name='name' placeholder='Ім`я' required><br>");
        out.println("<input type='text' name='phone' placeholder='Телефон'><br>");
        out.println("<input type='email' name='email' placeholder='Email' required><br>");
        out.println("<button type='submit'>Додати</button></form><hr>");

        displayListForCrud(out, customers, "customer", "ID", "Ім`я", "Email");
    }

    private void displayProductManagement(HttpServletRequest request, PrintWriter out) {
        List<Product> products = productDao.findAll();
        out.println("<h2>Керування товарами (" + products.size() + ")</h2>");

        out.println("<h3>Додати товар</h3>" +
                "<form action='manage?entity=product&action=add' method='POST'>");
        out.println("<input type='text' name='name' placeholder='Назва товару' required><br>");
        out.println("<input type='number' name='price' step='0.01' min='0.01'" +
                "placeholder='Ціна ($)' required><br>");
        out.println("<button type='submit'>Додати</button></form><hr>");

        displayListForCrud(out, products, "product", "ID", "Назва", "Ціна");
    }

    private <T> void displayListForCrud(PrintWriter out, List<T> entities,
                                        String entityName, String... headers) {
        if (entities.isEmpty()) {
            out.println("<p>Список порожній.</p>");
            return;
        }

        out.println("<h3>Список для видалення / оновлення</h3>");
        out.println("<table><thead><tr>");
        for (String header : headers) {
            out.printf("<th>%s</th>", header);
        }
        out.println("<th>Дія</th></tr></thead><tbody>");

        for (T entity : entities) {
            Long id = null;
            String col1 = "";
            String col2 = "";

            if (entity instanceof Seller s) {
                id = s.getId();
                col1 = s.getName();
                col2 = s.getEmail();
            } else if (entity instanceof Customer c) {
                id = c.getId();
                col1 = c.getName();
                col2 = c.getEmail();
            } else if (entity instanceof Product p) {
                id = p.getId();
                col1 = p.getName();
                col2 = String.format("%.2f", p.getPrice());
            }

            out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>" +
                    "<form style='display:inline;' action='manage?entity=%s&action=edit_form'" +
                    "method='GET'>" +
                    "<input type='hidden' name='id' value='%d'>" +
                    "<button type='submit'>Редагувати</button>" +
                    "</form>" +
                    " | " +
                    "<form style='display:inline;' action='manage?entity=%s&action=delete' method='POST'>" +
                    "<input type='hidden' name='id' value='%d'>" +
                    "<button type='submit' onclick=\"return confirm('Видалити?');\">Видалити</button>" +
                    "</form></td></tr>", id, col1, col2, entityName, id, entityName, id);
        }
        out.println("</tbody></table>");
    }

    private void displayEditForm(HttpServletRequest request, PrintWriter out, String entity)
            throws IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        out.println("<h1>Редагування " + entity.toUpperCase() + " (ID: " + id + ")</h1>");

        out.println("<form action='manage?entity=" + entity + "&action=update' method='POST'>");
        out.println("<input type='hidden' name='id' value='" + id + "'>");

        switch (entity) {
            case "seller":
            case "customer":
                displayPersonEditFields(out, entity.equals("seller") ? sellerDao.findById(id)
                        : customerDao.findById(id), entity);
                break;
            case "product":
                displayProductEditFields(out, productDao.findById(id));
                break;
        }

        out.println("<br><button type='submit'>Зберегти зміни</button>");
        out.println("</form>");
    }

    private void displayPersonEditFields(PrintWriter out, Object entity, String entityType) {
        String name = "";
        String phone = "";
        String email = "";

        if (entity instanceof Seller s) {
            name = s.getName();
            phone = s.getPhone();
            email = s.getEmail();
        } else if (entity instanceof Customer c) {
            name = c.getName();
            phone = c.getPhone();
            email = c.getEmail();
        } else {
            out.println("<p style='color: red;'>Сутність не знайдено.</p>");
            return;
        }

        out.println("<label for='name'>Ім`я:</label><br>");
        out.printf("<input type='text' id='name' name='name' value='%s' required><br>", name);

        out.println("<label for='phone'>Телефон:</label><br>");
        out.printf("<input type='text' id='phone' name='phone' value='%s'><br>", phone != null ? phone : "");

        out.println("<label for='email'>Email:</label><br>");
        out.printf("<input type='email' id='email' name='email' value='%s' required><br>", email);
    }

    private void displayProductEditFields(PrintWriter out, Product product) {
        if (product == null) {
            out.println("<p style='color: red;'>Товар не знайдено.</p>");
            return;
        }

        out.println("<label for='name'>Назва товару:</label><br>");
        out.printf("<input type='text' id='name' name='name' value='%s' required><br>", product.getName());

        out.println("<label for='price'>Ціна:</label><br>");
        out.printf("<input type='number' id='price' name='price' step='0.01' min='0.01' value='%.2f'" +
                "required><br>", product.getPrice());
    }

    private void processSellerPost(HttpServletRequest request, String action) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        Long id = request.getParameter("id") != null
                ? Long.parseLong(request.getParameter("id")) : null;

        if (action.equals("add")) {
            Seller s = new Seller();
            s.setName(name);
            s.setPhone(phone);
            s.setEmail(email);
            sellerDao.create(s);
        } else if (action.equals("delete") && id != null) {
            sellerDao.delete(id);
        } else if (action.equals("update") && id != null) {
            Seller s = new Seller();
            s.setId(id);
            s.setName(name);
            s.setPhone(phone);
            s.setEmail(email);
            sellerDao.update(s);
        }
    }

    private void processCustomerPost(HttpServletRequest request, String action) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        Long id = request.getParameter("id") != null
                ? Long.parseLong(request.getParameter("id")) : null;

        if (action.equals("add")) {
            Customer c = new Customer();
            c.setName(name);
            c.setPhone(phone);
            c.setEmail(email);
            customerDao.create(c);
        } else if (action.equals("delete") && id != null) {
            customerDao.delete(id);
        } else if (action.equals("update") && id != null) {
            Customer c = new Customer();
            c.setId(id);
            c.setName(name);
            c.setPhone(phone);
            c.setEmail(email);
            customerDao.update(c);
        }
    }

    private void processProductPost(HttpServletRequest request, String action) {
        String name = request.getParameter("name");
        Double price = Double.parseDouble(request.getParameter("price"));
        Long id = request.getParameter("id") != null
                ? Long.parseLong(request.getParameter("id")) : null;

        if (action.equals("add")) {
            Product p = new Product();
            p.setName(name);
            p.setPrice(price);
            productDao.create(p);
        } else if (action.equals("delete") && id != null) {
            productDao.delete(id);
        } else if (action.equals("update") && id != null) {
            Product p = new Product();
            p.setId(id);
            p.setName(name);
            p.setPrice(price);
            productDao.update(p);
        }
    }
}