# 🌿 Naturalia - Sprint 1

Aplicación de reservas enfocada en **glamping y casas campestres**. Proyecto desarrollado como parte del Desafío Profesional con enfoque fullstack (Spring Boot + React).

---

## ✅ Historias de Usuario Completadas (Sprint 1)

| ID  | Historia                                      | Estado |
|-----|-----------------------------------------------|--------|
| #1  | Header con logo y barra de navegación         | ✅     |
| #2  | Fondo con buscador, categorías, recomendaciones | ✅     |
| #3  | Crear alojamiento + validación + Cloudinary   | ✅     |
| #4  | Listado aleatorio (máx. 10) con layout 2x5    | ✅     |
| #5  | Detalle del producto con info e imágenes      | ✅     |
| #6  | Galería con modal, diseño 1+2x2, responsivo   | ✅     |
| #7  | Footer con logo, año, responsive              | ✅     |
| #8  | Paginación funcional (Inicio, Ant, Sig)       | ✅     |
| #9  | Panel admin con bloqueo en móvil              | ✅     |
| #10 | Lista de productos con acciones CRUD          | ✅     |
| #11 | Eliminación con confirmación visual           | ✅     |

---

## 📦 Estructura del Proyecto

### Backend (Java Spring Boot)
- Base de datos: PostgreSQL en Render
- Validación: nombre duplicado con excepción personalizada
- Endpoints documentados vía Swagger (`/swagger-ui.html`)
- Tests:
  - ✅ Unitarios con JUnit + Mockito
  - ✅ Controladores con @WebMvcTest + MockMvc

### Frontend (React + Vite)
- Diseño responsivo con CSS Modules
- Uso de Cloudinary para carga de imágenes
- Rutas: `/`, `/create`, `/stays/:id`, `/administracion`
- Componente modularizado: `Header`, `Footer`, `Gallery`, `StayListSection`, `AdminPanel`, etc.

---

## 🧪 Pruebas automatizadas

- E2E: Selenium con Page Object Model
- Archivo base: `NaturaliaTests.java`
- Simula flujo completo de creación
- `PageObjectCreateStay.java` encapsula acciones

---
Documentacion tecinca swagger
http://localhost:8080/swagger-ui/index.html#/
---


## 🚀 Cómo ejecutar el proyecto

### Backend:
```bash
cd naturalia-backend
./mvnw spring-boot:run
```

### Frontend:
```bash
cd naturalia-frontend
npm install
npm run dev
```

---
logo:
https://res.cloudinary.com/dy6udvu4e/image/upload/v1748229233/Logo_pf2jgo.png
paleta:
https://www.pokemonpalette.com/leafeon
1:rgb(218, 188, 134)
2:rgb(49, 87, 68)
3:rgb(108, 188, 140)

---

## 👤 Roles (para entrega Scrum)
- TL Backend: William David Esteban Mora
- TL Frontend: William David Esteban Mora
- TL BBDD: William David Esteban Mora
- TL Infra: William David Esteban Mora

---

## 📄 Repositorios
- Backend: [https://github.com/srwesteban/naturalia-backend](https://github.com/srwesteban/naturalia-backend)
- Frontend: [https://github.com/srwesteban/naturalia-frontend](https://github.com/srwesteban/naturalia-frontend)

---

¡Sprint 1 completo con éxito! 💪
