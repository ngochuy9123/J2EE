package com.springboot.j2ee.service;

import com.springboot.j2ee.dto.SearchHistoryDTO;
import com.springboot.j2ee.entity.SearchHistory;

public interface SearchHistoryService {
    SearchHistory saveSearchHistory(SearchHistoryDTO searchHistoryDTO);


//    Truyen id search history
    void delete(Long id);
}
