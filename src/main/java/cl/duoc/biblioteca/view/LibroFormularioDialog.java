package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.model.Libro;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;

public class LibroFormularioDialog extends JDialog {

    private final JTextField txtTitulo;
    private final JTextField txtAutor;
    private final JTextField txtIsbn;
    private final JTextField txtEditorial;
    private final JTextField txtStock;

    private final JComboBox<Categoria> cmbCategoria;

    private Libro libro;
    private boolean guardado;

    public LibroFormularioDialog(
            JFrame padre,
            Libro libro
    ) {
        super(
                padre,
                libro == null
                        ? "Agregar libro"
                        : "Editar libro",
                true
        );

        this.libro = libro;
        this.guardado = false;

        setSize(520, 410);
        setLocationRelativeTo(padre);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtIsbn = new JTextField();
        txtEditorial = new JTextField();
        txtStock = new JTextField();

        cmbCategoria = new JComboBox<>();

        JPanel formulario = new JPanel(new GridBagLayout());

        formulario.setBorder(
                BorderFactory.createEmptyBorder(20, 35, 15, 35)
        );

        GridBagConstraints restricciones = new GridBagConstraints();

        restricciones.insets = new Insets(7, 7, 7, 7);
        restricciones.fill = GridBagConstraints.HORIZONTAL;

        agregarCampo(
                formulario,
                restricciones,
                0,
                "Título:",
                txtTitulo
        );

        agregarCampo(
                formulario,
                restricciones,
                1,
                "Autor:",
                txtAutor
        );

        agregarCampo(
                formulario,
                restricciones,
                2,
                "ISBN:",
                txtIsbn
        );

        agregarCampo(
                formulario,
                restricciones,
                3,
                "Editorial:",
                txtEditorial
        );

        agregarCampo(
                formulario,
                restricciones,
                4,
                "Stock:",
                txtStock
        );

        agregarCampo(
                formulario,
                restricciones,
                5,
                "Categoría:",
                cmbCategoria
        );

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(formulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        cargarCategorias();
        cargarDatosLibro();

        btnGuardar.addActionListener(evento -> guardar());

        btnCancelar.addActionListener(evento -> dispose());
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            int fila,
            String etiqueta,
            JTextField campo
    ) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.weightx = 0;

        panel.add(new JLabel(etiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.gridy = fila;
        restricciones.weightx = 1.0;

        panel.add(campo, restricciones);
    }

    private void agregarCampo(
            JPanel panel,
            GridBagConstraints restricciones,
            int fila,
            String etiqueta,
            JComboBox<Categoria> combo
    ) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.weightx = 0;

        panel.add(new JLabel(etiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.gridy = fila;
        restricciones.weightx = 1.0;

        panel.add(combo, restricciones);
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();

        try {
            CategoriaDAO categoriaDAO = new CategoriaDAO();

            List<Categoria> categorias =
                    categoriaDAO.listarTodos();

            for (Categoria categoria : categorias) {
                cmbCategoria.addItem(categoria);
            }

        } catch (SQLException error) {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible cargar las categorías.\n\n"
                            + "Detalle técnico: "
                            + error.getMessage(),
                    "Error de base de datos",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarDatosLibro() {
        if (libro == null) {
            return;
        }

        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtIsbn.setText(libro.getIsbn());
        txtEditorial.setText(libro.getEditorial());
        txtStock.setText(String.valueOf(libro.getStock()));

        seleccionarCategoriaActual();
    }

    private void seleccionarCategoriaActual() {
        for (int indice = 0;
             indice < cmbCategoria.getItemCount();
             indice++) {

            Categoria categoria =
                    cmbCategoria.getItemAt(indice);

            if (categoria.getId() == libro.getIdCategoria()) {
                cmbCategoria.setSelectedIndex(indice);
                return;
            }
        }
    }

    private void guardar() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();
        String textoStock = txtStock.getText().trim();

        Categoria categoriaSeleccionada =
                (Categoria) cmbCategoria.getSelectedItem();

        if (
                titulo.isBlank()
                        || autor.isBlank()
                        || isbn.isBlank()
                        || editorial.isBlank()
                        || textoStock.isBlank()
        ) {
            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos.",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Categoría requerida",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int stock;

        try {
            stock = Integer.parseInt(textoStock);

        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtStock.requestFocus();

            return;
        }

        if (stock < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock no puede ser negativo.",
                    "Stock inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtStock.requestFocus();

            return;
        }

        int id = libro == null ? 0 : libro.getId();

        libro = new Libro(
                id,
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                categoriaSeleccionada.getId()
        );

        guardado = true;

        dispose();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Libro getLibro() {
        return libro;
    }
}
