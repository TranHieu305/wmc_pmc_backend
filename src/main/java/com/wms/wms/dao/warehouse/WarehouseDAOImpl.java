package com.wms.wms.dao.warehouse;

import com.wms.wms.entity.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WarehouseDAOImpl implements IWarehouseDAO{
    private EntityManager entityManager;

    @Autowired
    public WarehouseDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Warehouse> findAll() {
        TypedQuery<Warehouse> query = entityManager.createQuery("FROM Warehouse", Warehouse.class);
        List<Warehouse> warehouses = query.getResultList();
        return warehouses;
    }

    @Override
    public Warehouse findById(int id) {
        Warehouse theWarehouse = entityManager.find(Warehouse.class, id);
        return theWarehouse;
    }

    @Override
    public Warehouse save(Warehouse theWarehouse) {
        Warehouse dbWarehouse = entityManager.merge(theWarehouse);
        return dbWarehouse;
    }

    @Override
    public void deleteById(int id) {
        Warehouse theWarehouse = entityManager.find(Warehouse.class, id);
        if (theWarehouse != null) {
            entityManager.remove(theWarehouse);
        }
        return;
    }
}