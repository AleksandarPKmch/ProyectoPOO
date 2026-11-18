package carnets;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Lee la cadena de conexion de la variable de sistema MONGO_URI o MONGODB_URI,
 * o de un archivo .env. La contrasena nunca va escrita en el codigo.
 */
public final class Config {

    private static final String[] CLAVES = {"MONGO_URI", "MONGODB_URI"};
    private static final String[] RUTAS = {".env", "carnets-uvg/.env", "../.env"};

    private Config() {
    }

    public static String mongoUri() {
        for (String clave : CLAVES) {
            String deSistema = System.getenv(clave);
            if (deSistema != null && !deSistema.trim().isEmpty()) {
                return deSistema.trim();
            }
        }
        for (String ruta : RUTAS) {
            for (String clave : CLAVES) {
                String valor = leerDeArchivoEnv(Paths.get(ruta), clave);
                if (valor != null) {
                    return valor;
                }
            }
        }
        return null;
    }

    private static String leerDeArchivoEnv(Path archivo, String clave) {
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
