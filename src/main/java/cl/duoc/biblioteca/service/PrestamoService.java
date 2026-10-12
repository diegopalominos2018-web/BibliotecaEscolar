package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.model.Prestamo;

import java.sql.SQLException;
import java.time.LocalDate;

public class PrestamoService {

    private final LibroDAO libroDAO;
    private final PrestamoDAO prestamoDAO;

    public PrestamoService() {
        libroDAO = new LibroDAO();
        prestamoDAO = new PrestamoDAO();
    }

    public synchronized boolean registrarPrestamo(
            int idEstudiante,
            int idLibro
    ) throws SQLException {

        Libro libro = libroDAO.buscarPorId(idLibro);

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro seleccionado no existe."
            );
        }

        if (libro.getStock() <= 0) {
            return false;
        }

        LocalDate fechaPrestamo = LocalDate.now();

        LocalDate fechaDevolucion =
                fechaPrestamo.plusDays(7);

        Prestamo prestamo = new Prestamo(
                0,
                idEstudiante,
                idLibro,
                fechaPrestamo,
                fechaDevolucion,
                false
        );

        boolean prestamoCreado = prestamoDAO.crear(prestamo);

        if (!prestamoCreado) {
            return false;
        }

        libro.setStock(libro.getStock() - 1);

        boolean stockActualizado = libroDAO.actualizar(libro);

        if (!stockActualizado) {
            throw new SQLException(
                    "El préstamo se registró, "
                            + "pero no fue posible actualizar el stock."
            );
        }

        return true;
    }

    public synchronized boolean registrarDevolucion(
            int idPrestamo
    ) throws SQLException {

        Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);

        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "El préstamo seleccionado no existe."
            );
        }

        if (prestamo.isDevuelto()) {
            return false;
        }

        Libro libro = libroDAO.buscarPorId(
                prestamo.getIdLibro()
        );

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro asociado al préstamo no existe."
            );
        }

        boolean devolucionRegistrada =
                prestamoDAO.registrarDevolucion(idPrestamo);

        if (!devolucionRegistrada) {
            return false;
        }

        libro.setStock(libro.getStock() + 1);

        boolean stockActualizado = libroDAO.actualizar(libro);

        if (!stockActualizado) {
            throw new SQLException(
                    "La devolución se registró, "
                            + "pero no fue posible actualizar el stock."
            );
        }

        return true;
    }
}
