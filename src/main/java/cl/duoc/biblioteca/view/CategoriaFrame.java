package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.model.Categoria;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

public class CategoriaFrame extends JFrame {

    private final CategoriaDAO categoriaDAO;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaCategorias;

    public CategoriaFrame() {
        categoriaDAO = new CategoriaDAO();

        setTitle("Biblioteca Escolar - Gestión de categorías");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JLabel titulo = new JLabel(
                "Gestión de categorías",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(new Color(31, 78, 121));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(18, 10, 8, 10)
        );

        JLabel subtitulo = new JLabel(
                "Administre las categorías disponibles para los libros.",
                SwingConstants.CENTER
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        subtitulo.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 15, 10)
        );

        JPanel panelEncabezado = new JPanel(new BorderLayout());

        panelEncabezado.add(titulo, BorderLayout.CENTER);
        panelEncabezado.add(subtitulo, BorderLayout.SOUTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre de categoría"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloTabla);

        tablaCategorias.setAutoCreateRowSorter(true);

        tablaCategorias.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaCategorias.setRowHeight(26);

        tablaCategorias.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        JScrollPane scroll = new JScrollPane(tablaCategorias);

        scroll.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20)
        );

        JButton btnAgregar = new JButton("Agregar categoría");

        JButton btnEditar = new JButton(
                "Editar seleccionada"
        );

        JButton btnEliminar = new JButton(
                "Eliminar seleccionada"
        );

        JButton btnActualizar = new JButton(
                "Actualizar listado"
        );

        JButton btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 14)
        );

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelEncabezado, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(
                evento -> agregarCategoria()
        );

        btnEditar.addActionListener(
                evento -> editarCategoria()
        );

        btnEliminar.addActionListener(
                evento -> eliminarCategoria()
        );

        btnActualizar.addActionListener(
                evento -> cargarCategorias()
        );

        btnCerrar.addActionListener(
                evento -> dispose()
        );

        cargarCategorias();
    }

    private void cargarCategorias() {
        modeloTabla.setRowCount(0);

        try {
            List<Categoria> categorias =
                    categoriaDAO.listarTodos();

            for (Categoria categoria : categorias) {
                modeloTabla.addRow(new Object[]{
                        categoria.getId(),
                        categoria.getNombre()
                });
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar las categorías.",
                    error
            );
        }
    }

    private void agregarCategoria() {
        String nombre = JOptionPane.showInputDialog(
                this,
                "Ingrese el nombre de la nueva categoría:",
                "Agregar categoría",
                JOptionPane.QUESTION_MESSAGE
        );

        if (nombre == null) {
            return;
        }

        nombre = nombre.trim();

        if (nombre.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre de la categoría no puede estar vacío.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {
            Categoria categoria = new Categoria(0, nombre);

            boolean creada = categoriaDAO.crear(categoria);

            if (creada) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoría agregada correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCategorias();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo agregar la categoría.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible agregar la categoría.\n"
                            + "Verifique que el nombre no esté repetido.",
                    error
            );
        }
    }

    private void editarCategoria() {
        int filaSeleccionada =
                tablaCategorias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla para editar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaCategorias.convertRowIndexToModel(
                        filaSeleccionada
                );

        int idCategoria = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        String nombreActual = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 1)
        );

        String nuevoNombre = JOptionPane.showInputDialog(
                this,
                "Modifique el nombre de la categoría:",
                nombreActual
        );

        if (nuevoNombre == null) {
            return;
        }

        nuevoNombre = nuevoNombre.trim();

        if (nuevoNombre.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre de la categoría no puede estar vacío.",
                    "Dato obligatorio",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {
            Categoria categoria = new Categoria(
                    idCategoria,
                    nuevoNombre
            );

            boolean actualizada = categoriaDAO.actualizar(categoria);

            if (actualizada) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoría actualizada correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCategorias();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar la categoría.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible actualizar la categoría.\n"
                            + "Verifique que el nombre no esté repetido.",
                    error
            );
        }
    }

    private void eliminarCategoria() {
        int filaSeleccionada =
                tablaCategorias.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla para eliminar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo =
                tablaCategorias.convertRowIndexToModel(
                        filaSeleccionada
                );

        int idCategoria = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        String nombreCategoria = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 1)
        );

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar la categoría:\n"
                        + nombreCategoria
                        + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminada = categoriaDAO.eliminar(idCategoria);

            if (eliminada) {
                JOptionPane.showMessageDialog(
                        this,
                        "Categoría eliminada correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarCategorias();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar la categoría.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible eliminar la categoría.\n"
                            + "La categoría podría estar asociada a uno o más libros.",
                    error
            );
        }
    }

    private void mostrarError(String mensaje, SQLException error) {
        JOptionPane.showMessageDialog(
                this,
                mensaje
                        + "\n\nDetalle técnico: "
                        + error.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
