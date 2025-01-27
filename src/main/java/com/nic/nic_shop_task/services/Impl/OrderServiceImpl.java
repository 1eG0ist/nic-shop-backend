package com.nic.nic_shop_task.services.Impl;

import com.nic.nic_shop_task.dtos.CellWithOutOrderDto;
import com.nic.nic_shop_task.models.Order;
import com.nic.nic_shop_task.models.OrderCell;
import com.nic.nic_shop_task.models.Product;
import com.nic.nic_shop_task.models.User;
import com.nic.nic_shop_task.repositories.OrderCellRepository;
import com.nic.nic_shop_task.repositories.OrderRepository;
import com.nic.nic_shop_task.repositories.UserRepository;
import com.nic.nic_shop_task.services.EmailService;
import com.nic.nic_shop_task.services.OrderService;
import com.nic.nic_shop_task.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final OrderCellRepository orderCellRepository;

    private final EmailService emailService;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> createOrderS(List<CellWithOutOrderDto> cells) {
        Long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findUserById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        productService.checkAndReduceProductQuantity(cells);

        Order order = orderRepository.save(new Order(
                null,
                ZonedDateTime.now(),
                userId,
                new ArrayList<>()));

        List<OrderCell> orderCells = cells.stream()
                .map(dto -> new OrderCell(
                        null,
                        order.getId(),
                        dto.getProductId(),
                        dto.getCount()
                )).collect(Collectors.toList());

        orderCellRepository.saveAll(orderCells);

        List<Product> products = productService.getProductsByIds(
                cells.stream().map(CellWithOutOrderDto::getProductId).collect(Collectors.toList()));

        emailService.sendEmail(
                user.getEmail(),
                "Информация по заказу",
                generateOrderInfo(cells, products)
        );

        return ResponseEntity.ok(order);
    }

    public static String generateOrderInfo(List<CellWithOutOrderDto> cells, List<Product> products) {
        Map<Long, Integer> productCountMap = cells.stream()
                .collect(Collectors.toMap(CellWithOutOrderDto::getProductId, CellWithOutOrderDto::getCount));

        StringBuilder orderInfo = new StringBuilder();
        Double sum = 0.0;
        for (Product product : products) {
            int count = productCountMap.getOrDefault(product.getId(), 0);
            double totalPrice = product.getPrice() * count;
            sum += totalPrice;
            orderInfo.append(product.getName())
                    .append(" | кол-во : ")
                    .append(count)
                    .append(" | Стоимость : ")
                    .append(totalPrice)
                    .append("\n");
        }

        orderInfo
                .append("Сумма: ")
                .append(sum);

        return orderInfo.toString();
    }
}
