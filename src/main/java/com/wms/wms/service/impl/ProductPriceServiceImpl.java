package com.wms.wms.service.impl;

import com.wms.wms.dto.request.ProductPriceRequest;
import com.wms.wms.entity.AbstractPartner;
import com.wms.wms.entity.Product;
import com.wms.wms.entity.ProductPrice;
import com.wms.wms.exception.ResourceNotFoundException;
import com.wms.wms.repository.ProductPriceRepository;
import com.wms.wms.service.EntityRetrievalService;
import com.wms.wms.service.ProductPriceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductPriceServiceImpl implements ProductPriceService {
    private final ProductPriceRepository productPriceRepository;
    private final EntityRetrievalService entityRetrievalService;

    @Override
    public ProductPrice save(ProductPriceRequest request) {

        // Get price
        ProductPrice productPrice;
        if (request.getId() != 0) {
            productPrice = this.getProductPriceById(request.getId());
        } else {
            // Only save product and partner when create
            Product product = entityRetrievalService.getProductById(request.getProductId());
            AbstractPartner partner = entityRetrievalService.getPartnerById(request.getPartnerId());

            productPrice = ProductPrice.builder()
                    .productId(request.getProductId())
                    .partnerId(request.getPartnerId())
                    .build();
        }

        // Check start Date
        Date dateApply;
        if (request.getDateApply() != null) {
            dateApply = request.getDateApply();
        }
        else {
            dateApply = new Date();
        }
        productPrice.setPrice(request.getPrice());
        productPrice.setDateApply(dateApply);

        // If currentPrice of product exists, update endDate of that price
//        ProductPrice currentPrice = this.getCurrentPrice(product);
//        if (currentPrice != null) {
//            if (currentPrice.getStartDate().after(startDate)) {
//                throw new ConstraintViolationException("Start Date must be after" + currentPrice.getStartDate());
//            }
//            currentPrice.setEndDate(request.getStartDate());
//            productPriceRepository.save(currentPrice);
//        }
        productPriceRepository.save(productPrice);
        log.info("Save Product price successfully with ID: {}", productPrice.getId());

        return productPrice;
    }

    @Override
    public List<ProductPrice> findAll() {
        return productPriceRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        ProductPrice productPrice = this.getProductPriceById(id);
        productPriceRepository.delete(productPrice);
        log.info("Delete Product price successfully with ID: {}", id);
    }

    @Override
    public ProductPrice getProductPriceById(int id) {
        return productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Product price exists"));
    }

    @Override
    public ProductPrice getCurrentPrice(Product product) {
        return productPriceRepository.findLatestPriceByProductId(product.getId());
    }

    @Override
    public List<ProductPrice> findPricesByProductId(int productId) {
        return productPriceRepository.findByProductId(productId);
    }

    @Override
    public List<ProductPrice> findLatestPricesForAllProducts() {
        return productPriceRepository.findLatestPricesForAllProducts();
    }
}
