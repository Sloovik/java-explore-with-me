package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Category add(CategoryDto categoryDto) {
        checkIsCategoryExist(categoryDto);

        return categoryRepository.save(CategoryMapper.toCategoryShort(categoryDto));
    }

    @Override
    public Category update(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("Category with id %x not found", categoryId)));

        category.setName(categoryDto.getName());

        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("It is not possible to delete a category that is associated with events.");
        }

        categoryRepository.deleteById(categoryId);
    }

    private void checkIsCategoryExist(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException(String.format("Category with name %s already exits", categoryDto.getName()));
        }
    }

}
