package life.rnl.migration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class InsertGenerator {
	public static void main(String[] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("src/main/resources/source/data-h2.sql");

		int partId = 1;
		fos.write(String.format(
				"INSERT INTO PART_TYPE (ID, VERSION, PART_NUMBER, DATE_CREATED) VALUES (%d, 0, '%s', CURRENT_TIMESTAMP);\n",
				partId, UUID.randomUUID().toString().replaceAll("-", "")).getBytes());

		for (int i = 0; i < 50000; i++) {
			if (i % 1000 == 0 && i > 0) {
				System.out.println(i);
				partId++;
				fos.write(String.format(
						"INSERT INTO PART_TYPE (ID, VERSION, PART_NUMBER, DATE_CREATED) VALUES (%d, 0, '%s', CURRENT_TIMESTAMP);\n",
						partId, UUID.randomUUID().toString().replaceAll("-", "")).getBytes());
			}

			fos.write(String.format(
					"INSERT INTO ASSET (VERSION, PART, SERIAL_NUMBER, DATE_CREATED, PROCESSED_IND) VALUES (0, %d, '%s', CURRENT_TIMESTAMP, 'UNREAD');\n",
					partId, UUID.randomUUID().toString().replaceAll("-", "")).getBytes());
		}
		fos.close();
	}
}