package life.rnl.autowiring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryConfigurerTests {

	@Autowired
	ApplicationContext appContext;

	@Autowired
	RepositoryConfigurer repositoryConfigurer;

	@Test
	public void testRepositoriesConfigurer() {
		assertThat(repositoryConfigurer).isNotNull();

		Repositories repositories = repositoryConfigurer.getRepositories();
		assertThat(repositories).isNotNull();

		repositories.forEach(repository -> {
			System.out.println(repository.getName());
		});
	}
}