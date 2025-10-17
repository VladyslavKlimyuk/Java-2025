package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "lab4_1_servlet", value = "/lab4_1_servlet")
public class Lab4_1_Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String quote =
            "Any fool can write code that a computer can understand. " +
                    "Good programmers write code that humans can understand.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"ru\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Цитата Мартіна Фаулера</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <h1>Цитата Мартіна Фаулера</h1>");
        out.println("    <p>");
        out.println("        \"<strong>" + quote + "</strong>\"");
        out.println("    </p>");
        out.println("    <p>— Мартін Фаулер (Martin Fowler)</p>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
