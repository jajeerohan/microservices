package com.substring.foodie.food.service.external;

import com.substring.foodie.food.config.AppConstants;
import com.substring.foodie.food.dto.RestaurantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class RestaurantWebClientService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // get restaurant by Id
    public RestaurantDto getRestaurantById(String restaurantId){
        RestaurantDto restaurantDto = webClientBuilder.baseUrl(AppConstants.RESTAURANT_SERVICE_URL)
                .build()
                .get()
                .uri("/api/v1/restaurants/{id}", restaurantId)
                .retrieve()
                .bodyToMono(RestaurantDto.class)
                .block();
        return restaurantDto;
    }

    // get all restaurants
    public List<RestaurantDto> getAllRestaurants() {
        return webClientBuilder.baseUrl(AppConstants.RESTAURANT_SERVICE_URL)
                .build()
                .get()
                .uri("/api/v1/restaurants")
                .retrieve()
                .bodyToFlux(RestaurantDto.class)
                .collectList()
                .block();
    }

    // post a restaurant
    public RestaurantDto restaurantDto(RestaurantDto restaurantDto) {
        return webClientBuilder.baseUrl(AppConstants.RESTAURANT_SERVICE_URL)
                .build()
                .post()
                .uri("/api/v1/restaurants")
                .header("Athorization", "Bearer asdgag")
                .bodyValue(restaurantDto)
                .retrieve()
                .bodyToMono(RestaurantDto.class)
                .block();
    }

    // non blocking:

    // get by id
    public Mono<RestaurantDto> getResbyId(String restId) {
        return webClientBuilder.baseUrl(AppConstants.RESTAURANT_SERVICE_URL)
                .build()
                .get()
                .uri("/api/v1/restaurants/{id}", restId)
                .retrieve()
                .bodyToMono(RestaurantDto.class);
    }

    // get all restaurants
    public Flux<RestaurantDto> getAllNon() {
        return webClientBuilder.baseUrl(AppConstants.RESTAURANT_SERVICE_URL)
                .build()
                .get()
                .uri("/api/v1/restaurants")
                .retrieve()
                .bodyToFlux(RestaurantDto.class);
    }
}

