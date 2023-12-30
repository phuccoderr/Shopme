package com.shopme.product;

import com.shopme.common.entity.product.Product;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<Product> withSearch(String keyword) {
        return (root, query, builder) -> {
            Predicate enabledPredicate = builder.isTrue(root.get("enabled"));
            Predicate searchPredicate = builder.or(
                    builder.like(root.get("name"), "%" + keyword + "%"),
                    builder.like(root.get("shortDescription"), "%" + keyword + "%"),
                    builder.like(root.get("fullDescription"), "%" + keyword + "%")
            );
            return builder.and(enabledPredicate, searchPredicate);
        };
    }
    public static Specification<Product> withCategory(Integer categoryId,String categoryIdMatch) {
        return (root, query, builder) -> {
            Predicate enabledPredicate = builder.isTrue(root.get("enabled"));
            Predicate categoryPredicate = builder.or(
                    builder.equal(root.get("category").get("id"), categoryId),
                    builder.like(root.get("category").get("allParentIds"), "%" + categoryIdMatch + "%")
            );
            return builder.and(enabledPredicate, categoryPredicate);
        };
    }


    public static Specification<Product> withBrand(Integer brandId) {
        return (root, query, builder) -> {
            Predicate enabledPredicate = builder.isTrue(root.get("enabled"));
            Predicate brandPredicate =
                    builder.equal(root.get("brand").get("id"),brandId);
            return builder.and(enabledPredicate, brandPredicate);
        };
    }

}
