package shoppingMall.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {


    @Query("select o from Order o " +
            "where o.user.username = :username " +
            "order by o.orderDateTime desc" )
    List<Order> findOrder(@Param("username") String username, Pageable pageable);


    @Query("select count(o) from Order o " +
            "where o.user.username = :username")
    Long findOrderCount(@Param("username") String username);

    @Query("select o from Order o " +
            "where o.user.username = :username " +
            "order by o.orderDateTime desc" )
    List<Order> findReviewer(@Param("username") String username);

    @Query("select o from Order o " +
            "where o.id = :id " +
            "order by o.orderDateTime desc" )
    Optional<Order> findOrder(@Param("id") Long id);

}
