package ru.practicum.ewm.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryPublicService {

    List<Category> findAll(Pageable pageable);

    Category find(Long categoryId);

}
