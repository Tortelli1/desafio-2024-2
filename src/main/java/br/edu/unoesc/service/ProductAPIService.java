package br.edu.unoesc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.edu.unoesc.dto.ProductAPI;

@Service
public class ProductAPIService {

	@Autowired
    private final RestTemplate restTemplate;

    public ProductAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductAPI fetchProductFromAPI(Integer id) {
    	String url = "https://dummyjson.com/products/" + id;
    	ProductAPI product = restTemplate.getForObject(url, ProductAPI.class);
        return product;
    }
}