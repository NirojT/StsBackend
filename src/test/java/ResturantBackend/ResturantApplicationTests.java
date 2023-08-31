package ResturantBackend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResturantApplicationTests {

	@Test
	void contextLoads() {
	}

	Mathematics m=new Mathematics();
	
	@Test
	void doSUm() {
		
		int expectedResult=31;
		
		int result = m.doSum(10, 10, 10);
		
		assertThat(result).isEqualTo(expectedResult);
	}
	
}
