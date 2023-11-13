package jvm.cot.javacotloader.mappers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public class PaginationMapper {
    public static Pageable getPageRequest(int page, int size, String sort) {
        return PageRequest.of(page, size, Sort.by(parseSortArray(sort)));
    }

    private static List<Order> parseSortArray(String sort) {
        List<Order> orders = new ArrayList<>();
        if (sort == null || sort.isEmpty()) {
            return orders;
        }
        String[] pairs = sort.split(";");
        for (String pair : pairs) {
            String[] elements = pair.split(",");
            String property = elements[0];
            String direction = elements.length > 1 ? elements[1] : "asc";
            orders.add(new Order(getSortDirection(direction), property));
        }
        return orders;
    }

    private static Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Direction.ASC;
        }
        return Direction.DESC;
    }
}
