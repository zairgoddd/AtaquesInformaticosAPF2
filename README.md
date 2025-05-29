# Ataques Informáticos APF2

Aplicación web desarrollada para la gestión y análisis de ataques informáticos, implementada con tecnologías Java y bases de datos MySQL.

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java
- **Frontend**: HTML, CSS, JavaScript
- **Base de Datos**: MySQL Workbench
- **Framework**: Spring Boot (si aplica)

## 📋 Configuración de Base de Datos

### Datos de Conexión
- **Nombre de la BD**: `ataques_informaticos`
- **Usuario**: `root`
- **Contraseña**: `root`

### Configuración Personalizada
Si necesitas usar credenciales diferentes, modifica el archivo `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ataques_informaticos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## 👥 Usuarios del Sistema

### Credenciales de Acceso

| Tipo de Usuario | Usuario | Contraseña | Rol/Permisos |
|----------------|---------|------------|--------------|
| Administrador  | admin   | admin123   | Acceso completo |
| Analista       | analista | analista12345 | Análisis limitado |
| Cliente        | benjaminestebann31@gmail.com | benjamin12345 | Acceso de solo lectura |

*Nota: Estas son credenciales de desarrollo/prueba. En producción se deben cambiar por credenciales seguras.*

## 🚀 Instalación y Ejecución

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/zairgoddd/AtaquesInformaticosAPF2.git
   ```

2. **Configurar MySQL Workbench**
   - Crear la base de datos `ataques_informaticos`
   - Importar el esquema/datos si existe un archivo SQL

3. **Configurar credenciales**
   - Verificar `application.properties`
   - Ajustar usuario y contraseña si es necesario

4. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

## 🔒 Seguridad

- Las contraseñas mostradas son solo para desarrollo
- En producción, implementar hash de contraseñas
- Configurar variables de entorno para credenciales sensibles

## 👨‍💻 Desarrolladores

- Castillo Escobar Cristopher 
- Olivos Saavedra Genaro
- Peña Morán Benjamín

## 📝 Notas Adicionales

- Asegúrate de tener MySQL Workbench ejecutándose antes de iniciar la aplicación
- El puerto por defecto de MySQL es 3306
- Para cualquier problema de conexión, verificar las credenciales en `application.properties`
