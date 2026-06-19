package interpreter.analex.interfaces;
import interpreter.events.TokenEvent;

public interface ITokenEventListener {

    void help(TokenEvent event);
    void helpFlujo(TokenEvent event);
    // Paso 0 — Catálogos ACB
    void rol(TokenEvent event);
    void metodoPago(TokenEvent event);
    void planPago(TokenEvent event);
    void franjaHoraria(TokenEvent event);
    void tipoVehiculo(TokenEvent event);
    void tipoCurso(TokenEvent event);

    // CU-01
    void usuario(TokenEvent event);

    // CU-02
    void vehiculo(TokenEvent event);

    // CU-03
    void curso(TokenEvent event);

    // CU-05
    void inscripcion(TokenEvent event);

    // CU-06
    void certificacion(TokenEvent event);

    // CU-07
    void pago(TokenEvent event);

    // CU-08
    void reporte(TokenEvent event);

    // CU-09
    void reserva(TokenEvent event);

    void error(TokenEvent event);
}
