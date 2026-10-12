package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.model.Libro;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.util.List;

public class LibroFrame extends JFrame {

    private final LibroDAO libroDAO;
    private final DefaultTableModel modeloTabla;
    private final JTable tablaLibros;

    public LibroFrame() {
        libroDAO = new LibroDAO();

        setTitle("Biblioteca Escolar - Gestión de libros");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel(
                "Gestión de libros",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        titulo.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Título",
                        "Autor",
                        "ISBN",
                        "Editorial",
                        "Stock",
                        "Categoría"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setAutoCreateRowSorter(true);
        tablaLibros.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scroll = new JScrollPane(tablaLibros);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton btnAgregar = new JButton("Agregar libro");
        JButton btnEditar = new JButton("Editar seleccionado");
        JButton btnEliminar = new JButton("Eliminar seleccionado");
        JButton btnActualizar = new JButton("Actualizar listado");
        JButton btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(evento -> agregarLibro());

        btnEditar.addActionListener(evento -> editarLibro());

        btnEliminar.addActionListener(evento -> eliminarLibro());

        btnActualizar.addActionListener(evento -> cargarLibros());

        btnCerrar.addActionListener(evento -> dispose());

        cargarLibros();
    }

    private void cargarLibros() {
        modeloTabla.setRowCount(0);

        try {
            List<Libro> libros = libroDAO.listarTodos();

            for (Libro libro : libros) {
                modeloTabla.addRow(new Object[]{
                        libro.getId(),
                        libro.getTitulo(),
                        libro.getAutor(),
                        libro.getIsbn(),
                        libro.getEditorial(),
                        libro.getStock(),
                        libro.getIdCategoria()
                });
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible cargar los libros.",
                    error
            );
        }
    }

    private void agregarLibro() {
        LibroFormularioDialog formulario =
                new LibroFormularioDialog(this, null);

        formulario.setVisible(true);

        if (!formulario.isGuardado()) {
            return;
        }

        try {
            boolean creado = libroDAO.crear(formulario.getLibro());

            if (creado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Libro agregado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarLibros();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo agregar el libro.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible agregar el libro.\n"
                            + "Verifique que el ISBN no esté repetido.",
                    error
            );
        }
    }

    private void editarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla para editar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo = tablaLibros.convertRowIndexToModel(
                filaSeleccionada
        );

        int idLibro = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        try {
            Libro libro = libroDAO.buscarPorId(idLibro);

            if (libro == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "El libro ya no existe en la base de datos.",
                        "Libro no encontrado",
                        JOptionPane.WARNING_MESSAGE
                );

                cargarLibros();
                return;
            }

            LibroFormularioDialog formulario =
                    new LibroFormularioDialog(this, libro);

            formulario.setVisible(true);

            if (!formulario.isGuardado()) {
                return;
            }

            boolean actualizado = libroDAO.actualizar(
                    formulario.getLibro()
            );

            if (actualizado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Libro actualizado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarLibros();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el libro.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible editar el libro.\n"
                            + "Verifique que el ISBN no esté repetido.",
                    error
            );
        }
    }

    private void eliminarLibro() {
        int filaSeleccionada = tablaLibros.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un libro de la tabla para eliminar.",
                    "Selección requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int filaModelo = tablaLibros.convertRowIndexToModel(
                filaSeleccionada
        );

        int idLibro = (int) modeloTabla.getValueAt(
                filaModelo,
                0
        );

        String tituloLibro = String.valueOf(
                modeloTabla.getValueAt(filaModelo, 1)
        );

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el libro:\n"
                        + tituloLibro
                        + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean eliminado = libroDAO.eliminar(idLibro);

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Libro eliminado correctamente.",
                        "Operación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarLibros();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el libro.",
                        "Operación no realizada",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (SQLException error) {
            mostrarError(
                    "No fue posible eliminar el libro.\n"
                            + "Puede tener préstamos asociados.",
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