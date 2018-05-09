package com.mudanza.utils.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemImpl  implements IFileSystem {

	@Override
	public boolean exists(Path path) {
		return path.toFile().exists();
	}

	@Override
	public boolean createDirectories(Path path) throws IOException {
		
			Files.createDirectories(path);
			return true;
		
	}

}
