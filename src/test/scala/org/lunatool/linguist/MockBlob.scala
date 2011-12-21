package org.lunatool.linguist

import java.io._
import Helper._

class MockBlob(val path: String, val basePath: Option[String]) extends FileBlob with BlobHelper {
	lazy val file = new File(path)

	lazy val name = basePath match {
		case None => path
		case Some(bp) => path.substring(bp.length + 1)
	}

	lazy val data = {
		val inStream = new FileInputStream(file)
		val result = guessString(inputStreamToByteArray(inStream))

		inStream.close

		result
	}
	
	lazy val size = file.length
}