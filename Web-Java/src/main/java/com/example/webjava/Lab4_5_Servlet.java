package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "lab4_5_servlet", value = "/lab4_5_servlet")
public class Lab4_5_Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String manufacturer_name = "Lenovo";
    private static final String country = "Гонконг (Китай), Моррісвілл (США)";
    private static final String employee_count = "75 000+";
    private static final String logo_url = "/images/Lenovo.png";
    private static final String general_info = "Lenovo Group Limited — " +
            "китайська багатонаціональна технологічна компанія, що спеціалізується на розробці, " +
            "виробництві та продажі персональних комп'ютерів, планшетів, смартфонів, робочих станцій " +
            "та серверів. Відома своїми надійними лінійками ThinkPad і Yoga.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath();

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String page = request.getParameter("page");
        if (page == null) {
            page = "main";
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang=\"uk\">");
        out.println("<head>");
        out.println("    <meta charset=\"UTF-8\">");
        out.println("    <title>Виробник ноутбуків: " + manufacturer_name + "</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; margin: 0; padding: 0; " +
                "background-color: #f4f4f4; }");
        out.println("        .header { background-color: #E4002B; color: white; padding: 15px 30px; " +
                "display: flex; justify-content: space-between; align-items: center; }");
        out.println("        .nav a { color: white; margin: 0 15px; text-decoration: none; " +
                "font-weight: bold; }");
        out.println("        .container { padding: 30px; max-width: 1200px; margin: 0 auto; " +
                "background-color: white; min-height: 80vh; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
        out.println("        .card { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; " +
                "border-radius: 8px; display: flex; gap: 20px; align-items: flex-start; }");
        out.println("        .card img { max-width: 200px; height: auto; border-radius: 4px; }");
        out.println("        .info-block { margin-bottom: 10px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class=\"header\">");
        out.println("        <div class=\"logo\"><img src=\"" + contextPath + logo_url + "\" " +
                "alt=\"Логотип Lenovo\"></div>");
        out.println("        <div class=\"nav\">");
        out.println("            <a href=\"lab4_5_servlet?page=info\">Загальна інформація</a>");
        out.println("            <a href=\"lab4_5_servlet?page=news\">Новини</a>");
        out.println("            <a href=\"lab4_5_servlet?page=models\">Моделі ноутбуків</a>");
        out.println("        </div>");
        out.println("    </div>");

        out.println("    <div class=\"container\">");

        switch (page) {
            case "info":
                displayGeneralInfo(out, contextPath);
                break;
            case "news":
                displayNews(out, contextPath);
                break;
            case "models":
                displayModels(out, contextPath);
                break;
            case "main":
            default:
                displayMainPage(out);
                break;
        }

        out.println("        <p style=\"margin-top: 40px;\"><a href=\"/Web_Java_war_exploded/\">" +
                "← Повернутися до навігаційного меню</a></p>");
        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    private void displayMainPage(PrintWriter out) {
        out.println("<h2>Ласкаво просимо на сайт виробника ноутбуків " + manufacturer_name + "!</h2>");
        out.println("<p style='font-size: 1.1em; color: #333;'>");
        out.println("Lenovo — це світовий технологічний лідер, який прагне створювати пристрої та " +
                "рішення для інтелектуального світу. ");
        out.println("Від легендарної лінійки ThinkPad до інноваційних гібридів Yoga та потужних " +
                "ігрових ПК Legion, ми надаємо технології, ");
        out.println("які допомагають нашим клієнтам трансформувати роботу, навчання та розваги.");
        out.println("</p>");
        out.println("<h3>Наші головні цінності:</h3>");
        out.println("<ul style='list-style-type: disc; padding-left: 20px;'>");
        out.println("<li>Інновації: Постійний пошук нових рішень.</li>");
        out.println("<li>Довіра: Надійність та безпека, перевірена часом.</li>");
        out.println("<li>Різноманітність: Продукти для будь-яких потреб — від бізнесу до ігор.</li>");
        out.println("</ul>");
        out.println("<p>Для детальної інформації про компанію, останні новини чи конкретні моделі, " +
                "будь ласка, скористайтеся навігаційним меню у верхній частині сторінки.</p>");
    }

    private void displayGeneralInfo(PrintWriter out, String contextPath) {
        out.println("<p><a href=\"lab4_5_servlet?page=main\">← Повернутися на головну сторінку</a></p>");
        out.println("<h2>Загальна інформація про виробника</h2>");
        out.println("<div class=\"info-block\"><strong>Назва виробника:</strong> "
                + manufacturer_name + "</div>");
        out.println("<div class=\"info-block\"><strong>Штаб-квартира:</strong> " + country + "</div>");
        out.println("<div class=\"info-block\"><strong>Кількість співробітників:</strong> "
                + employee_count + "</div>");
        out.println("<div class=\"info-block\"><strong>Коротка інформація:</strong><p>"
                + general_info + "</p></div>");
        out.println("<div class=\"info-block\"><strong>Логотип:</strong><br><img src=\""
                + contextPath + logo_url + "\" alt=\"Логотип Lenovo\"></div>");
    }

    private void displayNews(PrintWriter out, String contextPath) {
        out.println("<p><a href=\"lab4_5_servlet?page=main\">← Повернутися на головну сторінку</a></p>");
        out.println("<h2>Останні новини Lenovo</h2>");

        String[][] news = {
                {"Lenovo представляє нові ThinkPad X1", "Флагманська серія ThinkPad отримала оновлення " +
                        "з процесорами Intel Core Ultra та покращеною автономністю.",
                        contextPath + "/images/ThinkPad X1 Carbon Gen 12.jpg"},
                {"Анонс ігрової серії Legion", "Презентація ноутбуків Legion Pro з дисплеями Mini-LED " +
                        "та найновішими графічними адаптерами NVIDIA.",
                        contextPath + "/images/Legion Pro 7i.jpg"},
                {"Запуск Yoga Book 9i Gen 2", "Друге покоління інноваційного ноутбука з двома " +
                        "OLED-екранами для максимальної багатозадачності.",
                        contextPath + "/images/Yoga Book 9i Gen 2.jpg"}
        };

        for (String[] item : news) {
            out.println("<div class=\"card\">");
            out.println("    <img src=\"" + item[2] + "\" onerror=\"this.onerror=null;" +
                    "this.src='" + contextPath + "/images/NO_IMAGE.png'\" alt=\"Фото новини\">");
            out.println("    <div>");
            out.println("        <h3>" + item[0] + "</h3>");
            out.println("        <p>" + item[1] + "</p>");
            out.println("    </div>");
            out.println("</div>");
        }
    }

    private void displayModels(PrintWriter out, String contextPath) {
        out.println("<p><a href=\"lab4_5_servlet?page=main\">← Повернутися на головну сторінку</a></p>");
        out.println("<h2>Популярні моделі ноутбуків Lenovo</h2>");

        String[][] models = {
                {"ThinkPad X1 Carbon Gen 12", "Преміальний бізнес-ноутбук. Надлегкий корпус, " +
                        "висока надійність і безпека. Ідеальний для корпоративного використання.",
                        contextPath + "/images/ThinkPad X1 Carbon Gen 12.jpg"},
                {"IdeaPad Slim 5", "Універсальний ноутбук для повсякденних завдань, навчання та " +
                        "домашнього використання. Гарне співвідношення ціни та якості.",
                        contextPath + "/images/IdeaPad Slim 5.jpg"},
                {"Legion Pro 7i", "Потужна ігрова станція з передовими системами охолодження та " +
                        "високопродуктивними компонентами для геймерів.",
                        contextPath + "/images/Legion Pro 7i.jpg"}
        };

        for (String[] item : models) {
            out.println("<div class=\"card\">");
            out.println("    <img src=\"" + item[2] + "\" onerror=\"this.onerror=null;" +
                    "this.src='" + contextPath + "/images/NO_IMAGE.png'\" alt=\"Фото моделі\">");
            out.println("    <div>");
            out.println("        <h3>" + item[0] + "</h3>");
            out.println("        <p>" + item[1] + "</p>");
            out.println("    </div>");
            out.println("</div>");
        }
    }
}