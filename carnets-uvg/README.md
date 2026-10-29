# Carnets UVG

Sistema de carnets con menu completo en Main. Guarda en MongoDB Atlas y, si no hay conexion, sigue en memoria.

## Estructura

```
src/main/java/carnets/
  Main.java                  menu y conversacion con la persona
  Config.java                lee MONGO_URI de la variable de sistema o del archivo .env
  modelo/                    Carrera y EstudianteData
  servicio/                  EstudianteService, validaciones y carnet
  repositorio/               EstudianteRepository, RepositorioMemoria y RepositorioMongo
```

## Conectar con MongoDB

1. En Atlas entra a tu cluster, pulsa Connect, luego Drivers, y copia la cadena que empieza con mongodb+srv.
2. Copia el archivo .env.example y renombralo a .env, en la misma carpeta que pom.xml.
3. Dentro de .env cambia USUARIO, PASSWORD y TU-CLUSTER por tus datos reales.
4. Si la contrasena tiene simbolos como @ o #, hay que codificarlos, por ejemplo @ pasa a %40.
5. En Atlas, en Network Access, agrega tu IP actual.

El archivo .env esta en .gitignore, no se sube a GitHub.

## Correr

Abre la carpeta carnets-uvg como proyecto Maven y ejecuta Main.

Desde la terminal, dentro de la carpeta:

```
mvn compile exec:java
```

Al arrancar, el programa dice si quedo Conectado o si sigue en modo memoria. Arriba del menu siempre muestra donde esta guardando.

## Ver los datos

En Atlas, en Browse Collections, base Universidad, coleccion estudiantes.
