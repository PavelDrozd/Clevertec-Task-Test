package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.exception.ProductValidationException;
import ru.clevertec.product.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(productMap.values());
    }

    @Override
    public Product save(Product product) {
        checkNullProduct(product);
        UUID generatedUUID = UUID.randomUUID();
        productMap.put(generatedUUID, product);
        return findById(generatedUUID).orElseThrow();
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }

    private void checkNullProduct(Product product) {
        if (product == null) {
            throw new ProductValidationException("Передаваемый продукт является null");
        }
    }
}
