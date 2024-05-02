package com.wms.wms.dao.impl;

import com.wms.wms.dao.AbstractDAO;
import com.wms.wms.dao.IProductCategoryDAO;
import com.wms.wms.entity.ProductCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryDAOImpl extends AbstractDAO<ProductCategory> implements IProductCategoryDAO {
    private EntityManager entityManager;

    @Autowired
    public ProductCategoryDAOImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<ProductCategory> findAll() {
      return super.findAll(ProductCategory.class);
    }

    @Override
    public List<ProductCategory> findByName(String categoryName) {
        String sql = "SELECT o FROM ProductCategory WHERE o.name = :0";
        return super.findMany(ProductCategory.class, sql, categoryName);
    }

    @Override
    public ProductCategory findById(int categoryId) {
       return super.findById(ProductCategory.class, categoryId);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return super.save(productCategory);
    }

    @Override
    public void deleteById(ProductCategory productCategory) {
        super.delete(productCategory);
    }
}
