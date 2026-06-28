<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PMAP - Editar Materia</title>
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
        .container { max-width: 900px; margin: 0 auto; padding: 24px; }
        .card {
            background: var(--blanco);
            border: 1px solid var(--gris);
            border-radius: 14px;
            box-shadow: 0 10px 30px rgba(37, 99, 235, 0.08);
            padding: 20px;
        }

        .grid {
            display: grid;
            gap: 14px;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        }

        .full { grid-column: 1 / -1; }
        .form-group { display: flex; flex-direction: column; gap: 6px; }
        label { font-weight: 700; font-size: 14px; }
        input, textarea, select {
            border: 1px solid var(--gris);
            border-radius: 10px;
            padding: 11px 12px;
            font-size: 14px;
            background: var(--blanco);
        }
        textarea { min-height: 120px; resize: vertical; }
        .readonly { background: #f9fafb; }

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
    </style>
</head>
<body>
<header class="header">
    <h1>PMAP - Gestión de Materias</h1>
    <p>Edición de registro</p>
</header>

<main class="container">
    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <section class="card">
        <form action="${pageContext.request.contextPath}/materias" method="post">
            <input type="hidden" name="action" value="actualizar">
            <input type="hidden" name="id" value="${materia.id}">

            <div class="grid">
                <div class="form-group">
                    <label for="idVista">ID</label>
                    <input type="text" id="idVista" value="${materia.id}" class="readonly" readonly>
                </div>

                <div class="form-group">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado">
                        <option value="Activa" <c:if test="${materia.estado eq 'Activa'}">selected</c:if>>Activa</option>
                        <option value="Inactiva" <c:if test="${materia.estado eq 'Inactiva'}">selected</c:if>>Inactiva</option>
                    </select>
                </div>

                <div class="form-group full">
                    <label for="nombre">Nombre</label>
                    <input type="text" id="nombre" name="nombre" maxlength="100" value="${materia.nombre}" required>
                </div>

                <div class="form-group full">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" maxlength="1000">${materia.descripcion}</textarea>
                </div>

                <div class="form-group full">
                    <label for="categoria">Categoría</label>
                    <input type="text" id="categoria" name="categoria" maxlength="60" value="${materia.categoria}">
                </div>
            </div>

            <div class="actions">
                <button type="submit" class="btn btn-primary">Actualizar</button>
                <a href="${pageContext.request.contextPath}/materias?action=listar" class="btn btn-light">Volver al listado</a>
            </div>
        </form>
    </section>
</main>
</body>
</html>
