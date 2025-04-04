package com.substring.foodie.food.service.external;

import com.substring.foodie.food.dto.RestaurantDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantServiceFallback implements RestaurantServiceFeign {

    @Override
    public RestaurantDto getRestaurantDto(String id) {
        System.out.println("Fallback method called for restaurant service in food-service");
        return null;
    }

    @Override
    public List<RestaurantDto> getAll() {
        return List.of();
    }

    @Override
    public RestaurantDto create(RestaurantDto dto) {
        return null;
    }

    @Override
    public void delete(String restaurantId) {

    }
}
