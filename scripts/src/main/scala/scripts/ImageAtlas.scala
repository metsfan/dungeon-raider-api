package scripts

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.collection.mutable.HashMap
import scala.xml._
import java.io.BufferedWriter
import java.io.FileWriter

case class ImageAtlasEntry(x: Integer, y: Integer, width: Integer, height: Integer)

class ImageAtlas(width: Integer, height: Integer) {
	
	var cursorX: Integer = 0;
	var cursorY: Integer = 0;
	var shelfHeight: Integer = 0;
	var atlasImage: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
	var atlasEntries: HashMap[String, ImageAtlasEntry] = new HashMap[String, ImageAtlasEntry]()
	var atlasFilepath = ""
	
	def addImage(img: BufferedImage, name: String)
	{
		val imgWidth = img.getWidth();
		val imgHeight = img.getHeight();
		
		if(imgWidth + cursorX > width) {
		  cursorX = 0;
		  cursorY += shelfHeight + 2;
		  shelfHeight = 0;
		}
		
		if(imgHeight > shelfHeight) {
			shelfHeight = imgHeight;
		}
		
		if(cursorY + shelfHeight > height) {
			throw new Exception("Atlas is full!");
		}

		atlasImage.getGraphics().drawImage(img, cursorX, cursorY, imgWidth, imgHeight, null);
		atlasEntries += ((name, new ImageAtlasEntry(cursorX, cursorY, imgWidth, imgHeight)));
		
		cursorX += imgWidth + 2;
	}
	
	def writeImageToFile(filepath: String) {
	  val file = new File(filepath);
	  ImageIO.write(atlasImage, "png", file);
	  atlasFilepath = filepath
	}
	
	def writeConfigToFile(filepath: String, imageFile: String, prettyPrint: Boolean = true) {
		val items = atlasEntries.map { case(name, entry) =>
      <Image name={name} x_coord={entry.x.toString} y_coord={entry.y.toString} width={entry.width.toString} height={entry.height.toString} />
		}
		
		val rootNode = <TextureAtlas width={width.toString} height={height.toString} filename={imageFile}>{items}</TextureAtlas>
		
		val outStream = new BufferedWriter(new FileWriter(filepath))
		
		if(prettyPrint) {
			val printer = new PrettyPrinter(500, 4)
			val xmlString = printer.format(rootNode)
			outStream.write(xmlString)
		} else {
		    XML.write(outStream, rootNode, "UTF-8", true, null)
		}
		outStream.close()
	}
}