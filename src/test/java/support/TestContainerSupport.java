package support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Slf4j
public class TestContainerSupport {


    private static final String MYSQL_IMAGE = "mysql:latest";

    @Container
    private static final JdbcDatabaseContainer MYSQL_CONTAINER = new MySQLContainer(MYSQL_IMAGE)
            .withDatabaseName("testDB")
            .withUsername("testUser")
            .withPassword("testPassword")
            .withInitScript("init.sql");


//    @DynamicPropertySource
//    public static void overrideProps (DynamicPropertyRegistry registry) {
//
//        log.info("컨테이너 설정 시작");
//
//        if(MYSQL_CONTAINER.isRunning()) {
//            log.info("mysql 컨테이너 실행됨");
//        } else if (!MYSQL_CONTAINER.isRunning()) {
//            log.info("mysql 컨테이너 실행 안됨");
//        }
//
//        registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);
//        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
////        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
////        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
//    }


}


