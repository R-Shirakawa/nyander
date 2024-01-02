package com.example.nyander.service;

import com.example.nyander.repository.PrefectureRepository;
import com.example.nyander.repository.entity.Prefecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrefectureService {

    @Autowired
    PrefectureRepository prefectureRepository;

    public Map<Integer, String> getPrefectureAll() {
        List<Prefecture> results = prefectureRepository.findAllByOrderByIdAsc();
        HashMap<Integer,String> prefectures = new HashMap<>();
        for (Prefecture result : results) {
            prefectures.put(result.getId(), result.getPrefectureName());
        }
        return prefectures;
    }

}
