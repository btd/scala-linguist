import org.specs._

import main.Pathname
import java.io.File

class PathnameSpec extends Specification {
	val pathname = new Pathname("sub" + File.separator + "pasth" + File.separator + "dir.scala")

	"pathname should extract basename" in {
		pathname.basename must_== "dir.scala"
	}
	
	"pathname should extract extname" in {
		pathname.extname must_== ".scala"
	}
	

}