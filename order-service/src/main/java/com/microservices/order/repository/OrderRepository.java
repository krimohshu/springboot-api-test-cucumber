package com.microservices.order.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservices.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    List<Order> findByUserId(Long userId);

    List<Order> findByProductId(Long productId);

    List<Order> findByStatus(String status);

    List<Order> findByActive(Boolean active);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByProductId(Long productId, Pageable pageable);

    Page<Order> findByStatus(String status, Pageable pageable);

    Page<Order> findByActive(Boolean active, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE " +
           "(:userId IS NULL OR o.userId = :userId) AND " +
           "(:productId IS NULL OR o.productId = :productId) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:active IS NULL OR o.active = :active) AND " +
           "(:orderNumber IS NULL OR LOWER(o.orderNumber) LIKE LOWER(CONCAT('%', :orderNumber, '%'))) AND " +
           "(:startDate IS NULL OR o.orderDate >= :startDate) AND " +
           "(:endDate IS NULL OR o.orderDate <= :endDate)")
    Page<Order> searchOrders(
        @Param("userId") Long userId,
        @Param("productId") Long productId,
        @Param("status") String status,
        @Param("active") Boolean active,
        @Param("orderNumber") String orderNumber,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    @Query("SELECT COUNT(o) FROM Order o WHERE o.active = :active")
    long countByActive(@Param("active") Boolean active);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countByStatus();

    @Query("SELECT o.userId, COUNT(o) FROM Order o GROUP BY o.userId ORDER BY COUNT(o) DESC")
    List<Object[]> countByUser();

    @Query("SELECT o.productId, COUNT(o) FROM Order o GROUP BY o.productId ORDER BY COUNT(o) DESC")
    List<Object[]> countByProduct();

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.status = :status")
    BigDecimal sumTotalPriceByStatus(@Param("status") String status);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.userId = :userId")
    BigDecimal sumTotalPriceByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT DISTINCT o.status FROM Order o")
    List<String> findDistinctStatuses();
}
