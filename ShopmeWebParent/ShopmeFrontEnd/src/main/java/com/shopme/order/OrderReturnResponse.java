package com.shopme.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class OrderReturnResponse {
    private Integer orderId;

    public OrderReturnResponse(Integer orderId) {
        this.orderId = orderId;
    }
}
