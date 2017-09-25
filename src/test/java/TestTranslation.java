import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

import service.TranslationService;

public class TestTranslation {

	@Test
	public void testTranslate() {
		TranslationService ts = new TranslationService();
		ts.init();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			sb.append(UUID.randomUUID() + " ");
		}
		String str = sb.toString();
		Assert.assertEquals(ts.translate(str), str);

	}

}
