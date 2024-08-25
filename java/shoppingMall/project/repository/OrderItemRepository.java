package shoppingMall.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.entity.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {


    @Query("select count(o) from OrderItem o where o.order.id = :id")
    int orderItemCon(@Param("id")Long id);



}
