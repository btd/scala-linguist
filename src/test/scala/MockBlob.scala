
import main._
import java.io.File

class MockBlob(val path: String, val basePath: Option[String]) extends FileBlob with BlobHelper {
	private lazy val file = new File(path)

	lazy val name = basePath match {
		case None => path
		case Some(bp) => path.substring(bp.length + 1)
	}

	lazy val data:String = {
		val scanner = (new java.util.Scanner(file)).useDelimiter("""\z""")
		val builder = new scala.collection.mutable.StringBuilder
		while(scanner.hasNext) builder.append(scanner.next)
		builder.toString
	}
	
	lazy val size = file.length
}