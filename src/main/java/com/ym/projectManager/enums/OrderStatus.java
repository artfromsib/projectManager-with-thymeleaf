package com.ym.projectManager.enums;

import java.util.stream.Stream;

public enum OrderStatus {
    NEW,
    IN_PROGRESS,
    COMPLETE;

    public static Stream<OrderStatus> stream() {
        return Stream.of(OrderStatus.values());
    }
}
