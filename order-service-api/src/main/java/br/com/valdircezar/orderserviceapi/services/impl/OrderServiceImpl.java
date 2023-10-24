package br.com.valdircezar.orderserviceapi.services.impl;

import br.com.valdircezar.orderserviceapi.entities.Order;
import br.com.valdircezar.orderserviceapi.mapper.OrderMapper;
import br.com.valdircezar.orderserviceapi.repositories.OrderRepository;
import br.com.valdircezar.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;
import static models.enums.OrderStatusEnum.CLOSED;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public Order findById(final Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + Order.class.getSimpleName()
                ));
    }

    @Override
    public void save(CreateOrderRequest request) {
        final var entity = repository.save(mapper.fromRequest(request));
        log.info("Order created: {}", entity);
    }

    @Override
    public OrderResponse update(final Long id, UpdateOrderRequest request) {
        Order entity = findById(id);
        entity = mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(CLOSED)) {
            entity.setClosedAt(now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    @Override
    public void deleteById(final Long id) {
        repository.delete(findById(id));
    }

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }
}
