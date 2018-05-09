package com.mudanza.utils.file;

import java.io.IOException;
import java.nio.file.Path;

public interface IFileSystem {
	
	boolean exists(Path path);

    boolean createDirectories(Path path) throws IOException ;
}
