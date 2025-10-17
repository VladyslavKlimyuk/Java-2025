package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.DoubleStream;

@WebServlet(name = "lab4_4_servlet", value = "/lab4_4_servlet")
public class Lab4_4_Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Обчислення чисел</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 30px; }");
        out.println("        input[type=\"number\"] { padding: 8px; margin-bottom: 10px; border: " +
                "1px solid #ccc; width: 150px; }");
        out.println("        .radio-group label { display: block; margin-bottom: 5px; }");
        out.println("        button { padding: 10px 15px; background-color: #007bff; color: white; " +
                "border: none; cursor: pointer; margin-top: 15px; }");
        out.println("        .result { margin-top: 20px; font-size: 1.2em; font-weight: bold; " +
                "color: #333; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Обчислення мінімуму, максимуму та середнього значення</h1>");

        out.println("    <form method=\"post\" action=\"lab4_4_servlet\">");

        out.println("        <label for=\"num1\">Число 1:</label>");
        out.println("        <input type=\"number\" id=\"num1\" name=\"num1\" step=\"any\" " +
                "required><br>");
        out.println("        <label for=\"num2\">Число 2:</label>");
        out.println("        <input type=\"number\" id=\"num2\" name=\"num2\" step=\"any\" " +
                "required><br>");
        out.println("        <label for=\"num3\">Число 3:</label>");
        out.println("        <input type=\"number\" id=\"num3\" name=\"num3\" step=\"any\" " +
                "required><br>");

        out.println("        <h2>Виберіть дію:</h2>");
        out.println("        <div class=\"radio-group\">");

        out.println("            <label>");
        out.println("                <input type=\"radio\" name=\"operation\" value=\"min\" checked> " +
                "Мінімальне значення");
        out.println("            </label>");

        out.println("            <label>");
        out.println("                <input type=\"radio\" name=\"operation\" value=\"max\"> " +
                "Максимальне значення");
        out.println("            </label>");

        out.println("            <label>");
        out.println("                <input type=\"radio\" name=\"operation\" value=\"avg\"> " +
                "Середнє арифметичне");
        out.println("            </label>");

        out.println("        </div>");

        out.println("        <button type=\"submit\">Обчислити</button>");
        out.println("    </form>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String operation = request.getParameter("operation");
        String resultMessage;

        try {
            double n1 = Double.parseDouble(request.getParameter("num1"));
            double n2 = Double.parseDouble(request.getParameter("num2"));
            double n3 = Double.parseDouble(request.getParameter("num3"));

            double[] numbers = {n1, n2, n3};
            double result = 0;
            String operationName = "";

            switch (operation) {
                case "min":
                    result = DoubleStream.of(numbers).min().getAsDouble();
                    operationName = "Мінімальне значення";
                    break;
                case "max":
                    result = DoubleStream.of(numbers).max().getAsDouble();
                    operationName = "Максимальне значення";
                    break;
                case "avg":
                    result = DoubleStream.of(numbers).average().getAsDouble();
                    operationName = "Середнє арифметичне";
                    break;
                default:
                    operationName = "Невідома операція";
                    break;
            }

            resultMessage = operationName + " (" + n1 + ", " + n2 + ", " + n3 + ") дорівнює: " +
                    String.format("%.2f", result);

        } catch (NumberFormatException e) {
            resultMessage = "Помилка: Будь ласка, введіть коректні числові значення.";
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Результат обчислення</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 30px; }");
        out.println("        .result { margin-top: 20px; font-size: 1.2em; font-weight: bold; " +
                "color: #007bff; }");
        out.println("        .error { color: red; }");
        out.println("        .back-link { margin-top: 30px; display: block; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Результат обчислення</h1>");
        out.println("    <p class=\"result\">" + resultMessage + "</p>");

        out.println("    <a href=\"lab4_4_servlet\" class=\"back-link\">Повернутися до форми обчислення" +
                "</a>");
        out.println("    <a href=\"/Web_Java_war_exploded/\" class=\"back-link\">Повернутися до " +
                "головної сторінки</a>");

        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
