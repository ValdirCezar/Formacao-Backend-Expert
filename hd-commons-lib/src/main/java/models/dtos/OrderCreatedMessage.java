package models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import models.responses.OrderResponse;
import models.responses.UserResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private OrderResponse order;
    private UserResponse customer;
    private UserResponse requester;
}
