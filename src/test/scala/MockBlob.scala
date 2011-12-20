
import main._
import java.io._

class MockBlob(val path: String, val basePath: Option[String]) extends FileBlob with BlobHelper {
	lazy val file = new File(path)

	lazy val name = basePath match {
		case None => path
		case Some(bp) => path.substring(bp.length + 1)
	}

	lazy val data = {
		dataFromBytes(fileToByteArray(file))
			
	}

	

	private def fileToByteArray(file: File) = {
	  val inStream = new FileInputStream(file)
	  val outStream = new ByteArrayOutputStream
	  try {
	    var reading = true
	    while ( reading ) {
	      inStream.read() match {
	        case -1 => reading = false
	        case c => outStream.write(c)
	      }
	    }
	    outStream.flush()
	  }
	  finally {
	    inStream.close()
	  }
	  outStream.toByteArray
	}
	
	lazy val size = file.length
}