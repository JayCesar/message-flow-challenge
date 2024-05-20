package com.ibmmq.messageflow.dto;

import com.ibmmq.messageflow.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResellerDTO {
    private String name;
    private List<Book> resellerBookStock;
}
