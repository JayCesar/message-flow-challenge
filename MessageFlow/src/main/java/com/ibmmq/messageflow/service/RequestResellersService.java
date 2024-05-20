package com.ibmmq.messageflow.service;

import com.ibmmq.messageflow.dto.ResellerDTO;
import com.ibmmq.messageflow.model.Book;
import com.ibmmq.messageflow.model.Vendor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RequestResellersService {

    private String[] resellerNames = DataGenerationService.generateResellerNames();

    List<Book> resellerStock = DataGenerationService.generateBookStockReseller(Vendor.bookStock);

    @Scheduled(fixedRate = 10000)
    private void getUpdate() {
        resellerStock = DataGenerationService.generateBookStockReseller(Vendor.bookStock);
    }

    public List<ResellerDTO> listAllResellers() {
        List<ResellerDTO> resellerDTOS = new ArrayList<>();
        for (int i = 0; i < resellerNames.length; i++) {
            ResellerDTO resellerDTO = new ResellerDTO(resellerNames[i], resellerStock);
            resellerDTOS.add(resellerDTO);
        }
        return resellerDTOS;
    }

}
