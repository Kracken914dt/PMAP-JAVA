package com.pmap.desktop.view;

import com.pmap.desktop.dao.MateriaDAO;
import com.pmap.desktop.model.Materia;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

public class MateriaForm extends JFrame {

    private static final Color AZUL = new Color(37, 99, 235);
    private static final Color GRIS_CLARO = new Color(243, 244, 246);

    private final MateriaDAO materiaDAO = new MateriaDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripción", "Categoría", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextArea txtDescripcion = new JTextArea(4, 20);
    private final JTextField txtCategoria = new JTextField();
    private final JComboBox<String> cmbEstado = new JComboBox<>(new String[]{"Activa", "Inactiva"});
    private final JTextField txtBuscarId = new JTextField();
    private final JTable tblMaterias = new JTable(tableModel);

    public MateriaForm() {
        configurarVentana();
        construirInterfaz();
        cargarMaterias();
    }

    private void configurarVentana() {
        setTitle("PMAP - Gestión de Materias");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(12, 12));
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelFormulario(), BorderLayout.WEST);
        add(crearPanelTabla(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AZUL);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel("PMAP - Gestión de Materias", SwingConstants.LEFT);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        panel.add(titulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBackground(GRIS_CLARO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(crearTituloSeccion("Registro de Materia"), gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        panel.add(crearEtiqueta("ID"), gbc);
        gbc.gridx = 1;
        txtId.setEditable(false);
        panel.add(txtId, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(crearEtiqueta("Nombre"), gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(crearEtiqueta("Descripción"), gbc);
        gbc.gridx = 1;
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtDescripcion), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(crearEtiqueta("Categoría"), gbc);
        gbc.gridx = 1;
        panel.add(txtCategoria, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(crearEtiqueta("Estado"), gbc);
        gbc.gridx = 1;
        panel.add(cmbEstado, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(crearPanelBotones(), gbc);

        gbc.gridy++;
        panel.add(crearPanelBuscar(), gbc);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JButton btnGuardar = crearBoton("Guardar", AZUL);
        JButton btnActualizar = crearBoton("Actualizar", new Color(59, 130, 246));
        JButton btnEliminar = crearBoton("Eliminar", new Color(220, 38, 38));
        JButton btnLimpiar = crearBoton("Limpiar", new Color(107, 114, 128));

        btnGuardar.addActionListener(e -> guardarMateria());
        btnActualizar.addActionListener(e -> actualizarMateria());
        btnEliminar.addActionListener(e -> eliminarMateria());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnGuardar, gbc);

        gbc.gridx = 1;
        panel.add(btnActualizar, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(btnEliminar, gbc);

        gbc.gridx = 1;
        panel.add(btnLimpiar, gbc);

        return panel;
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder("Buscar por ID"));

        JButton btnBuscar = crearBoton("Buscar", AZUL);
        btnBuscar.addActionListener(e -> buscarMateria());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(txtBuscarId, gbc);

        gbc.gridx = 1;
        panel.add(btnBuscar, gbc);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        tblMaterias.setRowHeight(28);
        tblMaterias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMaterias.getTableHeader().setReorderingAllowed(false);
        tblMaterias.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tblMaterias.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tblMaterias.setSelectionBackground(new Color(191, 219, 254));

        tblMaterias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMaterias.getSelectedRow() >= 0) {
                cargarDesdeFilaSeleccionada();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tblMaterias);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Materias"));
        return scrollPane;
    }

    private JLabel crearTituloSeccion(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(new Color(31, 41, 55));
        return label;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return boton;
    }

    private void cargarMaterias() {
        tableModel.setRowCount(0);

        try {
            List<Materia> materias = materiaDAO.listar();
            for (Materia materia : materias) {
                tableModel.addRow(new Object[]{
                        materia.getId(),
                        materia.getNombre(),
                        materia.getDescripcion(),
                        materia.getCategoria(),
                        materia.getEstado()
                });
            }
        } catch (SQLException excepcion) {
            mostrarError("No fue posible cargar las materias.", excepcion);
        }
    }

    private Materia construirMateriaDesdeFormulario() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return null;
        }

        if (txtNombre.getText().trim().length() > 100) {
            JOptionPane.showMessageDialog(this, "El nombre no puede superar 100 caracteres.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return null;
        }

        if (txtCategoria.getText().trim().length() > 60) {
            JOptionPane.showMessageDialog(this, "La categoría no puede superar 60 caracteres.", "Validación", JOptionPane.WARNING_MESSAGE);
            txtCategoria.requestFocus();
            return null;
        }

        Materia materia = new Materia();
        if (!txtId.getText().trim().isEmpty()) {
            materia.setId(Integer.parseInt(txtId.getText().trim()));
        }
        materia.setNombre(txtNombre.getText().trim());
        materia.setDescripcion(txtDescripcion.getText().trim());
        materia.setCategoria(txtCategoria.getText().trim());
        Object estadoSeleccionado = cmbEstado.getSelectedItem();
        materia.setEstado(estadoSeleccionado != null ? estadoSeleccionado.toString() : "");
        return materia;
    }

    private void guardarMateria() {
        Materia materia = construirMateriaDesdeFormulario();
        if (materia == null) {
            return;
        }

        try {
            int idGenerado = materiaDAO.insertar(materia);
            JOptionPane.showMessageDialog(this, "Materia guardada correctamente con ID " + idGenerado + ".", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarMaterias();
        } catch (SQLException excepcion) {
            mostrarError("No fue posible guardar la materia.", excepcion);
        }
    }

    private void actualizarMateria() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Materia materia = construirMateriaDesdeFormulario();
        if (materia == null) {
            return;
        }

        try {
            if (materiaDAO.actualizar(materia)) {
                JOptionPane.showMessageDialog(this, "Materia actualizada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMaterias();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible actualizar la materia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException excepcion) {
            mostrarError("No fue posible actualizar la materia.", excepcion);
        }
    }

    private void eliminarMateria() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una materia para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la materia seleccionada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (materiaDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Materia eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarMaterias();
            } else {
                JOptionPane.showMessageDialog(this, "No fue posible eliminar la materia.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID es inválido.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException excepcion) {
            mostrarError("No fue posible eliminar la materia.", excepcion);
        }
    }

    private void buscarMateria() {
        String textoId = txtBuscarId.getText().trim();
        if (textoId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite un ID para buscar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(textoId);
            Materia materia = materiaDAO.buscarPorId(id);
            if (materia != null) {
                mostrarMateriaEnFormulario(materia);
                seleccionarFilaPorId(id);
                JOptionPane.showMessageDialog(this, "Materia encontrada.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una materia con ese ID.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException excepcion) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numérico.", "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException excepcion) {
            mostrarError("No fue posible buscar la materia.", excepcion);
        }
    }

    private void mostrarMateriaEnFormulario(Materia materia) {
        txtId.setText(String.valueOf(materia.getId()));
        txtNombre.setText(materia.getNombre());
        txtDescripcion.setText(materia.getDescripcion());
        txtCategoria.setText(materia.getCategoria());
        cmbEstado.setSelectedItem(materia.getEstado());
    }

    private void cargarDesdeFilaSeleccionada() {
        int fila = tblMaterias.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(tableModel.getValueAt(fila, 0).toString());
            txtNombre.setText(tableModel.getValueAt(fila, 1).toString());
            txtDescripcion.setText(tableModel.getValueAt(fila, 2).toString());
            txtCategoria.setText(tableModel.getValueAt(fila, 3).toString());
            cmbEstado.setSelectedItem(tableModel.getValueAt(fila, 4).toString());
        }
    }

    private void seleccionarFilaPorId(int id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object valorId = tableModel.getValueAt(i, 0);
            if (valorId != null && Integer.parseInt(valorId.toString()) == id) {
                tblMaterias.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCategoria.setText("");
        cmbEstado.setSelectedIndex(0);
        txtBuscarId.setText("");
        tblMaterias.clearSelection();
        txtNombre.requestFocus();
    }

    private void mostrarError(String mensaje, Exception excepcion) {
        JOptionPane.showMessageDialog(this, mensaje + "\n" + excepcion.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MateriaForm().setVisible(true));
    }
}
