package com.ibmmq.messageflow.controller;

import com.ibmmq.messageflow.dto.ResellerDTO;
import com.ibmmq.messageflow.model.Book;
import com.ibmmq.messageflow.service.RequestResellersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resellerapp")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ResellersController {

    @Autowired
    private RequestResellersService RequestResellersService;

    @GetMapping("/resellers")
    public List<ResellerDTO> list() {;
        return RequestResellersService.listAllResellers();
    }

}
