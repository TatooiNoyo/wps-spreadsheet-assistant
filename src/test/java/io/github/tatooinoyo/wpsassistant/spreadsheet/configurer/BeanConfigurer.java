package io.github.tatooinoyo.wpsassistant.spreadsheet.configurer;

import io.github.tatooinoyo.wpsassistant.spreadsheet.IStorageService;
import io.github.tatooinoyo.wpsassistant.spreadsheet.ImageHandler;
import io.github.tatooinoyo.wpsassistant.spreadsheet.services.StorageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Tatooi Noyo
 */
@Configuration
public class BeanConfigurer {

    @Bean
    public IStorageService storageService() {
        return new StorageServiceImpl();
    }

    @Bean
    public ImageHandler imageHandler(IStorageService storageService) {
        return new ImageHandler(storageService);
    }
}
