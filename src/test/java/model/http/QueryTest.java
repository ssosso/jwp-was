package model.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {
    private List<QueryParameter> givenQueryParameters;

    @BeforeEach
    void setUp() {
        givenQueryParameters = new ArrayList<>(Arrays.asList(QueryParameter.of("userId", "ssosso"),
                QueryParameter.of("password", "password"),
                QueryParameter.of("name", "ssosso")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"userId=ssosso&password=password&name=ssosso"})
    void of(String queryString) {
        Query query = Query.of(queryString);
        assertThat(query).isEqualTo(Query.of(givenQueryParameters));
    }

    @Test
    void addAll() {
        List<QueryParameter> queryParameters = Arrays.asList(QueryParameter.of("ssosso", "nice"), QueryParameter.of("hey", "like"));

        Query queryGiven = Query.ofEmpty();
        Query queryExpected = Query.of(queryParameters);

        Query queryForAdd = Query.of(queryParameters);

        assertThat(queryGiven.addAll(queryForAdd)).isEqualTo(queryExpected);
    }
}
