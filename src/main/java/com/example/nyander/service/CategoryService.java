package com.example.nyander.service;

import com.example.nyander.repository.CategoryRepository;
import com.example.nyander.repository.PrefectureRepository;
import com.example.nyander.repository.entity.Category;
import com.example.nyander.repository.entity.Prefecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Map<Integer, String> getCategoryAll() {
        List<Category> results = categoryRepository.findAllByOrderByIdAsc();
        HashMap<Integer,String> categories = new HashMap<>();
        for (Category result : results) {
            categories.put(result.getId(), result.getCategory());
        }
        return categories;
    }
}
