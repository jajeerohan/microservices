package com.substring.foodie.food.service;


import com.substring.foodie.food.dto.FoodCategoryDTO;
import com.substring.foodie.food.dto.FoodItemDTO;
import com.substring.foodie.food.dto.RestaurantDto;
import com.substring.foodie.food.entities.FoodCategory;
import com.substring.foodie.food.entities.FoodItem;
import com.substring.foodie.food.repository.FoodCategoryRepo;
import com.substring.foodie.food.repository.FoodItemRepo;
import com.substring.foodie.food.service.external.RestaurantServiceFeign;
import com.substring.foodie.food.service.external.RestaurantWebClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FoodItemService {

    private int attemptCounter = 0;

    @Autowired
    private FoodItemRepo foodItemRepository;

    @Autowired
    private FoodCategoryRepo foodCategoryRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestaurantServiceFeign restaurantServiceFeign; //feign-client

    @Autowired
    private RestaurantWebClientService restaurantWebClientService;

    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FoodItemDTO getFoodItemById(String id) {

        FoodItem foodItem = foodItemRepository.findById(id).orElseThrow(() -> new RuntimeException("FoodItem Not found with Id " + id));

        //RestTemplate
//        String restaurantServiceUrl = "http://restaurant-service/api/v1/restaurants/" + foodItem.getRestaurantId();
//        RestaurantDto restaurantDto = restTemplate.getForObject(restaurantServiceUrl, RestaurantDto.class);


        //FeignClient
        RestaurantDto restaurantDto = restaurantServiceFeign.getRestaurantDto(foodItem.getRestaurantId());

        //WebClient
//        RestaurantDto restaurantDto = restaurantWebClientService.getRestaurantById(foodItem.getRestaurantId());

        FoodItemDTO foodItemDTO = convertToDTO(foodItem);
        foodItemDTO.setRestaurant(restaurantDto);
        return  foodItemDTO;

    }

    public FoodItemDTO createFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = convertToEntity(foodItemDTO);
        foodItem.setId(UUID.randomUUID().toString());


        FoodCategory foodCategory = foodCategoryRepository.findById(foodItemDTO.getFoodCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        foodItem.setFoodCategory(foodCategory);

        foodItem = foodItemRepository.save(foodItem);
        return convertToDTO(foodItem);
    }

    public void deleteFoodItem(String id) {
        foodItemRepository.deleteById(id);
    }

    private FoodItemDTO convertToDTO(FoodItem foodItem) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setTitle(foodItem.getTitle());
        dto.setDescription(foodItem.getDescription());
        dto.setQuantity(foodItem.getQuantity());
        dto.setOutOfStock(foodItem.isOutOfStock());
        dto.setFoodType(foodItem.getFoodType());
        dto.setFoodCategoryId(foodItem.getFoodCategory().getId());
        FoodCategoryDTO foodCategoryDTO = new FoodCategoryDTO();
        foodCategoryDTO.setId(foodItem.getFoodCategory().getId());
        foodCategoryDTO.setName(foodItem.getFoodCategory().getName());
        foodCategoryDTO.setDescription(foodItem.getFoodCategory().getDescription());
        dto.setFoodCategory(foodCategoryDTO);

        dto.setRestaurantId(foodItem.getRestaurantId());
        return dto;
    }

    private FoodItem convertToEntity(FoodItemDTO dto) {
        FoodItem foodItem = new FoodItem();
        foodItem.setId(dto.getId());
        foodItem.setTitle(dto.getTitle());
        foodItem.setDescription(dto.getDescription());
        foodItem.setQuantity(dto.getQuantity());
        foodItem.setOutOfStock(dto.isOutOfStock());
        foodItem.setFoodType(dto.getFoodType());
        foodItem.setRestaurantId(dto.getRestaurantId());
        // Set foodCategory and restaurantId as needed
        return foodItem;
    }
}