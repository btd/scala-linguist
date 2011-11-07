import org.specs._

import main._


class LanguageSpec extends Specification {
	"fileNameIndex get language by filename" in {
		Language.fileNameIndex("CMakeLists.txt") must_== "CMake"
	}
	
	"ambiguous_? said true if file extension is more than one lang" in {
		Language.ambiguous_?(".h") must beTrue
	}
	
	"extensionIndex get language by extension" in {
		Language.extensionIndex(".rb") must_== "Ruby"
	}
	
	"extensionIndex doesnt contains ambiguous extensions" in {
		Language.extensionIndex.get(".h") must_== None
	}
}