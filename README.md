# 🌿 Naturalia - Bitácora Técnica

Naturalia es una aplicación web de reservas orientada a alojamientos rurales, como glampings y casas campestres. El sistema permite a usuarios buscar, visualizar y reservar estancias disponibles, así como gestionar favoritos y dejar reseñas. Está desarrollada con un enfoque fullstack utilizando Java Spring Boot para el backend y React + Vite para el frontend, con una arquitectura escalable, segura y responsiva.

---

## Índice

- [Tecnologías y herramientas utilizadas](#tecnologías-y-herramientas-utilizadas)
- [Paleta de colores principal](#paleta-de-colores-principal)
- [🟢 Sprint 1](#-sprint-1)
- [🟡 Sprint 2](#-sprint-2)
- [🔵 Sprint 3](#-sprint-3)
- [🟣 Sprint 4](#-sprint-4)
- [📄 Documentación técnica](#-documentación-técnica)
- [🚀 Despliegue](#-despliegue)
- [🎨 Diseño y marca](#-diseño-y-marca)
- [Comentarios finales](#comentarios-finales)

---

## Tecnologías y herramientas utilizadas

- **Backend:** Spring Boot, Spring Security, JWT, PostgreSQL (Render), JPA/Hibernate
- **Frontend:** React, Vite, React Router, react-datepicker, react-toastify, react-icons
- **Otros:** Cloudinary (imágenes), EmailService (confirmaciones), Swagger (documentación), CSS Modules

---

## Paleta de colores principal

- `rgb(218, 188, 134)` – arena cálida
- `rgb(49, 87, 68)` – verde profundo
- `rgb(108, 188, 140)` – verde natural

---

# 🟢 Sprint 1

### Definición del proyecto
Inicio del desarrollo con enfoque en estructura, visualización de productos y administración básica.

### Historias completadas
- Header fijo con logo y navegación.
- Home con buscador, categorías y recomendaciones.
- Registro de productos desde el panel admin.
- Visualización aleatoria (máx. 10) con galería.
- Detalle del producto con layout 1+2x2.
- Footer responsive y paginación funcional.
- Panel de administración (bloqueado en móvil).
- Lista de productos con acciones CRUD.

### Planificación y ejecución de las pruebas - Etapa 1: Preparación del entorno y pruebas básicas
- Configuración inicial de `@SpringBootTest` y `@AutoConfigureMockMvc` para habilitar tests integrados con MockMvc, simulando peticiones HTTP a controladores REST.
- Implementación de pruebas unitarias para controladores sencillos con Mockito y MockMvc para verificar respuestas HTTP.
- Verificación de rutas básicas y validaciones en controladores de autenticación y categorías.

**Aprendizajes:**
Se comprobó la necesidad de tokens JWT para la mayoría de endpoints protegidos, validación de datos de entrada y manejo de errores HTTP 4xx y 5xx.

---

# 🟡 Sprint 2

### Definición del proyecto
Implementación de autenticación, gestión de usuarios y estructura de datos dinámica.

### Historias completadas
- Registro/login con JWT, validación, avatar.
- Panel admin: asignar/quitar roles.
- Categorías: crear, asignar, filtrar productos.
- Características: CRUD desde el panel.
- Vista de características en detalle de producto.
- Filtrado múltiple por categorías en home.

### Planificación y ejecución de las pruebas - Etapa 2: Testing con seguridad y autenticación JWT
- Creación de tests que primero registran usuarios, luego hacen login para obtener token JWT.
- Uso del token para autorizar peticiones a endpoints protegidos en tests de integración (ejemplo: crear categorías, gestionar favoritos, realizar reservas).
- Validación de errores comunes como HTTP 403 (Forbidden) al no enviar token.
- Automatización del flujo: registrar usuario → login → obtener token → usar token en cabecera Authorization.

**Herramientas:**
- MockMvc para simular HTTP requests.
- ObjectMapper para serializar/deserializar JSON.
- Uso explícito de header `"Authorization: Bearer <token>"` en tests.

---

# 🔵 Sprint 3

### Definición del proyecto
Mejorar experiencia del usuario: búsqueda, favoritos, políticas y reviews.

### Historias completadas
- Búsqueda por nombre y fechas con sugerencias.
- Visualización de disponibilidad en detalle.
- Favoritos persistentes y listables por usuario.
- Políticas editables por anfitrión.
- Reviews visibles solo para usuarios con reservas.
- Media de puntuación en tiempo real.

### Planificación y ejecución de las pruebas - Etapa 3: Pruebas unitarias de servicios con Mockito
- Mockeo de repositorios JPA para aislar la lógica de negocio en servicios (`ServiceImpl`).
- Validación de comportamientos: guardar, actualizar, eliminar entidades, y manejo de excepciones personalizadas (`ResourceNotFoundException`, `DuplicateNameException`).
- Tests para servicios:
    - `AuthServiceImpl` → registro y obtención del usuario autenticado.
    - `CategoryServiceImpl` → creación, listado y eliminación con validación de relaciones.
    - `FavoriteServiceImpl` → agregar, eliminar, listar favoritos, comprobación de existencia.
    - `FeatureServiceImpl` → CRUD completo para características.
    - `ReservationServiceImpl` → creación de reservas con validación de conflictos, cálculo de precio total, envío de emails.
    - `StayServiceImpl` → creación, actualización, listado, filtrado y conversión DTO, incluyendo manejo de relaciones y autenticación.
    - `UserServiceImpl` → gestión y cambio de roles, listado por rol.

**Estrategias:**
- Uso de `@Mock` y `@InjectMocks` para aislar servicios.
- Mockito para configurar respuestas (`when(...).thenReturn(...)`).
- Asserts específicos para validar resultados y excepciones.
- Verificación de llamadas a métodos con `verify(...)`.

---

# 🟣 Sprint 4

### Definición del proyecto
Flujo completo de reservas, historial, confirmaciones e integración con WhatsApp.

### Historias completadas
- Selección de fechas con validación.
- Interfaz de reserva con resumen y feedback.
- Registro de reservas en backend.
- Historial con diferenciación de pasadas/futuras.
- Botón fijo de WhatsApp.
- Envío automático de confirmación por correo.

### Planificación y ejecución de las pruebas - Etapa 4: Tests específicos y casos límite
- Manejo de errores en pruebas: verificar que se lanzan excepciones adecuadas ante datos inválidos o estados imposibles (ej. fechas de reserva incoherentes, entidades no encontradas, nombres duplicados).
- Tests para endpoints que dependen de la autenticación: verificar rechazo sin token, aceptación con token válido.
- Pruebas para cálculos lógicos, por ejemplo:
    - Número de noches y precio total en reservas.
    - Promedios de puntuación en estancias.
- Asegurar que las relaciones entre entidades (features, categorías, host) se gestionan correctamente y se reflejan en los DTOs.

---

# 📄 Documentación técnica

- Swagger UI disponible en: [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

# 🚀 Despliegue

- El frontend está desplegado en **Vercel** y es accesible en:
  [https://naturalia-frontend.vercel.app/](https://naturalia-frontend.vercel.app/)

- El backend y la base de datos PostgreSQL están desplegados en **Render**, utilizando una imagen Docker para facilitar la administración y escalabilidad.

---

# 🎨 Diseño y marca

- Logo oficial:
  ![Logo Naturalia](https://res.cloudinary.com/dy6udvu4e/image/upload/v1748229233/Logo_pf2jgo.png)

- Paleta de colores:
  [https://www.pokemonpalette.com/leafeon](https://www.pokemonpalette.com/leafeon)

---

# Comentarios finales

Este proyecto ha sido desarrollado bajo un enfoque ágil con sprints claramente definidos que han permitido iterar funcionalidades y pruebas de manera organizada y eficiente, asegurando calidad en el código y experiencia de usuario. La integración completa de autenticación JWT, pruebas con MockMvc y Mockito, y la atención a casos borde en tests confirman un proceso profesional orientado a la escalabilidad y mantenimiento.

---

