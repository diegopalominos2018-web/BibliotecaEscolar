package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.model.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class EstudianteFormularioDialog extends JDialog {

    private final JTextField txtNombre;
    private final JTextField txtRut;
    private final JTextField txtCurso;
    private final JTextField txtCorreo;

    private Estudiante estudiante;
    private boolean guardado;

    public EstudianteFormularioDialog(
            JFrame padre,
            Estudiante estudiante
    ) {
        super(
                padre,
                estudiante == null
                        ? "Agregar estudiante"
                        : "Editar estudiante",
                true
        );

        this.estudiante = estudiante;
        this.guardado = false;

        setSize(500, 350);
        setLocationRelativeTo(padre);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        txtNombre = new JTextField();
        txtRut = new JTextField();
        txtCurso = new JTextField();
        txtCorreo = new JTextField();

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
                "Nombre:",
                txtNombre
        );

        agregarCampo(
                formulario,
                restricciones,
                1,
                "RUT:",
                txtRut
        );

        JLabel lblAyudaRut = new JLabel(
                "Sin puntos y con guión. Ejemplo: 12345678-9"
        );

        lblAyudaRut.setFont(
                new Font("SansSerif", Font.ITALIC, 11)
        );

        lblAyudaRut.setForeground(Color.GRAY);

        restricciones.gridx = 1;
        restricciones.gridy = 2;
        restricciones.weightx = 1.0;

        formulario.add(lblAyudaRut, restricciones);

        agregarCampo(
                formulario,
                restricciones,
                3,
                "Curso:",
                txtCurso
        );

        agregarCampo(
                formulario,
                restricciones,
                4,
                "Correo:",
                txtCorreo
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

        cargarDatosEstudiante();

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

    private void cargarDatosEstudiante() {
        if (estudiante == null) {
            return;
        }

        txtNombre.setText(estudiante.getNombre());
        txtRut.setText(estudiante.getRut());
        txtCurso.setText(estudiante.getCurso());
        txtCorreo.setText(estudiante.getCorreo());
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (
                nombre.isBlank()
                        || rut.isBlank()
                        || curso.isBlank()
                        || correo.isBlank()
        ) {
            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos.",
                    "Campos obligatorios",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!rut.matches("\\d{7,8}-[\\dkK]")) {
            JOptionPane.showMessageDialog(
                    this,
                    "El RUT debe escribirse sin puntos y con guión.\n"
                            + "Ejemplo: 12345678-9",
                    "RUT inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtRut.requestFocus();

            return;
        }

        if (!correo.contains("@")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo válido.",
                    "Correo inválido",
                    JOptionPane.WARNING_MESSAGE
            );

            txtCorreo.requestFocus();

            return;
        }

        int id = estudiante == null ? 0 : estudiante.getId();

        estudiante = new Estudiante(
                id,
                nombre,
                rut,
                curso,
                correo
        );

        guardado = true;

        dispose();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
}