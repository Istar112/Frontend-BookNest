#  BookNest - El nido de de tus lecturas.


**BookNest** es una aplicación multiplataforma moderna desarrollada con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**. Está diseñada para ofrecer a los lectores una herramienta elegante y eficiente para gestionar su biblioteca personal, permitiendo un seguimiento exhaustivo del progreso de lectura y el descubrimiento de nuevos títulos.

---

##  Características Principales

###  Exploración y Descubrimiento
- **Biblioteca Central:** Explora una amplia colección de libros con portadas reales y metadatos completos.
- **Detalle Profundo:** Consulta ISBN, categorías, número de páginas y fechas de publicación de cada obra.

### Sistema de Seguimiento (Tracking)
- **Gestión de Lectura:** Añade libros a tu seguimiento, marca como leídos y califica tus favoritos.
- **Estados Dinámicos:** Organiza tu biblioteca en secciones:
    -  **En Proceso:** Libros que estás disfrutando actualmente.
    -  **Finalizados:** Tu historial de éxitos literarios.
- **Edición en Tiempo Real:** Actualiza el progreso de páginas y fechas directamente desde la interfaz.

### Gestión de Usuario
- **Autenticación Completa:** Registro y Login seguro de usuarios.
- **Perfil Personalizado:** Visualiza y edita tus datos personales.
- **Seguridad:** Implementación de sistema de tokens (Access & Refresh Token).

### Experiencia de Usuario (UI/UX)
- **Selectores Táctiles:** Selectores estilo rueda para una entrada de datos fluida en móviles.
- **Valoración Interactiva:** Sistema de 5 estrellas para puntuar tus lecturas terminadas.
- **Diseño Minimalista:** Navegación intuitiva mediante menús laterales y organización modular.

---

## Stack Tecnológico

### Core & Arquitectura
*   **Kotlin Multiplatform (KMP):** Lógica de negocio compartida al 100% entre plataformas.
*   **Compose Multiplatform:** UI declarativa única para Android, Desktop y Web.
*   **Clean Architecture:** Implementación estricta de capas para asegurar la mantenibilidad.

### Librerías & Frameworks
*   **Koin:** Inyección de dependencias ligera y eficiente.
*   **Ktor:** Cliente HTTP para la comunicación con la API (FastAPI + MariaDB).
*   **Coil 3:** Gestión optimizada de carga y caché de imágenes.
*   **KotlinX Serialization:** Serialización de datos JSON.
*   **KotlinX Datetime:** Manejo de fechas y tiempos multiplataforma.
*   **Navigation Compose:** Flujo de navegación robusto entre pantallas.

---

## Estructura del Proyecto

El proyecto sigue los principios de **Clean Architecture**, dividiendo la lógica en capas dentro del módulo `composeApp/commonMain`:

```text
es.dam.booknest
├── aplication      # Casos de uso (Use Cases) de la aplicación
├── di              # Configuración de Inyección de Dependencias (Koin)
├── infraestructure # Implementación de Repositorios y Servicios API (Ktor)
├── model           # Entidades de dominio e interfaces de repositorios
└── ui              # Componentes de UI, Screens y ViewModels (Compose)
```

---

## Cómo Empezar

### Requisitos Previos
- Android Studio Iguana o superior / IntelliJ IDEA.
- JDK 17 o superior.
- Backend de BookNest en ejecución (FastAPI).

### Instalación
1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/Frontend-BookNest.git
   ```
2. Abre el proyecto en Android Studio.
3. Sincroniza Gradle para descargar las dependencias.

### Ejecución
- **Android:** Selecciona la configuración `composeApp` y el dispositivo/emulador.
- **Desktop:** Ejecuta la tarea de gradle: `./gradlew :composeApp:run`

---

