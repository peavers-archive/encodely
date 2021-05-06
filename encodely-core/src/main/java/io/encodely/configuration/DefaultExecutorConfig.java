/* Licensed under Apache-2.0 */
package io.encodely.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultExecutorConfig {

  @Bean
  public DefaultExecutor defaultExecutor() {
    final DefaultExecutor defaultExecutor = new DefaultExecutor();
    defaultExecutor.setStreamHandler(new PumpStreamHandler(new SocatLogOutputStream()));

    return defaultExecutor;
  }

  /** Log stream which outputs each line via SLF4j. */
  @Slf4j
  static class SocatLogOutputStream extends LogOutputStream {

    @Override
    protected void processLine(final String line, final int level) {
      log.info(line);
    }
  }
}
