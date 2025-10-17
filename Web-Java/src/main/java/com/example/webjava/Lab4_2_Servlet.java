package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "lab4_2_servlet", value = "/lab4_2_servlet")
public class Lab4_2_Servlet extends HttpServlet {
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
        out.println("    <title>Таблиця множення</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Таблиця множення</h1>");
        out.println("    <form method=\"post\" action=\"lab4_2_servlet\">");
        out.println("        <label for=\"number\">Введіть число (1-100):</label>");
        out.println("        <input type=\"number\" id=\"number\" name=\"number\"" +
                "min=\"1\" max=\"100\" required>");
        out.println("        <button type=\"submit\">Показати таблицю</button>");
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

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Результат таблиці множення</title>");
        out.println("    <style>");
        out.println("        table { border-collapse: collapse; width: 300px; margin-top: 20px; }");
        out.println("        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }");
        out.println("        th { background-color: #f2f2f2; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        try {
            String numberStr = request.getParameter("number");
            int number = Integer.parseInt(numberStr);

            out.println("    <h1>Таблиця множення для числа: " + number + "</h1>");

            out.println("    <table>");
            out.println("        <thead>");
            out.println("            <tr><th>Рівняння</th><th>Результат</th></tr>");
            out.println("        </thead>");
            out.println("        <tbody>");

            for (int i = 1; i <= 10; i++) {
                out.println("        <tr>");
                out.println("            <td>" + number + " &times; " + i + "</td>");
                out.println("            <td>" + (number * i) + "</td>");
                out.println("        </tr>");
            }

            out.println("        </tbody>");
            out.println("    </table>");

        } catch (NumberFormatException e) {
            out.println("    <p style=\"color: red;\">Помилка: Будь ласка, введіть дійсне число.</p>");
        }

        out.println("    <p><a href=\"lab4_2_servlet\">Повернутися до форми введення</a></p>");
        out.println("    <p><a href=\"/Web_Java_war_exploded/\">Повернутися до головної сторінки</a></p>");

        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
