package com.example.microservicios.app.sucursales.services;

import org.springframework.stereotype.Service;

import com.example.commons.services.CommonServiceImpl;
import com.example.commons.sucursales.models.entity.Sucursal;
import com.example.microservicios.app.sucursales.model.PuntoRetiro;
import com.example.microservicios.app.sucursales.services.repository.SucursalRepository;

@Service
public class SucursalServiceImpl  extends CommonServiceImpl<Sucursal, SucursalRepository> implements SucursalService {

	@Override
	public double obtenerSucursalMasCernaca(PuntoRetiro puntoRetiro , Sucursal sucursal) {
	   
		//Podemos utilizar el algoritmo Haversine
        //esta funcion nos sirve para calcular la distancia entre dos puntos:

        final double RADIO_TIERRA = 6371000; // en metros

        double dLat = Math.toRadians(sucursal.getLatitud() - puntoRetiro.getLatitud());

        double dLon = Math.toRadians(sucursal.getLongitud() - puntoRetiro.getLongitud());

        double lat1 = Math.toRadians(puntoRetiro.getLatitud());

        double lat2 = Math.toRadians(sucursal.getLatitud());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +

        Math.sin(dLon/2) * Math.sin(dLon/2) *

        Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            
        return c * RADIO_TIERRA;
	}

	

}
