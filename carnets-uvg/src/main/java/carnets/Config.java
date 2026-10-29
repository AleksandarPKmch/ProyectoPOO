package carnets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Lee la cadena de conexion de la variable MONGO_URI o del archivo .env. */
public final class Config {

    private Config() {
    }

    public static String mongoUri() {
        String deSistema = System.getenv("MONGO_URI");
        if (deSistema != null && !deSistema.trim().isEmpty()) {
            return deSistema.trim();
        }
        return leerDeArchivoEnv("MONGO_URI");
    }

    private static String leerDeArchivoEnv(String clave) {
        Path archivo = Paths.get(".env");
        if (!Files.isRegularFile(archivo)) {
            return null;
        }
        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                String l = linea.replace("﻿", "").trim();
                if (l.isEmpty() || l.startsWith("#")) {
                    continue;
                }
                int igual = l.indexOf('=');
                if (igual > 0 && l.substring(0, igual).trim().equals(clave)) {
                    String valor = l.substring(igual + 1).trim();
                    if (valor.length() >= 2 && (valor.startsWith("\"") && valor.endsWith("\"")
                            || valor.startsWith("'") && valor.endsWith("'"))) {
                        valor = valor.substring(1, valor.length() - 1);
                    }
                    return valor.isEmpty() ? null : valor;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
