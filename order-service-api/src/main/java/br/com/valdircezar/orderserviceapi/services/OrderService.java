package br.com.valdircezar.orderserviceapi.services;

import br.com.valdircezar.orderserviceapi.entities.Order;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {

    Order findById(final Long id);

    void save(CreateOrderRequest request);

    OrderResponse update(final Long id, UpdateOrderRequest request);

    void deleteById(final Long id);

    List<Order> findAll();

    Page<Order> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy);
}
