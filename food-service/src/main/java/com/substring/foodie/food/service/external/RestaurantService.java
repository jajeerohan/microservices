package com.substring.foodie.food.service.external;

import com.substring.foodie.food.config.AppConstants;
import com.substring.foodie.food.dto.RestaurantDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "restaurant-service", url = "http://restaurant-service")
@FeignClient(name = AppConstants.RESTAURANT_SERVICE_NAME)
public interface RestaurantService {

    @GetMapping("/api/v1/restaurants/{id}")
    RestaurantDto getRestaurantDto(@PathVariable("id") String id);

}
