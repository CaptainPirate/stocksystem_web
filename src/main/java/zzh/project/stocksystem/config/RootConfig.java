package zzh.project.stocksystem.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan({ "zzh.project.stocksystem.service" })
@Import({ PersistenceConfig.class, PushConfig.class })
public class RootConfig {

}
