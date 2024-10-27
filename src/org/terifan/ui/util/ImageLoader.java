package org.terifan.ui.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;


public class ImageLoader
{
	public static BufferedImage loadImage(Class aOwer, String aPath)
	{
		try
		{
			URL resource = aOwer.getResource(aPath);
			if (resource == null)
			{
				System.err.println("Resource missing: " + aPath);
				return null;
			}
			return ImageIO.read(resource);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}
}
