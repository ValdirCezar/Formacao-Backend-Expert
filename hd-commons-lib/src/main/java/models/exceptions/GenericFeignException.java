package models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenericFeignException extends RuntimeException{
    private Integer status;
    private Map error;
}
