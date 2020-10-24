package me.jayoevans.storage.common;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.nio.file.Files;

public class FileUtil
{
	private static final int BUFFER = 1024;

	public static File zip(File sourceDirectory, File outputDirectory, String outputFile) throws IOException
	{
		File tarFile = new File(outputDirectory, outputFile + ".tar");
		File gzipFile = new File(outputDirectory, outputFile + ".tar.gz");

		try (TarArchiveOutputStream output = new TarArchiveOutputStream(new FileOutputStream(tarFile)))
		{
			Files.walk(sourceDirectory.toPath()).forEach(path ->
			{
				File file = path.toFile();

				if (file.isDirectory())
				{
					return;
				}

				TarArchiveEntry entry = new TarArchiveEntry(file, file.getPath());

				System.out.println("Zipping entry: " + entry.getFile());

				try (FileInputStream input = new FileInputStream(file))
				{
					output.putArchiveEntry(entry);
					IOUtils.copy(input, output);
					output.closeArchiveEntry();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			});
		}

		try (GzipCompressorOutputStream output = new GzipCompressorOutputStream(new FileOutputStream(gzipFile)))
		{
			output.write(Files.readAllBytes(tarFile.toPath()));
			output.finish();
		}

		tarFile.delete();

		return gzipFile;
	}

	public static void unzip(File sourceFile) throws IOException
	{
		unzip(sourceFile, null);
	}

	public static void unzip(File sourceFile, File outputDirectory) throws IOException
	{
		System.out.println("Unzip " + sourceFile + " to " + outputDirectory);

		String path = sourceFile.getPath();
		File tarFile = new File(path.substring(0, path.length() - 3));

		try (GzipCompressorInputStream input = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(sourceFile))))
		{
			try (FileOutputStream output = new FileOutputStream(tarFile))
			{
				IOUtils.copy(input, output);
			}
		}

		try (TarArchiveInputStream input = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(tarFile))))
		{
			TarArchiveEntry entry;

			while ((entry = input.getNextTarEntry()) != null)
			{
				File file = new File(outputDirectory, entry.getName());

				System.out.println("Unzipping entry: " + entry.getName());
				System.out.println("To file: " + file);

				File parent = file.getParentFile();
				Files.createDirectories(parent.toPath());

				try (FileOutputStream output = new FileOutputStream(file))
				{
					IOUtils.copy(input, output);
				}
			}
		}

		tarFile.delete();
	}
}
