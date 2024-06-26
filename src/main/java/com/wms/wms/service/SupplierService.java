package com.wms.wms.service;

import com.wms.wms.dto.request.SupplierRequest;
import com.wms.wms.dto.response.supplier.SupplierDetailResponse;
import com.wms.wms.entity.Supplier;

import java.util.List;

public interface SupplierService {

    // Get list of all suppliers
    List<SupplierDetailResponse> findAll();

    // Get supplier by id
    SupplierDetailResponse findById(int id);

    // Save supplier
    SupplierDetailResponse save(SupplierRequest supplierRequestDTO);

    // Find by Id
    Supplier getSupplierById(int supplierId);

    // Delete supplier by Id
    void deleteById(int id);
}
