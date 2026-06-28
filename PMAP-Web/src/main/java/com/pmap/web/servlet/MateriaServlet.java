package com.pmap.web.servlet;

import com.pmap.web.dao.MateriaDAO;
import com.pmap.web.model.Materia;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "MateriaServlet", urlPatterns = {"/materias"})
public class MateriaServlet extends HttpServlet {

    private final MateriaDAO materiaDAO = new MateriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("action");
        if (accion == null || accion.isBlank() || "listar".equalsIgnoreCase(accion)) {
            listarMaterias(request, response);
            return;
        }

        switch (accion) {
            case "nuevo" -> request.getRequestDispatcher("/formMateria.jsp").forward(request, response);
            case "editar" -> mostrarFormularioEdicion(request, response);
            case "eliminar" -> eliminarMateria(request, response);
            case "buscar" -> buscarMateria(request, response);
            default -> listarMaterias(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("action");

        if ("guardar".equalsIgnoreCase(accion)) {
            guardarMateria(request, response);
        } else if ("actualizar".equalsIgnoreCase(accion)) {
            actualizarMateria(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/materias?action=listar");
        }
    }

    private void listarMaterias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("materias", materiaDAO.listar());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/materias.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException excepcion) {
            throw new ServletException("No fue posible listar las materias.", excepcion);
        }
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = obtenerId(request);
        if (id <= 0) {
            response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=ID%20inv%C3%A1lido");
            return;
        }

        try {
            Materia materia = materiaDAO.buscarPorId(id);
            if (materia == null) {
                response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=Materia%20no%20encontrada");
                return;
            }

            request.setAttribute("materia", materia);
            request.getRequestDispatcher("/editarMateria.jsp").forward(request, response);
        } catch (SQLException excepcion) {
            throw new ServletException("No fue posible abrir el formulario de edición.", excepcion);
        }
    }

    private void buscarMateria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = obtenerId(request);

        try {
            if (id > 0) {
                Materia materia = materiaDAO.buscarPorId(id);
                if (materia != null) {
                    request.setAttribute("materias", List.of(materia));
                    request.setAttribute("mensaje", "Materia encontrada.");
                } else {
                    request.setAttribute("materias", List.of());
                    request.setAttribute("mensaje", "No se encontró la materia.");
                }
            } else {
                request.setAttribute("materias", materiaDAO.listar());
                request.setAttribute("mensaje", "Digite un ID válido para buscar.");
            }

            request.getRequestDispatcher("/materias.jsp").forward(request, response);
        } catch (SQLException excepcion) {
            throw new ServletException("No fue posible realizar la búsqueda.", excepcion);
        }
    }

    private void guardarMateria(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Materia materia = construirMateriaDesdeRequest(request);
        String validacion = validarMateria(materia, false);

        if (validacion != null) {
            request.setAttribute("error", validacion);
            request.setAttribute("materia", materia);
            request.getRequestDispatcher("/formMateria.jsp").forward(request, response);
            return;
        }

        try {
            if (materiaDAO.insertar(materia) > 0) {
                response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=Materia%20guardada%20correctamente");
            } else {
                request.setAttribute("error", "No fue posible guardar la materia.");
                request.setAttribute("materia", materia);
                request.getRequestDispatcher("/formMateria.jsp").forward(request, response);
            }
        } catch (SQLException excepcion) {
            throw new ServletException("No fue posible guardar la materia.", excepcion);
        }
    }

    private void actualizarMateria(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Materia materia = construirMateriaDesdeRequest(request);
        String validacion = validarMateria(materia, true);

        if (validacion != null) {
            request.setAttribute("error", validacion);
            request.setAttribute("materia", materia);
            request.getRequestDispatcher("/editarMateria.jsp").forward(request, response);
            return;
        }

        try {
            if (materiaDAO.actualizar(materia)) {
                response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=Materia%20actualizada%20correctamente");
            } else {
                request.setAttribute("error", "No fue posible actualizar la materia.");
                request.setAttribute("materia", materia);
                request.getRequestDispatcher("/editarMateria.jsp").forward(request, response);
            }
        } catch (SQLException excepcion) {
            throw new ServletException("No fue posible actualizar la materia.", excepcion);
        }
    }

    private void eliminarMateria(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = obtenerId(request);

        try {
            if (id > 0 && materiaDAO.eliminar(id)) {
                response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=Materia%20eliminada%20correctamente");
            } else {
                response.sendRedirect(request.getContextPath() + "/materias?action=listar&mensaje=No%20fue%20posible%20eliminar%20la%20materia");
            }
        } catch (SQLException excepcion) {
            throw new IOException("No fue posible eliminar la materia.", excepcion);
        }
    }

    private Materia construirMateriaDesdeRequest(HttpServletRequest request) {
        Materia materia = new Materia();

        String idTexto = request.getParameter("id");
        if (idTexto != null && !idTexto.isBlank()) {
            try {
                materia.setId(Integer.parseInt(idTexto));
            } catch (NumberFormatException ignored) {
                materia.setId(0);
            }
        }

        materia.setNombre(valorLimpio(request.getParameter("nombre")));
        materia.setDescripcion(valorLimpio(request.getParameter("descripcion")));
        materia.setCategoria(valorLimpio(request.getParameter("categoria")));
        materia.setEstado(valorLimpio(request.getParameter("estado")));
        return materia;
    }

    private String validarMateria(Materia materia, boolean requiereId) {
        if (requiereId && materia.getId() <= 0) {
            return "El ID es obligatorio para actualizar.";
        }

        if (materia.getNombre() == null || materia.getNombre().isBlank()) {
            return "El nombre es obligatorio.";
        }

        if (materia.getNombre().length() > 100) {
            return "El nombre no puede superar 100 caracteres.";
        }

        if (materia.getCategoria() != null && materia.getCategoria().length() > 60) {
            return "La categoría no puede superar 60 caracteres.";
        }

        return null;
    }

    private String valorLimpio(String valor) {
        return valor != null ? valor.trim() : "";
    }

    private int obtenerId(HttpServletRequest request) {
        String idTexto = request.getParameter("id");
        if (idTexto == null || idTexto.isBlank()) {
            return 0;
        }

        try {
            return Integer.parseInt(idTexto);
        } catch (NumberFormatException excepcion) {
            return 0;
        }
    }
}
