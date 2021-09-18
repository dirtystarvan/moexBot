package ru.sbrf.trade.data.context;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.trade.data.bh.StockDataPipeline;
import ru.sbrf.trade.data.da.DataStorageConnection;
import ru.sbrf.trade.data.da.StockConnection;
import ru.sbrf.trade.data.da.impl.ClickhouseDataStorageConnectionImpl;
import ru.sbrf.trade.data.da.impl.FileDataStorageConnectionImpl;
import ru.sbrf.trade.data.da.impl.MoexStockConnectionImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class AppConfig {

    private static final String URI = "http://iss.moex.com/iss/history/engines/stock/markets/shares/boards/tqbr/securities.json?date=";
//    private static final String DB_URL = "jdbc:clickhouse://localhost:8123/default";

    private static final String DB_HOST    = "rc1a-6kbyyb4pvl3ovlg3.mdb.yandexcloud.net";
    private static final String DB_NAME    = "moex-shares";
    private static final String DB_USER    = "test_moex_user";
    private static final String DB_PASS    = "qwerty123456";
    private static final String CACERT     = "CA.pem";
    private static final String FULL_DB_URL = String.format("jdbc:clickhouse://%s:8443/%s?ssl=1&sslmode=strict&sslrootcert=%s",
            DB_HOST, DB_NAME, CACERT);

    @Bean(name="stockDataPipeline")
    public StockDataPipeline stockDataPipelineInitMethod() {
        return new StockDataPipeline();
    }

    @Bean(name="stockConnection")
    public StockConnection moexStockConnectionImplInitMethod(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new MoexStockConnectionImpl(URI, httpClient);
    }

    @Bean(name="storageConnection")
    public DataStorageConnection clickhouseDataStorageConnectionImpl() throws SQLException {
        Connection connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASS);
        return new ClickhouseDataStorageConnectionImpl(connection);
    }

    @Bean(name = "consumerRestTemplate")
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return new RestTemplate(factory);
    }
}
