package com.example.microservicios.app.sucursales.services.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.commons.sucursales.models.entity.Sucursal;

public interface SucursalRepository extends PagingAndSortingRepository<Sucursal, Long>{


}
