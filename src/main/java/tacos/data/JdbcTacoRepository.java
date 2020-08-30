package tacos.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        // Taco 객체를 DB에 저장하고 저장된 ID 반환
        long tacoId = saveTacoInfo(taco);
        // 저장된 ID를 Taco 객체에 저장.
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()) {
            // Ingredient와 Taco의 관계 저장을 위해 넘겨받은 모든 Ingredient를 Taco_Ingredients 에 저장.
            saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreator psc = new PreparedStatementCreatorFactory("insert into Taco (name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP).newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime())));
        // KeyHolder가 저장된 Taco의 ID 제공
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);
        // ID 반
        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient)" + "values (?, ?)", tacoId, ingredient.getId());
    }
}
