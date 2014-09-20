package com.aeskreis.dungeonraider.controllers

import play.api.mvc.Action
import play.api.Play
import java.util.UUID
import java.io.File
import play.api.libs.json.Json._
import play.api.Play.current

/**
 * Created by Adam on 3/2/14.
 */
object UploadController extends BaseController {

  def imageUpload = Action { implicit request =>
    var responseData = Map[String, String]()

    val formMultipartData = request.body.asMultipartFormData
    if (formMultipartData.isDefined) {
      val imageData = formMultipartData.get.files(0)
      val contentType = imageData.contentType.get

      if (isValidImageType(contentType)) {
        val directory = Play.application.configuration.getString("directory.upload.images").get
        val newFilename = UUID.randomUUID.toString
        val extension = extensionForImageType(contentType)
        val file = new File(directory + "\\" + newFilename + "." + extension)

        imageData.ref.moveTo(file, true)

        responseData = responseData ++ Seq("imageURL" -> ("/assets/upload/images/" + newFilename + "." + extension))
      }
    }

    Ok(toJson(responseData))
  }

  private def isValidImageType(contentType: String): Boolean =
  {
    if (contentType == "image/png" ||
      contentType == "image/jpeg" ||
      contentType == "image/gif")
      true
    else
      false
  }

  private def extensionForImageType(contentType: String): String =
  {
    if (contentType == "image/png")
      "png"
    else if (contentType == "image/jpeg")
      "jpeg"
    else if(contentType == "image/gif")
      "gif"
    else
      ""
  }
}
