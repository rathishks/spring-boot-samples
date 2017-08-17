package life.rnl.migration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class InsertGenerator {
	public static void main(String[] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("src/main/resources/sourcedata/data-h2.sql");
		for (int i = 0; i < 50000; i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}

			fos.write(String.format(
					"INSERT INTO ASSET (ID, VERSION, SERIAL_NUMBER, DATE_CREATED) VALUES (%s, 0, '%s', CURRENT_TIMESTAMP());\n",
					i, UUID.randomUUID().toString().substring(0, 8)).getBytes());
		}
		fos.close();
	}
}