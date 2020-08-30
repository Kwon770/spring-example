package tacos.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.Order;
import tacos.Taco;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcOrderRepository implements OrderRepository {
    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order").usingGeneratedKeyColumns("id");

        this.orderTacoInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order_Tacos");

        // objectMapper : 데이터 추가 메소드 execute(), executeAndReturnKey()
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();

        for (Taco taco : tacos){
            saveTacoToOrder(taco, orderId);
        }

        return order;
    }

    private long saveOrderDetails(Order order) {
        // Map<String, Object> 타입으로 데이터 저장, String(key): the name of table column, Object(value): the data
        // ObjectMapper.convertValue(): Convert order to Map
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        // ObjectMapper은 Data 타입의 값을 long 타입으로 변환하기 때문에, Key가 "placedAt"인 데이터 교체.
        values.put("placedAt", order.getPlacedAt());

        long orderId = orderInserter.executeAndReturnKey(values).longValue();
        return orderId;
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        // Map의 Key는 테이블의 열 이름과 같기때문에 다음과 같이 key를 설정
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
}
