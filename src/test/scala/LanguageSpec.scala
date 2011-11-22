import org.specs._

import main._


class LanguageSpec extends Specification {
	"Ambiguous extensions" in {
		Language.ambiguous_?(".h") must beTrue

		Language.ambiguous_?(".m") must beTrue

		Language.ambiguous_?(".pl") must beTrue

	    Language.ambiguous_?(".r") must beTrue

	    Language.ambiguous_?(".t") must beTrue
	}

	val pathName1 = new Pathname("dir"+ java.io.File.separator+"test.rb")
	val pathName2 = new Pathname("dir"+ java.io.File.separator+"Rakefile")

	"Finding by pathname" in {
		Language.findByFilename(pathName1) must_== Language("Ruby")
		Language.findByFilename(pathName2) must_== Language("Ruby")
	}
}