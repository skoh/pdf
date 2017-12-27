package org.oh;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Spring Boot Application
 * 
 * @author skoh
 */
@SpringBootApplication
@ImportResource("file:D:/dev/workspace/workspace_common/pdf/conf/spring-config.xml")
//@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class })
public class PdfApplication {
	protected static Log log = LogFactory.getLog(PdfApplication.class);

	/**
	 * 내장 서블릿 컨테이너를 커스터마이징한다.
	 * 
	 * @return EmbeddedServletContainerCustomizer
	 */
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer() {
//		ErrorMvcAutoConfiguration configuration = new ErrorMvcAutoConfiguration() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				String errorPath = "/WEB-INF/jsp/error.jsp";
//
//				container.addErrorPages(new ErrorPage(Throwable.class, errorPath));
//				container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, errorPath));
//				container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, errorPath));
//			}
//		};
//
//		return configuration;
//	}

	public static void main(String[] args) {
		SpringApplication.run(PdfApplication.class, args);
	}
}
