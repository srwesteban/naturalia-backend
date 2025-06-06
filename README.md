🌿 Naturalia - Sprint 1 & 2

Aplicación de reservas enfocada en glamping y casas campestres. Proyecto desarrollado como parte del Desafío Profesional con enfoque fullstack (Spring Boot + React).

✅ Historias de Usuario Completadas (Sprint 1)

ID

Historia

Estado

#1

Header con logo y barra de navegación

✅

#2

Fondo con buscador, categorías, recomendaciones

✅

#3

Crear alojamiento + validación + Cloudinary

✅

#4

Listado aleatorio (máx. 10) con layout 2x5

✅

#5

Detalle del producto con info e imágenes

✅

#6

Galería con modal, diseño 1+2x2, responsivo

✅

#7

Footer con logo, año, responsive

✅

#8

Paginación funcional (Inicio, Ant, Sig)

✅

#9

Panel admin con bloqueo en móvil

✅

#10

Lista de productos con acciones CRUD

✅

#11

Eliminación con confirmación visual

✅

✅ Historias de Usuario Completadas (Sprint 2)

ID

Historia

Estado

#12

Categorizar productos

✅

#13

Registrar usuario

✅

#14

Identificar usuario (Login)

✅

#15

Cerrar sesión

✅

#16

Identificar administrador

✅

#17

Administrar características de producto

✅

#18

Visualizar características en detalle

✅

#19

Confirmación por correo (opcional)

❌

#20

Filtro por categorías

✅

#21

Crear nueva categoría

✅

📦 Estructura del Proyecto

Backend (Java Spring Boot)

Base de datos: PostgreSQL en Render

Seguridad: Spring Security + JWT

Validaciones con DTOs

Documentación Swagger (/swagger-ui.html)

Tests:

✅ Unitarios con JUnit + Mockito

✅ Controladores con @WebMvcTest + MockMvc

Frontend (React + Vite)

Diseño responsivo con CSS Modules

Uso de Cloudinary para carga de imágenes

Rutas: /, /create, /stays/:id, /administracion

Componentes: Header, Footer, Gallery, StayListSection, AdminPanel, etc.

Login y registro con JWT, avatar de iniciales, filtros dinámicos

🧪 Pruebas automatizadas

Casos de prueba (Sprint 2)

UserControllerTest

GET /users → Lista de usuarios

GET /users/hosts → Lista de hosts

PUT /users/{id}/role → Actualiza rol

FeatureControllerTest

GET /features → Lista de características

POST /features → Crear característica

PUT /features/{id} → Editar característica

DELETE /features/{id} → Eliminar característica

CategoryControllerTest

GET /categories → Lista de categorías

POST /categories → Crear categoría

DELETE /categories/{id} → Eliminar categoría

StayControllerTest

GET /stays → Lista de estancias

GET /stays/{id} → Estancia por ID

POST /stays → Crear estancia

PUT /stays/{id} → Editar estancia

DELETE /stays/{id} → Eliminar estancia

Cobertura

Todos los controladores cubiertos

Pruebas de estado HTTP, respuestas y servicios mockeados

📄 Documentación técnica

Swagger UI: http://localhost:8080/swagger-ui/index.html#/

Bitácora técnica, tabla de endpoints y pruebas unitarias en /docs

🚀 Cómo ejecutar el proyecto

Backend:

cd naturalia-backend
./mvnw spring-boot:run

Frontend:

cd naturalia-frontend
npm install
npm run dev

🎨 Diseño y marca

Logo: https://res.cloudinary.com/dy6udvu4e/image/upload/v1748229233/Logo_pf2jgo.png

Paleta: https://www.pokemonpalette.com/leafeon

1: rgb(218, 188, 134)

2: rgb(49, 87, 68)

3: rgb(108, 188, 140)

👤 Roles del equipo

TL Backend: William David Esteban Mora

TL Frontend: William David Esteban Mora

TL BBDD: William David Esteban Mora

TL Infra: William David Esteban Mora

📄 Repositorios

