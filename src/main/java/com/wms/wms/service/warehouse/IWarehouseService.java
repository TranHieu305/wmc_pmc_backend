package com.wms.wms.service.warehouse;

import com.wms.wms.entity.Warehouse;

import java.util.List;

public interface IWarehouseService {

    // Get list of all warehouses
    List <Warehouse> findAll();

    // Get warehouse by id
    Warehouse findById(int id);

    // Save warehouse
    Warehouse save(Warehouse warehouse);

    // Delete warehouse by Id
    void deleteById(int id);
}