package kr.co.corners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;


@Configuration
@EnableAutoConfiguration
@EnableEncryptableProperties
@EnableTransactionManagement
@ComponentScan(basePackages = "kr.co.corners")
//@EnableResourceServer
//@EnableAuthorizationServer
@SpringBootApplication
public class SidpApplication extends SpringBootServletInitializer  {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SidpApplication.class);
    }
    
	public static void main(String[] args) {

		SpringApplication.run(SidpApplication.class, args);

	}
}
