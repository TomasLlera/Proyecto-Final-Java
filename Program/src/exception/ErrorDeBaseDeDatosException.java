package exception;

public class ErrorDeBaseDeDatosException extends RuntimeException {
    public ErrorDeBaseDeDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
