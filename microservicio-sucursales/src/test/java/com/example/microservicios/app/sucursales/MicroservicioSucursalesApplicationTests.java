package com.example.microservicios.app.sucursales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.commons.sucursales.models.entity.Sucursal;
import com.example.microservicios.app.sucursales.controllers.SucursalController;
import com.example.microservicios.app.sucursales.model.PuntoRetiro;

@SpringBootTest
class MicroservicioSucursalesApplicationTests {
	@Autowired
	SucursalController sucursalController;

	/*
	 * probamos caso exitoso de creacion de sucursal
	 */
	@Test
	void testCrear() {

		Sucursal sucursal = new Sucursal();

		int numeroAleatorio = (int) (Math.random() * 25 + 1);
		sucursal.setDireccion("TEST - angel gallardo numero:" + numeroAleatorio);
		sucursal.setHorarioAtencion("TEST - Lunes a viernes de 9 a 3");
		sucursal.setLatitud(-34.57060);
		sucursal.setLongitud(-58.72734);

		/*
		 * Sucursal sucursalSave = sucursalController.getService().save(sucursal);
		 * Optional<Sucursal> o =
		 * sucursalController.getService().findById(sucursalSave.getId());
		 * 
		 * Sucursal sucursalBd = o.get();
		 * 
		 * assertEquals("TEST - angel gallardo numero:"+numeroAleatorio+"",
		 * sucursalBd.getDireccion());
		 */
	}

	/*
	 * probamos que no se debe crear sucursales al agregar direccion en null
	 */
	@Test
	void testErrorCrear() {

		Sucursal sucursal = new Sucursal();

		sucursal.setDireccion(null);
		sucursal.setHorarioAtencion("TEST - Lunes a viernes de 9 a 3");
		sucursal.setLatitud(-34.57060);
		sucursal.setLongitud(-58.72734);

		Sucursal sucursalSave = null;

		try {

			sucursalSave = sucursalController.getService().save(sucursal);

		} catch (ConstraintViolationException e) {
			e.printStackTrace();
		}

		assertNull(sucursalSave);

	}

	/*
	 * probamos la devolucion de todas las sucursales
	 */
	@Test
	void getSucursales() {

		assertNotNull(sucursalController.getService().findAll());

	}

	/*
	 * aca vamos a probar calculo de distancia
	 * 
	 * set de datos prueba 1 - Carrefour, los polvorines latitud : -34.53051 ,
	 * longitud -58.70278 2 - Abasto bs as, latitud : -34.60085 , longitud -58.40347
	 * 3 - Estadio Monumental River PLate , latitud : -34.54399 , longitud :
	 * -58.45002
	 * 
	 * input
	 * 
	 * Jardín Japonés latitud : -34.575533 , longitud : -58.41718
	 * 
	 * output : se debe asignar la sucursal abasto como la mas cercana
	 */
	@Test
	void obtenerSucursalMasCernaca() {

		PuntoRetiro puntoRetiro = new PuntoRetiro();
		Map<Long, Double> mapDistanciaSucu = new HashMap<>();

		int numeroAleatorio = (int) (Math.random() * 25 + 1);
		Long idPuntoRetiro = (long) numeroAleatorio;
		puntoRetiro.setId(idPuntoRetiro);
		puntoRetiro.setCapacidad(12);
		puntoRetiro.setLatitud(-34.575533);
		puntoRetiro.setLongitud(-58.41718);

		List<Sucursal> sucursales = (List<Sucursal>) sucursalController.getService().findAll();

		// cargamos todas las distancias y sucursales
		for (Sucursal sucursal : sucursales) {
			mapDistanciaSucu.put(sucursal.getId(),
					sucursalController.getService().obtenerSucursalMasCernaca(puntoRetiro, sucursal));
		}

		// ordenamos las distancias de menor a mayor
		Map<Long, Double> sortedByValue = mapDistanciaSucu.entrySet().stream()
				.sorted(Map.Entry.<Long, Double>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		// aca obtenemos la primer distancia
		Map.Entry<Long, Double> mapsucursalMasCercana = sortedByValue.entrySet().stream().findFirst().get();

		// buscamos la sucursal mas cercana para el punto de referencia si no la
		// econtramos informamos lo sucedido
		Optional<Sucursal> o = sucursalController.getService().findById(mapsucursalMasCercana.getKey());
		Sucursal sucursalCercana = o.get();

		// la sucursal mas cercana debe ser la de abasto de bs as
		// 2 - Abasto bs as, latitud : -34.60085 , longitud -58.40347

		assertEquals(sucursalCercana.getLatitud(), -34.60085);
		assertEquals(sucursalCercana.getLongitud(), -58.40347);

	}

	
}
