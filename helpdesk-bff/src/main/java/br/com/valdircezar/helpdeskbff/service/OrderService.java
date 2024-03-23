package br.com.valdircezar.helpdeskbff.service;

import br.com.valdircezar.helpdeskbff.client.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import models.requests.CreateOrderRequest;
import models.requests.UpdateOrderRequest;
import models.responses.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderFeignClient orderFeignClient;

    public OrderResponse findById(final Long id) {
        return orderFeignClient.findById(id).getBody();
    }

    public void save(CreateOrderRequest request) {
        orderFeignClient.save(request);
    }

    public OrderResponse update(final Long id, UpdateOrderRequest request) {
        return orderFeignClient.update(id, request).getBody();
    }

    public void deleteById(final Long id) {
        orderFeignClient.deleteById(id);
    }

    public List<OrderResponse> findAll() {
        return orderFeignClient.findAll().getBody();
    }

    public Page<OrderResponse> findAllPaginated(Integer page, Integer linesPerPage, String direction, String orderBy) {
        return orderFeignClient.findAllPaginated(page, linesPerPage, direction, orderBy).getBody();
    }

}
