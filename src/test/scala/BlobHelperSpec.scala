import org.specs._

import main._
import java.io.File

class BlobHelperSpec extends Specification {
	val blobHelper1 = new BlobHelper("sub" + File.separator + "pasth" + File.separator + "dir.scala", Some("sub"))
	val blobHelper2 = new BlobHelper("sub" + File.separator + "pasth" + File.separator + "dir.scala", None)

	val blobHelperImage = new BlobHelper("dir.jpg", None)

	val blobHelperVendor = new BlobHelper("vendor/dir.jpg", None)

	"name of blobHelper is splitted if basePath setted" in {
		blobHelper1.name must_== "pasth" + File.separator + "dir.scala"
	}
	
	"name of blobHelper is not splitted if basePath setted" in {
		blobHelper2.name must_== blobHelper2.path
	}

	"Image are recognized by extension" in {
		blobHelperImage.image_? must beTrue
	}

	"vendored names" in {
		blobHelperVendor.vendored_?  must beTrue
		blobHelperImage.vendored_? must beFalse
	}
}