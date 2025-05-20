# API Kotlin AFIP-WS

Client para el acceso a los web services de ARCA (ex AFIP).
- `WSAA` Webservice de Autenticación y Autorización
- `WSFE` Webservice de Facturación Electrónica v1
- `WSFEX` Webservice de Facturación Electrónica de exportación v1

---

## Uso

### Requisitos

- Kotlin JVM 2.1.20
- Java 21
- Certificado y PK válidos de AFIP (producción/homologación) y los webservice habilitados

Crear en la raíz un archivo con el nombre **config.properties** (o ejecutar por primera vez y se creará automáticamente) con el siguiente contenido:

```
CERT=nombre_del_cert.crt
KEY=nombre_pk.key
URL=https://wsaahomo.afip.gov.ar/ws/services/LoginCms
```

---


## Librerías utilizadas

- [OkHttp](https://square.github.io/okhttp/)
- [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime)
- [SimpleXML](https://github.com/ngallagher/simplexml)
- [Bouncy Castle](https://www.bouncycastle.org/)
