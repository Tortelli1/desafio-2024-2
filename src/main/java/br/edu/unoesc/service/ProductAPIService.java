package br.edu.unoesc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.edu.unoesc.dto.ProductAPI;

@Service
public class ProductAPIService {

    private final RestTemplate restTemplate;

    public ProductAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductAPI fetchProductFromAPI(Integer id) {
        String url = "https://dummyjson.com/products/{id}";
        return restTemplate.getForObject(url, ProductAPI.class, id);
    }
}