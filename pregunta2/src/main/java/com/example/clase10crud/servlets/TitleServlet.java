package com.example.clase10crud.servlets;

import com.example.clase10crud.beans.Title;
import com.example.clase10crud.daos.TitleDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "TitleServlet", value = "/TitleServlet")public class JobServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        TitleDao titleDao = new TitleDao();

        switch (action){
            case "lista":
                //saca del modelo
                ArrayList<Title> list = titleDao.listar();

                //mandar la lista a la vista -> job/lista.jsp
                request.setAttribute("lista",list);
                RequestDispatcher rd = request.getRequestDispatcher("title/lista.jsp");
                rd.forward(request,response);
                break;
            case "new":
                request.getRequestDispatcher("title/form_new.jsp").forward(request,response);
                break;
            case "edit":
                String id = request.getParameter("id");
                Title title = TitleDao.buscarPorId(id);

                if(title != null){
                    request.setAttribute("title",title);
                    request.getRequestDispatcher("title/form_edit.jsp").forward(request,response);
                }else{
                    response.sendRedirect(request.getContextPath() + "/JobServlet");
                }
                break;
            case "del":
                String idd = request.getParameter("id");
                Title titlee = titleDao.buscarPorId(idd);

                if(titlee != null){
                    try {
                        titleDao.borrar(idd);
                    } catch (SQLException e) {
                        System.out.println("Log: excepcion: " + e.getMessage());
                    }
                }
                response.sendRedirect(request.getContextPath() + "/TitleServlet");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");


        TitleDao titleDao = new TitleDao();

        String action = request.getParameter("action") == null ? "crear" : request.getParameter("action");

        switch (action){
            case "crear"://voy a crear un nuevo titulo
                String jobId = request.getParameter("emp_no");
                String jobTitle = request.getParameter("title");
                String minSalary = request.getParameter("from_date");
                String maxSalary = request.getParameter("to_date");

                boolean isAllValid = true;

                if(jobTitle.length() > 35){
                    isAllValid = false;
                }

                if(jobId.length() > 10){
                    isAllValid = false;
                }

                if(isAllValid){

                    Title title = TitleDao.buscarPorId(jobId);

                    if(title == null){
                        titleDao.crear(emp_no,title,from_date,to_date);
                        response.sendRedirect(request.getContextPath() + "/TitleServlet");
                    }else{
                        request.getRequestDispatcher("title/form_new.jsp").forward(request,response);
                    }
                }else{
                    request.getRequestDispatcher("title/form_new.jsp").forward(request,response);
                }
                break;
            case "e": //voy a actualizar
                String jobId2 = request.getParameter("emp_no");
                String jobTitle2 = request.getParameter("title");
                String minSalary2 = request.getParameter("from_date");
                String maxSalary2 = request.getParameter("to_date");

                boolean isAllValid2 = true;

                if(jobTitle2.length() > 35){
                    isAllValid2 = false;
                }

                if(jobId2.length() > 10){
                    isAllValid2 = false;
                }
                if(isAllValid2){
                    Title title = new Title();
                    title.setEmp_no(jobId2);
                    title.setTitle(jobTitle2);
                    title.setMinSalary(Integer.parseInt(minSalary2));
                    title.setMaxSalary(Integer.parseInt(maxSalary2));

                    titleDao.actualizar(title);
                    response.sendRedirect(request.getContextPath() + "/TitleServlet");
                }else{
                    request.setAttribute("title",titleDao.buscarPorId(jobId2));
                    request.getRequestDispatcher("title/form_edit.jsp").forward(request,response);
                }
                break;
            case "s":
                String textBuscar = request.getParameter("textoBuscar");
                ArrayList<Title> lista = titleDao.buscarIdOrTitle(textBuscar);

                request.setAttribute("lista",lista);
                request.setAttribute("busqueda",textBuscar);
                request.getRequestDispatcher("title/lista.jsp").forward(request,response);

                break;
        }
    }
}
