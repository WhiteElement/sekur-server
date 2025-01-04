package org.braun.sekur.sekurserver.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ArgsExtractor {
    final private ApplicationArguments applicationArguments;
    
    public Optional<String> getArg(String arg) {
        if (!applicationArguments.containsOption(arg)) return Optional.empty();
        
        return applicationArguments.getOptionValues(arg).stream()
            .findFirst();
    }
}
