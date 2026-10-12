package cl.duoc.biblioteca.EstilosUI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public final class EstilosUI {

    public static final Color AZUL_PRINCIPAL =
            new Color(30, 64, 100);

    public static final Color AZUL_SECUNDARIO =
            new Color(51, 100, 150);

    public static final Color AZUL_CLARO =
            new Color(224, 236, 248);

    public static final Color FONDO_VENTANA =
            new Color(245, 248, 252);

    public static final Color FONDO_PANEL =
            Color.WHITE;

    public static final Color TEXTO_OSCURO =
            new Color(38, 50, 56);

    public static final Color VERDE_ACCION =
            new Color(46, 125, 50);

    public static final Color ROJO_ACCION =
            new Color(183, 28, 28);

    private EstilosUI() {
    }

    public static JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel();

        panel.setBackground(FONDO_VENTANA);

        return panel;
    }

    public static Border crearBordePanel() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 220, 230)
                ),
                BorderFactory.createEmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );
    }

    public static void aplicarTitulo(JLabel etiqueta) {
        etiqueta.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        etiqueta.setForeground(AZUL_PRINCIPAL);

        etiqueta.setHorizontalAlignment(
                SwingConstants.CENTER
        );
    }

    public static void aplicarSubtitulo(JLabel etiqueta) {
        etiqueta.setFont(
                new Font("SansSerif", Font.PLAIN, 15)
        );

        etiqueta.setForeground(TEXTO_OSCURO);

        etiqueta.setHorizontalAlignment(
                SwingConstants.CENTER
        );
    }

    public static void aplicarBotonPrincipal(
            JButton boton
    ) {
        boton.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_PRINCIPAL);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setOpaque(true);
    }

    public static void aplicarBotonSecundario(
            JButton boton
    ) {
        boton.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_SECUNDARIO);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setOpaque(true);
    }

    public static void aplicarBotonPeligro(
            JButton boton
    ) {
        boton.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(ROJO_ACCION);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        boton.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.setOpaque(true);
    }

    public static void aplicarTabla(JTable tabla) {
        tabla.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        tabla.setForeground(TEXTO_OSCURO);
        tabla.setBackground(Color.WHITE);

        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(TEXTO_OSCURO);

        tabla.setGridColor(new Color(220, 225, 230));
        tabla.setRowHeight(28);

        JTableHeader encabezado = tabla.getTableHeader();

        encabezado.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        encabezado.setForeground(Color.WHITE);
        encabezado.setBackground(AZUL_PRINCIPAL);

        encabezado.setReorderingAllowed(false);
    }
}
