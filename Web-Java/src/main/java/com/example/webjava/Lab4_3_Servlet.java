package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "lab4_3_servlet", value = "/lab4_3_servlet")
public class Lab4_3_Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int MIN_NUM = 0;
    private static final int MAX_NUM = 100;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        session.setAttribute("min", MIN_NUM);
        session.setAttribute("max", MAX_NUM);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        displayGamePage(out, session, "Загадайте число від " + MIN_NUM +
                " до " + MAX_NUM + ".");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        int min = (int) session.getAttribute("min");
        int max = (int) session.getAttribute("max");
        int guess = (int) session.getAttribute("guess");

        String userResponse = request.getParameter("response");
        String message = "";
        boolean isGameOver = false;

        if (userResponse != null) {
            switch (userResponse) {
                case "too_low":
                    min = guess + 1;
                    message = "Добре, число більше, ніж " + guess + ". Продовжуємо пошук.";
                    break;
                case "too_high":
                    max = guess - 1;
                    message = "Зрозумів, число менше, ніж " + guess + ". Продовжуємо пошук.";
                    break;
                case "correct":
                    message = "Ура! Комп'ютер вгадав ваше число (" + guess + ")!";
                    isGameOver = true;
                    break;
            }
        }

        if (min > max && !isGameOver) {
            message = "Ви, мабуть, помилилися у відповідях. Спробуйте ще раз.";
            isGameOver = true;
        }

        session.setAttribute("min", min);
        session.setAttribute("max", max);

        displayGamePage(out, session, message, isGameOver);

        out.close();
    }

    private void displayGamePage(PrintWriter out, HttpSession session, String statusMessage) {
        displayGamePage(out, session, statusMessage, false);
    }

    private void displayGamePage(PrintWriter out, HttpSession session,
                                 String statusMessage, boolean isGameOver) {
        int min = (int) session.getAttribute("min");
        int max = (int) session.getAttribute("max");

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Вгадай число (Комп'ютер)</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; text-align: center; margin-top: " +
                "50px; }");
        out.println("        .controls { margin-top: 30px; }");
        out.println("        button { padding: 10px 20px; margin: 5px; cursor: pointer; }");
        out.println("        .guess { font-size: 2em; color: #007bff; }");
        out.println("        .status { margin: 20px 0; font-size: 1.1em; color: " +
                (isGameOver ? "green" : "#333") + "; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <h1>Гра \"Вгадай число\"</h1>");
        out.println("    <p class=\"status\">" + statusMessage + "</p>");

        if (isGameOver) {
            out.println("    <div class=\"controls\">");
            out.println("        <a href=\"lab4_3_servlet\"><button>Почати нову гру</button></a>");
            out.println("    </div>");
        } else {
            int nextGuess = min + (max - min) / 2;
            session.setAttribute("guess", nextGuess);

            out.println("    <h2>Ваше число: <span class=\"guess\">" + nextGuess + "</span>?</h2>");
            out.println("    <p>(Діапазон пошуку: " + min + " - " + max + ")</p>");

            out.println("    <form method=\"post\" action=\"lab4_3_servlet\" class=\"controls\">");
            out.println("        <h3>Як співвідноситься моє припущення з вашим числом?</h3>");

            out.println("        <button type=\"submit\" name=\"response\" value=\"too_low\">" +
                    "Моє число БІЛЬШЕ (" + nextGuess + " &lt; Ваше)</button>");

            out.println("        <button type=\"submit\" name=\"response\" value=\"correct\">" +
                    "ВГАДАВ!</button>");

            out.println("        <button type=\"submit\" name=\"response\" value=\"too_high\">" +
                    "Моє число МЕНШЕ (" + nextGuess + " &gt; Ваше)</button>");
            out.println("    </form>");
        }

        out.println("    <p style=\"margin-top: 50px;\"><a href=\"/Web_Java_war_exploded/\">" +
                "Повернутися до головної сторінки</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
