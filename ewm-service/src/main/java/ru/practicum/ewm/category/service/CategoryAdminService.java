package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;

public interface CategoryAdminService {

    Category add(CategoryDto categoryDto);

    Category update(Long categoryId, CategoryDto categoryDto);

    void delete(Long categoryId);

}
