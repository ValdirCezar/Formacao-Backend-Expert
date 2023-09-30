package br.com.valdircezar.orderserviceapi.services.impl;

import br.com.valdircezar.orderserviceapi.mapper.OrderMapper;
import br.com.valdircezar.orderserviceapi.repositories.OrderRepository;
import br.com.valdircezar.orderserviceapi.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.requests.CreateOrderRequest;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;

    @Override
    public void save(CreateOrderRequest request) {
        final var entity = repository.save(mapper.fromRequest(request));
        log.info("Order created: {}", entity);
    }
}
