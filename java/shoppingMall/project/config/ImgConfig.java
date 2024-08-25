package shoppingMall.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//사진을 서버폴더에서 불러오기 위한 url 환경설정파일
@Configuration
public class ImgConfig implements WebMvcConfigurer {
    @Value("${uploadPath}")
     String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations(uploadPath);
    }
}