Backend: https://github.com/srwesteban/naturalia-backend

Frontend: https://github.com/srwesteban/naturalia-frontend

✅ Sprint 2 finalizado con éxito. Proyecto listo para entrega.



# 🌿 Naturalia - Sprint 3

> Última actualización: 2025-06-05

---

## 📘 Bitácora de Desarrollo

**Sprint 3** tuvo como foco principal mejorar la experiencia del usuario mediante nuevas funcionalidades clave como búsqueda avanzada, favoritos, políticas por producto, y administración de categorías.

**Definición del proyecto:**  
Plataforma web de reservas de glampings y casas campestres. Los usuarios pueden buscar alojamientos por fechas o nombre, ver detalles del producto con disponibilidad, guardar favoritos, y realizar reservas. Los administradores pueden gestionar productos, categorías y políticas.

---

## ✅ Historias de Usuario Completadas

| ID   | Historia                        | Estado |
|------|----------------------------------|--------|
| #22  | Realizar búsqueda                | ✅     |
| #23  | Visualizar disponibilidad        | ✅     |
| #24  | Marcar como favorito             | ✅     |
| #25  | Listar productos favoritos       | ✅     |
| #26  | Ver bloque de políticas          | ✅     |
| #27  | Compartir productos              | ✅     |
| #29  | Eliminar categoría               | ✅     |

---

## 🧪 Planificación y Ejecución de Pruebas

### #22 Realizar búsqueda

| Caso de prueba                             | Resultado |
|-------------------------------------------|-----------|
| Buscar sin texto ni fechas → mostrar todo | ✅         |
| Buscar por nombre parcial → sugerencias   | ✅         |
| Click en sugerencia → realiza búsqueda    | ✅         |
| Campos requeridos vacíos → ignora búsqueda| ✅         |
| Feedback al buscar (loading visual)       | ✅         |

### #23 Visualizar disponibilidad

| Caso de prueba                                       | Resultado |
|-----------------------------------------------------|-----------|
| Fechas reservadas aparecen en otro color            | ✅         |
| Usuario no autenticado puede ver disponibilidad     | ✅         |
| Errores al cargar → mensaje visible + retry         | ✅         |

### #24 Marcar como favorito

| Caso de prueba                          | Resultado |
|----------------------------------------|-----------|
| Icono visible en cards de productos    | ✅         |
| Click → añade a favoritos              | ✅         |
| Usuario no logueado → no se permite    | ✅         |
| Estado persiste al refrescar           | ✅         |

### #25 Listar productos favoritos

| Caso de prueba                                      | Resultado |
|----------------------------------------------------|-----------|
| Acceso desde menú del usuario                      | ✅         |
| Eliminar favorito desde la lista                   | ✅         |
| Lista vacía → mensaje "sin favoritos"              | ✅         |
| Diseño responsive correcto                         | ✅         |

### #26 Ver bloque de políticas

| Caso de prueba                                  | Resultado |
|------------------------------------------------|-----------|
| Título visible, subrayado                      | ✅         |
| Políticas divididas en 3 columnas              | ✅         |
| Información real cargada del backend           | ✅         |

### #27 Compartir productos

| Caso de prueba                           | Resultado |
|-----------------------------------------|-----------|
| Botón visible en detalle de producto    | ✅         |
| Al hacer click → se abre modal redes    | ✅         |
| Copia de link disponible                | ✅         |
| (Integración real futura - placeholder) | ⚠️ Simulado |

### #29 Eliminar categoría

| Caso de prueba                                                | Resultado |
|--------------------------------------------------------------|-----------|
| Botón eliminar visible en panel                              | ✅         |
| Modal de confirmación con advertencia                        | ✅         |
| No se puede eliminar si está en uso → muestra mensaje claro  | ✅         |

---

## 🧾 Conclusión del Sprint

Todas las funcionalidades planificadas en el Sprint 3 se desarrollaron y probaron satisfactoriamente. Se mejoró significativamente la usabilidad con interacciones más intuitivas y se fortaleció la experiencia del usuario.