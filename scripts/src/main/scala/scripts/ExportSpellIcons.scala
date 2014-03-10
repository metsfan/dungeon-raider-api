package scripts

import java.util.Properties
import java.sql.DriverManager
import javax.imageio.ImageIO
import java.net.URL
import java.io.FileInputStream

/**
 * Created by Adam on 3/9/14.
 */

object ExportSpellIcons {
  def main(args: Array[String]) {
    val props = new Properties()
    //props.load(new FileInputStream("src/main/resources/config.properties"))
    props.load(getClass().getResourceAsStream("config.properties"))

    val dbDriver = props.getProperty("db.default.driver")
    val dbUrl = props.getProperty("db.default.url")
    val dbUser = props.getProperty("db.default.user")
    val dbPass = props.getProperty("db.default.password")

    val configFile = "spell_icons.xml"
    val imageFile = "spell_icons.png"

    Class.forName(dbDriver)
    val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)

    val query = "SELECT id, icon_url FROM spell WHERE class_id > 0"

    val rs = conn.createStatement().executeQuery(query)

    val atlas = new ImageAtlas(1024, 1024)
    val baseUrl = "http://localhost:9002"

    while (rs.next()) {
      val imageUrl = rs.getString("icon_url")
      val image = ImageIO.read(new URL(baseUrl + imageUrl))
      atlas.addImage(image, imageUrl)
    }

    atlas.writeConfigToFile(configFile, imageFile)
    atlas.writeImageToFile(imageFile)
  }
}