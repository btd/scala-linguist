import org.specs._

import main.{Pathname, Language}
import java.io.File

class PathnameSpec extends Specification {
	val / = File.separator

	val filename = "sub" + / + "pasth" + / + "dir.scala"
	val pathname = new Pathname(filename)

	"toString equals path" in {
		pathname.toString must_== filename
	}

	"basename extracted correctlly" in {
		(new Pathname("file.rb")).basename must_== "file.rb"
		(new Pathname("." + / + "file.rb")).basename must_== "file.rb"
		(new Pathname("sub" + / + "dir" + / + "file.rb")).basename must_== "file.rb"
		(new Pathname(".profile")).basename must_== ".profile"
	}

	"extname extracted correctlly" in {
		(new Pathname("file.rb")).extname must_== ".rb"
		(new Pathname("." + / + "file.rb")).extname must_== ".rb"
		(new Pathname("sub" + / + "dir" + / + "file.rb")).extname must_== ".rb"
		(new Pathname(".profile")).extname must_== ""
	}

	"language recognized correctlly" in {
		Language("Ruby") must_== (new Pathname("file.rb")).language
		Language("Ruby") must_== (new Pathname("." + / + "file.rb")).language
		Language("Ruby") must_== (new Pathname("sub" + / + "dir" + / + "file.rb")).language

		Language("Ruby") must_== (new Pathname("Rakefile")).language
		Language("Ruby") must_== (new Pathname("vendor" + / + "Rakefile")).language
		Language("Ruby") must_== (new Pathname("." + / + "Rakefile")).language

		Language("Gentoo Ebuild") must_== (new Pathname("file.ebuild")).language
		Language("Python") must_== (new Pathname("itty.py")).language
		Language("Nu") must_== (new Pathname("itty.nu")).language

		(new Pathname("defun.kt")).language must_== None
	}  

}