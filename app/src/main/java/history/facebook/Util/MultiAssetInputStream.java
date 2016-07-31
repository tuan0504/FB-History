package history.facebook.Util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Input stream that reads from multiple assets, joining them together. This
 * allows assets to exceed the 1MB length limit.
 */
public class MultiAssetInputStream extends InputStream
{
	private AssetManager assets;
	private LinkedList<String> remainingFileNames;

	private InputStream current;

	public MultiAssetInputStream(AssetManager assets, String[] fileNames)
	{
		this.assets = assets;
		this.remainingFileNames = new LinkedList<String>(Arrays.asList(fileNames));
	}

	private void checkLoaded() throws IOException
	{
		if(current == null && !remainingFileNames.isEmpty())
		{
			String name = remainingFileNames.removeFirst();
			current = assets.open(name, AssetManager.ACCESS_STREAMING);
		}
	}

	protected void finalize() throws Throwable
	{
		close();
	}

	@Override
	public void close() throws IOException
	{
		if(current != null)
		{
			current.close();
		}
	}

	@Override
	public int read() throws IOException
	{
		checkLoaded();
		if(current == null)
		{
			return -1;
		}
		int value = current.read();
		if(value == -1)
		{
			// EOF, recurse with next stream
			current.close();
			current = null;
			return read();
		}
		else
		{
			return value;
		}
	}

	@Override
	public int read(byte[] b) throws IOException
	{
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int offset, int length) throws IOException
	{
		checkLoaded();
		if(current == null)
		{
			return -1;
		}
		int read = current.read(b, offset, length);
		if(read == -1)
		{
			// EOF, recurse with next stream
			current.close();
			current = null;
			return read(b, offset, length);
		}
		else
		{
			return read;
		}
	}
}
