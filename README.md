# AppChat

**AppChat** es una aplicación de mensajería de escritorio desarrollada en Java con Swing, inspirada en servicios como WhatsApp Web. Permite a los usuarios registrarse, gestionar contactos individuales o grupales, enviar mensajes con emoticonos, suscribirse a una cuenta premium con descuentos y exportar conversaciones a PDF.

El proyecto sigue una arquitectura en capas basada en MVC, utilizando patrones como DAO, Singleton y Adaptador para la gestión de la persistencia y la lógica de negocio.

## 🧩 Estructura del Proyecto

- **Dominio (`umu.tds.appchat.dominio`)**: Define las entidades del sistema como `Usuario`, `Mensaje`, `Grupo`, `ContactoIndividual` y `Contacto`.
- **Controlador (`umu.tds.appchat.controlador`)**: Orquesta la lógica de la aplicación mediante clases como `AppChat` y `CargarAppChat`.
- **Persistencia (`umu.tds.appchat.persistencia`)**: Implementa los adaptadores de acceso a datos (`AdaptadorUsuario`, `AdaptadorMensaje`, `AdaptadorGrupo`, `AdaptadorDescuento`) usando el patrón DAO.
- **Vista (`umu.tds.appchat.vista`)**: Conjunto de interfaces gráficas implementadas en Swing para interacción con el usuario.

## 📦 Funcionalidades

### ✔️ Usuarios
- Registro con nombre, fecha de nacimiento, email, imagen, número de teléfono y contraseña.
- Login mediante número de teléfono y contraseña.
- Gestión de perfil, incluyendo saludo e imagen.
- Suscripción a cuenta Premium con sistema flexible de descuentos:
  - Por antigüedad en el registro.
  - Por cantidad de mensajes enviados.
- Exportación de mensajes a PDF (solo usuarios Premium).

### ✔️ Contactos
- Añadir contactos mediante número de teléfono.
- Contactos individuales o grupos de usuarios (no anidados).
- Gestión de grupos: crear, editar miembros, asignar imagen.

### ✔️ Mensajes
- Envío de mensajes individuales o a grupos.
- Mensajes con texto, emoticonos, fecha y hora.
- Visualización contextual en base al contacto o grupo.
- Añadir contacto directamente desde el chat si no existe.

### ✔️ Vista
- Interfaces diseñadas en Swing: login, registro, chat principal, gestión de contactos, perfil, premium, exportación PDF, etc.

### ✔️ Otros
- Uso de librerías externas como `JCalendar` (via Maven).
- Imágenes cargadas desde URLs para simplicidad.
- Preparado para extender nuevas políticas de descuento.