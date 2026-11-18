# Bus UVG, control de abordaje

Valida en la puerta del bus que quien sube pertenece a UVG. El piloto escanea o escribe el carnet y la pantalla responde en grande ACEPTADO en verde o RECHAZADO en rojo, con nombre, tipo de persona y motivo. Cada intento queda en una bitacora, tambien los rechazados, y de ahi salen las estadisticas.

## Que hace

- Pestana Abordaje: un solo campo para el carnet. Un lector USB de codigo de barras o QR funciona sin configurar nada, porque escribe como un teclado y termina con Enter. Acepta `26949` y tambien lecturas como `UVG-26949`.
- Pestana Personas: registrar estudiantes, catedraticos y personal con su carnet real de UVG, buscar, suspender o activar un carnet y eliminar.
- Pestana Bitacora y estadisticas: todos los intentos, total, aceptados, rechazados, porcentaje de rechazo, hora pico, grafica por hora y conteo por tipo de persona.

## Reglas de abordaje

Una persona sube solo si:

1. La lectura es un numero de carnet valido.
2. El carnet esta registrado.
3. El carnet esta activo, no suspendido.
4. Ese carnet no abordo en los ultimos 5 minutos. Si pasa, lo mas probable es un carnet prestado.

Cada regla es una clase que implementa `ReglaAbordaje`. Para agregar otra basta con crear una clase nueva y sumarla a la lista en `AbordajeService`.

## Limite conocido

El sistema comprueba el carnet, no la cara de quien lo presenta. Si alguien tiene el carnet de otra persona y esa persona no ha abordado en los ultimos 5 minutos, pasa. Mostrar la foto del titular en pantalla seria la siguiente mejora.

## Estructura, patron MVC

```
src/main/java/carnets/
  Main.java              arranque: elige MongoDB o memoria y abre la vista
  Config.java            lee MONGO_URI del sistema o del archivo .env
  DatosDePrueba.java     personas inventadas y un dia simulado para la demo
  modelo/                Persona abstracta, Estudiante, Catedratico, Personal,
                         TipoPersona, Carrera, RegistroAbordaje
  repositorio/           interfaz Repositorio, RepositorioMemoria, RepositorioMongo
  servicio/              AbordajeService, PersonaService, Estadisticas, ResultadoValidacion
  servicio/reglas/       ReglaAbordaje, CarnetActivo, SinDobleAbordaje
  controlador/           Controlador, unico punto de entrada de las vistas
  vista/                 VentanaPrincipal y sus paneles en Swing, y VistaConsola
```

Las dos vistas, grafica y consola, usan el mismo `Controlador`. Ninguna vista toca la base de datos.

## Conectar con MongoDB

1. En Atlas entra a tu cluster, Connect, Drivers, y copia la cadena que empieza con mongodb+srv.
2. Copia `.env.example`, renombralo a `.env` en la misma carpeta que `pom.xml` y pon tus datos.
3. En Atlas, Network Access, agrega tu IP actual.

Se guarda en la base `Universidad`, colecciones `personas` y `abordajes`.

Si no hay `.env` o no hay conexion, el programa sigue en modo memoria con datos de prueba, por ejemplo el carnet 24101. La barra de abajo dice siempre donde se esta guardando: verde para MongoDB, naranja para memoria.

## Correr

Con Maven, dentro de esta carpeta:

```
mvn compile exec:java
```

Opciones:

```
mvn compile exec:java -Dexec.args="--consola"     menu de texto
mvn compile exec:java -Dexec.args="--sin-datos"   memoria vacia, sin datos de prueba
```

En VS Code con el Extension Pack for Java, abre esta carpeta y ejecuta `Main`.

## Datos de prueba

| Carnet | Persona | Para probar |
|---|---|---|
| 24101 a 24106 | Estudiantes | Aceptado |
| 24107 | Valeria Ortiz | Rechazado, carnet suspendido |
| 10501, 10502 | Catedraticos | Aceptado |
| 30801, 30802 | Personal UVG | Aceptado |
| 99999 | Nadie | Rechazado, no registrado |

Validar el mismo carnet dos veces seguidas muestra el rechazo por posible carnet prestado.
