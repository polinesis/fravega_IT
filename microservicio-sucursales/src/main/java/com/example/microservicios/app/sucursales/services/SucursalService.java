package com.example.microservicios.app.sucursales.services;

import com.example.commons.services.CommonService;
import com.example.commons.sucursales.models.entity.Sucursal;
import com.example.microservicios.app.sucursales.model.PuntoRetiro;

public interface SucursalService  extends CommonService<Sucursal> {
	
	    public double obtenerSucursalMasCernaca(PuntoRetiro puntoRetiro, Sucursal sucursal);
	    

}
