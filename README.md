# Patrones de diseño

El proyecto usa el patrón **Factory Method** en el arquetipo, en los microservicios y en el front Angular. La creación de cada objeto sigue el mismo algoritmo y cada fábrica concreta decide qué instancia producir y cómo completarla.

## Factory Method

`crear` queda fijo. Las subclases solo implementan dos pasos:

1. `nuevaInstancia`: crea el objeto vacío.
2. `completar`: copia y normaliza los datos de entrada.

Quien llama a `crear` no usa `new` ni reparte los campos a mano.

## Arquetipo

La plantilla está en `arquetipo/microservicio-base` y viaja dentro de `arquetipo/microservicio-arquetipo`.

| Clase               | Rol                                                                                    |
|---------------------|----------------------------------------------------------------------------------------|
| `Fabrica<T, R>`     | Contrato: `T crear(R datos)`.                                                          |
| `FabricaBase<T, R>` | Implementa `crear` y lo deja `final`. Obliga a definir `nuevaInstancia` y `completar`. |

Al generar un microservicio, esas clases quedan en el paquete del proyecto nuevo. La fábrica concreta se agrega después, con la entidad de ese servicio.

## Microservicios

Cada servicio extiende `FabricaBase` y el servicio de negocio solo pide el objeto ya armado.

| Microservicio   | Fábrica             | Qué produce                                                                                                                                                                   |
|-----------------|---------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `usuarios`      | `UsuarioFabrica`    | Un `Usuario` a partir de `UsuarioRequest`: recorta el nombre, normaliza el correo y guarda la clave cifrada. `actualizar` reutiliza la misma fábrica cuando cambia el perfil. |
| `publicaciones` | `CategoriaFabrica`  | Una `Categoria` con nombre y descripción. También carga las cinco categorías iniciales.                                                                                       |
| `publicaciones` | `TemaFabrica`       | Un `Tema` publicado, con fecha, categoría y autor.                                                                                                                            |
| `publicaciones` | `ComentarioFabrica` | Un `Comentario` publicado, asociado a un tema y a un autor.                                                                                                                   |
| `moderacion`    | `SancionFabrica`    | Una `DecisionModeracion` con estado `BANEADO`. `aplicar` copia esa decisión sobre el tema o el comentario.                                                                    |

## Front Angular

Nexo repite el mismo patrón en TypeScript, en `foro-web/src/app/fabrica`.

| Clase               | Qué produce                                             |
|---------------------|---------------------------------------------------------|
| `Fabrica<T, R>`     | El algoritmo `crear`.                                   |
| `UsuarioFabrica`    | Un usuario con rol `USUARIO` y el correo en minúsculas. |
| `TemaFabrica`       | Un tema con estado `PUBLICADO` y la fecha de creación.  |
| `ComentarioFabrica` | Un comentario publicado, ligado al tema y al autor.     |

`ForoDatos` guarda esas instancias en listas. El front no llama a los microservicios: las pantallas leen y modifican esas colecciones. El administrador, al banear, cambia el estado a `BANEADO` y el listado público deja de mostrar ese registro.
