<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PMAP - Gestión de Materias</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        :root {
            --azul: #2563EB;
            --gris: #E5E7EB;
            --gris-oscuro: #374151;
            --fondo: #F3F4F6;
            --blanco: #FFFFFF;
        }

        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: linear-gradient(180deg, #f8fafc 0%, var(--fondo) 100%);
            color: var(--gris-oscuro);
        }

        .header { background: var(--azul); color: var(--blanco); padding: 24px 32px; }
        .container { max-width: 1200px; margin: 0 auto; padding: 24px; }
        .card {
            background: var(--blanco);
            border: 1px solid var(--gris);
            border-radius: 14px;
            box-shadow: 0 10px 30px rgba(37, 99, 235, 0.08);
            padding: 20px;
            margin-bottom: 20px;
        }

        .toolbar {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            align-items: end;
        }

        .grid {
            display: grid;
            gap: 14px;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        label { font-weight: 700; font-size: 14px; }
        input, textarea, select {
            border: 1px solid var(--gris);
            border-radius: 10px;
            padding: 11px 12px;
            font-size: 14px;
            background: var(--blanco);
        }

        textarea { min-height: 120px; resize: vertical; }

        .actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            margin-top: 14px;
        }

        .btn {
            display: inline-block;
            border: 0;
            border-radius: 10px;
            padding: 11px 16px;
            text-decoration: none;
            font-weight: 700;
            cursor: pointer;
        }

        .btn-primary { background: var(--azul); color: var(--blanco); }
        .btn-secondary { background: #6B7280; color: var(--blanco); }
        .btn-danger { background: #DC2626; color: var(--blanco); }
        .btn-light { background: #E5E7EB; color: #111827; }

        .message {
            padding: 14px 16px;
            border-radius: 10px;
            margin-bottom: 16px;
            background: #dbeafe;
            color: #1d4ed8;
            font-weight: 700;
        }

        .error {
            background: #fee2e2;
            color: #b91c1c;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--blanco);
        }

        th, td {
            border-bottom: 1px solid var(--gris);
            padding: 12px 10px;
            text-align: left;
            vertical-align: top;
        }

        th { background: #eff6ff; }
        .table-actions { display: flex; gap: 8px; flex-wrap: wrap; }
    </style>
</head>
<body>
<header class="header">
    <h1>PMAP - Gestión de Materias</h1>
    <p>Listado, búsqueda, edición y eliminación de materias</p>
</header>

<main class="container">
    <c:set var="mensajeMostrado" value="${not empty mensaje ? mensaje : param.mensaje}" />
    <c:if test="${not empty mensajeMostrado}">
        <div class="message">${mensajeMostrado}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <section class="card">
        <form action="${pageContext.request.contextPath}/materias" method="get" class="toolbar">
            <input type="hidden" name="action" value="buscar">
            <div class="form-group" style="min-width: 240px; flex: 1;">
                <label for="id">Buscar por ID</label>
                <input type="number" id="id" name="id" min="1" placeholder="Digite el ID">
            </div>
            <div class="actions">
                <button type="submit" class="btn btn-primary">Buscar</button>
                <a href="${pageContext.request.contextPath}/materias?action=nuevo" class="btn btn-secondary">Nueva materia</a>
                <a href="${pageContext.request.contextPath}/materias?action=listar" class="btn btn-light">Ver todas</a>
            </div>
        </form>
    </section>

    <section class="card">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Descripción</th>
                <th>Categoría</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty materias}">
                    <c:forEach var="materia" items="${materias}">
                        <tr>
                            <td>${materia.id}</td>
                            <td>${materia.nombre}</td>
                            <td>${materia.descripcion}</td>
                            <td>${materia.categoria}</td>
                            <td>${materia.estado}</td>
                            <td>
                                <div class="table-actions">
                                    <a class="btn btn-light" href="${pageContext.request.contextPath}/materias?action=editar&id=${materia.id}">Editar</a>
                                    <a class="btn btn-danger" href="${pageContext.request.contextPath}/materias?action=eliminar&id=${materia.id}" onclick="return confirm('¿Desea eliminar esta materia?');">Eliminar</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6">No hay materias registradas.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </section>
</main>
</body>
</html>
