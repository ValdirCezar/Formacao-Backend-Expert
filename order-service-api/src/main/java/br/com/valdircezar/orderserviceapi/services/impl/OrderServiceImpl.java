package br.com.valdircezar.orderserviceapi.services.impl;

import br.com.valdircezar.orderserviceapi.clients.UserServiceFeignClient;
import br.com.valdircezar.orderserviceapi.entities.Order;
import br.com.valdircezar.orderserviceapi.mapper.OrderMapper;
import br.com.valdircezar.orderserviceapi.repositories.OrderRepository;
import br.com.valdircezar.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.dtos.OrderCreatedMessage;
import models.exceptions.ResourceNotFoundException;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import models.responses.UserResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final UserServiceFeignClient userServiceFeignClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Order findById(final Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Object not found. Id: " + id + ", Type: " + Order.class.getSimpleName()
                ));
    }

    @CacheEvict(value = "orders", allEntries = true)
    @Override
    public void save(CreateOrderRequest request) {
        final var requester = validateUserId(request.requesterId());
        final var customer = validateUserId(request.customerId());
        final var entity = repository.save(mapper.fromRequest(request));

        log.info("Order created: {}", entity);

        rabbitTemplate.convertAndSend(
                "helpdesk",
                "rk.orders.create",
                new OrderCreatedMessage(mapper.fromEntity(entity), customer, requester)
        );
    }

    @Override
    @CacheEvict(value = "orders", allEntries = true)
    public OrderResponse update(final Long id, UpdateOrderRequest request) {
        validateUsers(request);

        Order entity = findById(id);
        entity = mapper.fromRequest(entity, request);

        if (entity.getStatus().equals(CLOSED)) {
            entity.setClosedAt(now());
        }

        return mapper.fromEntity(repository.save(entity));
    }

    private void validateUsers(UpdateOrderRequest request) {
        if (request.requesterId() != null) validateUserId(request.requesterId());
        if (request.customerId() != null) validateUserId(request.customerId());
    }

    @Override
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteById(final Long id) {
        repository.delete(findById(id));
    }

    @Override
    @Cacheable(value = "orders")
    public List<Order> findAll() {
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "orders", key = "#page + '-' + #linesPerPage + '-' + #direction + '-' + #orderBy")
    public Page<Order> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        PageRequest pageRequest = PageRequest.of(
                page,
                linesPerPage,
                org.springframework.data.domain.Sort.Direction.valueOf(direction),
                orderBy
        );

        return repository.findAll(pageRequest);
    }

    public UserResponse validateUserId(final String id) {
        return userServiceFeignClient.findById(id).getBody();
    }
}
