package nineto6.Talk.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class ETagConfig {
    @Bean
    FilterRegistrationBean<ShallowEtagHeaderFilter> getShallowETagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> frb = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
        frb.addUrlPatterns("/profiles/images/*");
        return frb;
    }
}
