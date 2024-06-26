package com.wms.wms.service.impl;

import com.wms.wms.dto.request.MaterialOrderRequest;
import com.wms.wms.dto.request.OrderItemRequest;
import com.wms.wms.dto.request.OrderStatusRequest;
import com.wms.wms.dto.response.order.MaterialOrderDetailResponse;
import com.wms.wms.entity.*;
import com.wms.wms.entity.enumentity.OrderItemType;
import com.wms.wms.entity.enumentity.OrderStatus;
import com.wms.wms.exception.ConstraintViolationException;
import com.wms.wms.exception.ResourceNotFoundException;
import com.wms.wms.repository.MaterialOrderRepository;
import com.wms.wms.service.*;
import com.wms.wms.util.StringHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MaterialOrderServiceImpl implements MaterialOrderService {
    private final MaterialOrderRepository materialOrderRepository;
    private final EntityRetrievalService entityRetrievalService;

    @Transactional
    @Override
    public MaterialOrderDetailResponse save(MaterialOrderRequest requestDTO) {
        MaterialOrder materialOrder;
        if (requestDTO.getId() != 0) {
            materialOrder = this.getMaterialOrder(requestDTO.getId());
        }
        else {
            materialOrder = MaterialOrder.builder().build();
        }
        // Validate Supplier
        Supplier supplier = entityRetrievalService.getSupplierById(requestDTO.getSupplierId());
        this.savePartnerInfo(materialOrder, supplier);

        materialOrder.setName(StringHelper.preProcess(requestDTO.getName()));

        materialOrder.setOrderDate(requestDTO.getOrderDate());
        materialOrder.setExpectedDate(requestDTO.getExpectedDate());
        materialOrder.setActualDate(requestDTO.getActualDate());

        // Remove old OrderItems
        if (!materialOrder.getOrderItems().isEmpty()) {
            List<OrderItem> existingItems = new ArrayList<>(materialOrder.getOrderItems());
            existingItems.forEach(materialOrder::removeOrderItem);
        }

        // Save new OrderItems
        if (!requestDTO.getOrderItems().isEmpty()) {
            List<OrderItem> newItems = this.convertToOrderItems(requestDTO.getOrderItems());
            newItems.forEach(materialOrder::addOrderItem);
        }

        this.saveTotalCost(materialOrder);

        MaterialOrder dbOrder = materialOrderRepository.save(materialOrder);
        log.info("Add material order successfully");
        return this.convertToDetailResponse(dbOrder);
    }

    // Map OrderItem request to OrderItem entity
    private List<OrderItem> convertToOrderItems(List<OrderItemRequest> requestDTOList) {
        // Get product list

        Set<Integer> productIds = requestDTOList.stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toSet());
        List<Product> products = entityRetrievalService.getProductByIds(productIds);

        return  requestDTOList.stream().map(requestDTO ->
        {
            // Validate product
            Product product = products.stream()
                    .filter(p -> p.getId() == requestDTO.getProductId())
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Product is not exist"));

            // Validate price
            ProductPrice currentPrice = entityRetrievalService.getCurrentProductPrice(product);
            if (currentPrice == null) {
                throw new ConstraintViolationException("Please add price of product: " + product.getName());
            }

            // Map OrderItem
            OrderItem orderItem;
            if (requestDTO.getId() != 0) {
                orderItem = entityRetrievalService.getOrderItemById(requestDTO.getId());
            }
            else {
                orderItem = OrderItem.builder().build();
            }
            orderItem.setOrderType(OrderItemType.MATERIAL);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setProductUom(product.getUom());
            orderItem.setProductPrice(currentPrice.getPrice());
            orderItem.setQuantity(requestDTO.getQuantity());
            return orderItem;
        }).toList();
    }

    private void saveTotalCost(MaterialOrder order) {
        List<OrderItem> orderItems = order.getOrderItems();
        BigDecimal totalCost = orderItems.stream()
                .map(OrderItem::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalOrderCost(totalCost);
        order.setTax(MaterialOrder.IMPORT_TAX);
    }

    private void savePartnerInfo(MaterialOrder order, Supplier supplier) {
        order.setSupplierId(supplier.getId());
        order.setPartnerName(supplier.getName());
        order.setPartnerEmail(supplier.getEmail());
        order.setPartnerPhone(supplier.getPhone());
        order.setPartnerAddress(supplier.getAddress());
    }


    @Override
    @Transactional
    public void updateOrderStatus(int orderId, OrderStatusRequest request) {
        MaterialOrder order = this.getMaterialOrder(orderId);
        OrderStatus newStatus = request.getStatus();
        order.setStatus(newStatus);
        materialOrderRepository.save(order);
    }

    @Override
    public MaterialOrderDetailResponse findById(int orderId) {
        MaterialOrder dbOrder = getMaterialOrder(orderId);
        log.info("Get Material_order detail id: {} successfully", orderId);
        return this.convertToDetailResponse(dbOrder);
    }


    @Override
    public List<MaterialOrderDetailResponse> findAll() {
        List<MaterialOrder> materialOrderList = materialOrderRepository.findAll();
        List<MaterialOrderDetailResponse> orderResponseList = new ArrayList<>();
        materialOrderList.forEach(order -> orderResponseList.add(this.convertToDetailResponse(order)));
        log.info("Get all material orders successfully");
        return orderResponseList;
    }

    @Override
    @Transactional
    public void deleteById(int orderId) {
        MaterialOrder materialOrder = this.getMaterialOrder(orderId);
        materialOrderRepository.delete(materialOrder);
        log.info("Delete material order id: {} successfully", orderId);
    }

    /**
     * Find Material order by id from Database
     *
     * @param orderId order ID
     * @return MaterialOrder || ResourceNotFoundException
     */
    private MaterialOrder getMaterialOrder(int orderId) {
        return  materialOrderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Material order not found with ID:" + orderId));
    }


    private MaterialOrderDetailResponse convertToDetailResponse(MaterialOrder materialOrder) {
        return MaterialOrderDetailResponse.builder()
                .id(materialOrder.getId())
                .supplierId(materialOrder.getSupplierId())
                .name(materialOrder.getName())
                .partnerName(materialOrder.getPartnerName())
                .partnerEmail(materialOrder.getPartnerEmail())
                .partnerPhone(materialOrder.getPartnerPhone())
                .partnerAddress(materialOrder.getPartnerAddress())
                .totalOrderCost(materialOrder.getTotalOrderCost())
                .tax(materialOrder.getTax())
                .orderDate(materialOrder.getOrderDate())
                .expectedDate(materialOrder.getExpectedDate())
                .actualDate(materialOrder.getActualDate())
                .additionalData(materialOrder.getAdditionalData())
                .status(materialOrder.getStatus())
                .createdAt(materialOrder.getCreatedAt())
                .modifiedAt(materialOrder.getModifiedAt())
                .orderItems(materialOrder.getOrderItems())
                .build();
    }
}
