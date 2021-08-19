# fravega_IT
Este proyecto se encarga de dar solucion a lo requerido en el Challenge 1 - Sucursal CRUD para Fravega IT. Se trata de un CRUD de Sucursal 
con  el agregado de la consulta de la sucursal mas cercana a un punto dado.  

El mismo esta desarrollado con Microserivcios 
springboot  , 2.5.3
java 11
load balancer de spring cloud 
eureka servidor de nombre
mysql 8.0
junit

para la base hay que crear es siguiente esquema : db_microservicio_sucursales por cliente o con : CREATE DATABASE `db_microservicio_sucursales`;

Microservicios

microservicios-eureka    - servidor de nombre
   * application.properties : puerto 8761 , se accede al mismo por : http://localhost:8761/

microservicios-gateway   - puerta de acceso a la app
  * application.properties : A tener en cuenta que a la app se ingresa por el puerto del load balancer puerto 8090, 
    y el path es /api/sucursales/. La url seria : localhost:8090/api/sucursales
    
microservicio-sucursales - en este se hace toda la funcionalidad
 * application.properties : se indica la configuracion de coneccion a mysql . cuando se instala el cliente MariaDB se crea el puerto :3306, usuario root , pass : sa
                        
 
Levantar en este orden

1 - eureka 
2 - sucursales
3 - gateway

Si esta todo bien configurado se crea la tabla Sucursal automaticamente.


Ahora si podemos probar la aplicacion 

Base de datos: MySql 
 se utilizo el cliente maria DB HeidiSQL
 
Entidades 
	Sucursal     : esta entidad se persiste en base 	  
	PuntoRetiro  : esta entidad no se persiste 

Con POSTMAN - 

Los datos de prueba se generan haciendo el siguiente POST :

localhost:8090/api/sucursales/crearSucursales

[
    {
        "direccion": "Av. Pres. Arturo Umberto Illia, B1613 Los Polvorines",
        "horarioAtencion": "Lunes a viernes de 6:00hs  a 18:00 hs",
        "latitud": -34.53051,
        "longitud": -58.70278
       
    },
       {
        "direccion": "Esteban Echeverria 3750, Munro",
        "horarioAtencion": "Lunes a viernes de 9:00hs  a 13:00 hs",
        "latitud": -34.51165,
        "longitud": -58.52347
       
    },
	   {
        "direccion": "Av. Corrientes 3247, C1193 CABA",
        "horarioAtencion": "Lunes a sábados de 10:30hs  a 15:00 hs",
        "latitud": -34.60085,
        "longitud": -58.40347
       
    },
	   {
        "direccion": "Av. Pres. Figueroa Alcorta 7597, C1428 CABA",
        "horarioAtencion": "Sábados de 10:00 hs a 13:00 hs",
        "latitud": -34.54399,
        "longitud": -58.45002
       
    }

]

Para traer todas las sucursales creadas con anterioridad

GET  : localhost:8090/api/sucursales


Modificamos la sucursal 1

PUT 
localhost:8090/api/sucursales/1


    {
	    	"direccion": "Av. Pres. Arturo Umberto Illia, B1613 Los Polvorines",
        "horarioAtencion": "Lunes a viernes de 6:00hs  a 18:00 hs",
        "latitud": -34.53051,
        "longitud": -58.70278
       
    }

Para traer solo la sucursal 2

GET

localhost:8090/api/sucursales/2

Si queremos eliminar una sucursal 

DELETE

localhost:8090/api/sucursales/4

GET con Paginacion

Primera pagina comienza en 0
cantidad de elementos a mostrar por pagina = 2

localhost:8090/api/sucursales/pagina?page0&size=2


Probamos la consulta de la sucursal mas cercana 

	  set de datos de prueba
    1 - Carrefour, los polvorines latitud : -34.53051 , longitud -58.70278
    2 - Abasto bs as, latitud : -34.60085 , longitud -58.40347
	  3 - Estadio Monumental River PLate , latitud : -34.54399 , longitud :  -58.45002
	 
   PuntoRetiro 
	  Jardín Japonés latitud : -34.575533 , longitud : -34.575533
	  
	 Salida : se debe asignar la sucursal abasto como la mas cercana
	 
GET 

Aca es el unico get que tenemos en el response la entidad de :

Response

PuntoRetiro
	    id	
            latitud
	    longitud
	    capacidad
	    
	    Sucursal 
	    		id
			direccion
			horarioAtencion 
			latitud
			longitud
	    
localhost:8090/api/sucursales/sucursalMasCernaca


{
        "capacidad": 12,
        "latitud": -34.575533,
        "longitud": -34.575533
       
}


** Test Unitarios **

Para probar la app se debe levantar todos los microserivios y buscar la clase : MicroservicioSucursalesApplicationTests
y hacer run de Junit.





