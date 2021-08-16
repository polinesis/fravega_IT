package com.example.microservicios.app.sucursales.controllers;


import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.commons.controllers.CommonController;
import com.example.commons.sucursales.models.entity.Sucursal;
import com.example.microservicios.app.sucursales.model.PuntoRetiro;
import com.example.microservicios.app.sucursales.services.SucursalService;


@RestController
public class SucursalController extends CommonController<Sucursal, SucursalService>{


	@PostMapping("/crearSucursales")
	public ResponseEntity<?> crear( @RequestBody Iterable<Sucursal> sucursales){
	
		Iterable<Sucursal> respuestasDb = service.saveAll(sucursales);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(respuestasDb);
		
	}

	
	@GetMapping("/sucursalMasCernaca")
	public ResponseEntity<?> obtenerSucursalMasCernaca(@RequestBody PuntoRetiro puntoRetiro){
		
	
		Map<Long,Double> mapDistanciaSucu = new HashMap<>();
		List<Sucursal> sucursales = (List<Sucursal>) service.findAll();
		Sucursal sucursalMasCercana = new Sucursal();
		PuntoRetiro puntoRetiroResultado = new PuntoRetiro(); 
		
		
		//validamos datos de entrada 
		
		if(puntoRetiro.getLatitud() == 0 ) {
			Map<String, Object> errores = new HashMap<>();
			errores.put("Latitud", " El campo : Latitud no puede estar vacío y debe ser mayor que cero");
			return ResponseEntity.badRequest().body(errores);
		}
		
		if(puntoRetiro.getLongitud() == 0 ) {
			Map<String, Object> errores = new HashMap<>();
			errores.put("Longitud", " El campo : Longitud no puede estar vacío y debe ser mayor que cero");
			return ResponseEntity.badRequest().body(errores);
		}
		
		
		if(sucursales.isEmpty()) {
			return ResponseEntity.notFound().build();
		}else {
			
			
			// cargamos todas las distancias y sucursales
			for (Sucursal sucursal : sucursales) {
				mapDistanciaSucu.put(sucursal.getId(), service.obtenerSucursalMasCernaca(puntoRetiro, sucursal));
			}
			
		    // ordenamos las distancias de menor a mayor 
			 Map<Long,Double> sortedByValue = mapDistanciaSucu.entrySet()
			 .stream()
			 .sorted(Map.Entry.<Long, Double>comparingByValue())
			 .collect(Collectors.toMap(Map.Entry::getKey, 
					  					Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

			 // aca obtenemos la primer distancia	
			 Map.Entry<Long,Double> mapsucursalMasCercana = sortedByValue.entrySet()
			 .stream()
			 .findFirst()
			 .get();
			 
			 //buscamos la sucursal mas cercana para el punto de referencia si no la econtramos informamos lo sucedido
			 Optional<Sucursal> o = service.findById(mapsucursalMasCercana.getKey());
			 if(!o.isPresent()) {
					return ResponseEntity.notFound().build();
			 }
			 
			 Sucursal sucursal =  o.get();
			 
			 sucursalMasCercana.setId(sucursal.getId());
			 sucursalMasCercana.setDireccion(sucursal.getDireccion());
			 sucursalMasCercana.setHorarioAtencion(sucursal.getHorarioAtencion());
			 sucursalMasCercana.setLatitud(sucursal.getLatitud());
			 sucursalMasCercana.setLongitud(sucursal.getLongitud());
			 
			 // devolvemos el resultado de la sucursal mas cercana de acuerdo al punto de referencia
			 int numeroAleatorio = (int) (Math.random()*25+1);
			 Long idPuntoRetiro = (long) numeroAleatorio;
			 puntoRetiroResultado.setId(idPuntoRetiro);
			 puntoRetiroResultado.setCapacidad(puntoRetiro.getCapacidad());
			 puntoRetiroResultado.setLatitud(puntoRetiro.getLatitud());
			 puntoRetiroResultado.setLongitud(puntoRetiro.getLongitud());
			 puntoRetiroResultado.setSucursal(sucursalMasCercana);
		
		}
		
		
		return ResponseEntity.ok(puntoRetiroResultado);
		
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Sucursal sucursal, BindingResult result, @PathVariable Long id) {
	

		if(result.hasErrors()) {
			return this.validar(result);
		}
		
		Optional<Sucursal> o = service.findById(id);
		if (o.isPresent()) {
			return ResponseEntity.notFound().build();
		}	
		
		Sucursal sucursaldb = o.get();
		
		sucursaldb.setDireccion(sucursal.getDireccion());
		sucursaldb.setHorarioAtencion(sucursal.getHorarioAtencion());
		sucursaldb.setLatitud(sucursal.getLatitud());
		sucursaldb.setLongitud(sucursal.getLongitud());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(sucursaldb));
		
	}

	public SucursalService getService() {
		 
		return service;
	}
}
