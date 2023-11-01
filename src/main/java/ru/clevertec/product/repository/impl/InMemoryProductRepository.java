package ru.clevertec.product.repository.impl;

import ru.clevertec.product.entity.Product;
import ru.clevertec.product.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryProductRepository implements ProductRepository {

    Map<UUID, Product> productMap = new HashMap<>();

    @Override
    public Optional<Product> findById(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(productMap.get(uuid));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(productMap.values());
    }

    @Override
    public Product save(Product product) {
        checkNullProduct(product);
        productMap.put(product.getUuid(), product);
        return findById(product.getUuid()).orElseThrow();
    }

    @Override
    public void delete(UUID uuid) {
        productMap.remove(uuid);
    }

    private void checkNullProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Передаваемый продукт является null");
        }
    }
}
