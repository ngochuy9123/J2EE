package com.springboot.j2ee.service.impl;

import com.springboot.j2ee.dto.SearchHistoryDTO;
import com.springboot.j2ee.entity.SearchHistory;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.SearchHistoryRepository;
import com.springboot.j2ee.repository.UserRepository;
import com.springboot.j2ee.service.SearchHistoryService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class SearchHistoryServiceImpl implements SearchHistoryService {

    private final UserRepository userRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public SearchHistoryServiceImpl(UserRepository userRepository, SearchHistoryRepository searchHistoryRepository) {
        this.userRepository = userRepository;
        this.searchHistoryRepository = searchHistoryRepository;
    }


    @Override
    public SearchHistory saveSearchHistory(SearchHistoryDTO searchHistoryDTO) {

//        SearchHistory s = searchHistoryRepository.findBy


        SearchHistory searchHistory = changetoEntity(searchHistoryDTO);

        return searchHistoryRepository.save(searchHistory);
    }

    @Override
    public void delete(Long id) {
        searchHistoryRepository.deleteById(id);
    }
    private SearchHistory changetoEntity(SearchHistoryDTO searchHistoryDTO){
        SearchHistory searchHistory = new SearchHistory();
        searchHistory.setLastSearch(timestamp);
        searchHistory.setValue(searchHistoryDTO.getValue());
        User user = userRepository.findById(searchHistoryDTO.getIdUser()).get();
        searchHistory.setUser(user);

        return searchHistory;
    }
}
