package es.medac.soporte.aerocheck;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public final class ConsoleEncoding {

    private ConsoleEncoding() { }

    public static void forceUtf8() {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
            // Si por algún motivo no se puede, no bloqueamos la ejecución.
        }
    }
}
