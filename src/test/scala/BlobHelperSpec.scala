import org.specs._

import main._
import java.io.File

class BlobHelperSpec extends Specification {
	val / = File.separator



	def fixtures_path = (new File(getClass.getResource("/fixtures").toURI)).getAbsolutePath

	def blob(name:String) = new MockBlob(fixtures_path + / + name, Some(fixtures_path))

	"name extracted correctly" in {
		"foo.rb" must_== blob("foo.rb").name
	}

	"pathname extracted correctly" in {
		new Pathname("foo.rb") must_== blob("foo.rb").pathname
	}

	"data is correct" in {
		"module Foo\nend\n" must_== blob("foo.rb").data
	}
	"lines splitted" in {
		Seq("module Foo", "end", "") must_== blob("foo.rb").lines
	}
	"size of file is correct" in {
		15 must_== blob("foo.rb").size
	}
	"count of lines is correct" in {
		3 must_== blob("foo.rb").loc
	}
	"count of not empty lines is correct" in {
		2 must_== blob("foo.rb").sloc
	}

	"images" in {
		blob("octocat.gif").image_? must beTrue
		blob("octocat.jpeg").image_? must beTrue
		blob("octocat.jpg").image_? must beTrue
		blob("octocat.png").image_? must beTrue
		blob("octocat.ai").image_? must beFalse
		blob("octocat.psd").image_? must beFalse
	}
	"viewable_?" in {
		blob("README").viewable_? must beTrue
		blob("foo.rb").viewable_?  must beTrue
		blob("script.pl").viewable_?  must beTrue
		blob("linguist.gem").viewable_? must beTrue
		//blob("octocat.ai").viewable_? must beFalse I cannot undestand binary file or not =(
		blob("octocat.png").viewable_? must beFalse
	}

	"generated_?" in {
		blob("README").generated_? must beFalse
		blob("MainMenu.xib").generated_? must beTrue
		blob("MainMenu.nib").generated_? must beTrue
		blob("project.pbxproj").generated_? must beTrue

    	// Visual Studio Files
		blob("project.csproj").generated_? must beTrue
		blob("project.dbproj").generated_? must beTrue
		blob("project.isproj").generated_? must beTrue
		blob("project.pyproj").generated_? must beTrue
		blob("project.rbproj").generated_? must beTrue
		blob("project.vbproj").generated_? must beTrue
		blob("project.vdproj").generated_? must beTrue
		blob("project.vcxproj").generated_? must beTrue
		blob("project.wixproj").generated_? must beTrue
		blob("project.resx").generated_? must beTrue
		blob("project.sln").generated_? must beTrue

    	// Generated .NET Docfiles
		blob("net_docfile.xml").generated_? must beTrue

    	// Long line
		blob("uglify.js").generated_? must beFalse

    	// Inlined JS, but mostly code
		blob("json2_backbone.js").generated_? must beFalse

    	// Minified JS
		blob("jquery-1.6.1.js").generated_? must beFalse
		blob("jquery-1.6.1.min.js").generated_? must beTrue
		blob("jquery-1.4.2.min.js").generated_? must beTrue

    	// CoffeScript JS

    	// These examples are to basic to tell
		blob("coffee/empty.js").generated_? must beFalse
		blob("coffee/hello.js").generated_? must beFalse

		blob("coffee/intro.js").generated_? must beTrue
		blob("coffee/classes.js").generated_? must beTrue
	}

	"vendored_?" in {
		blob("README").vendored_? must beFalse

		// Node depedencies
		blob("node_modules/coffee-script/lib/coffee-script.js").vendored_? must beTrue

		// Rails vendor/
		blob("vendor/plugins/will_paginate/lib/will_paginate.rb").vendored_? must beTrue

		// C deps
		blob("deps/http_parser/http_parser.c").vendored_? must beTrue
		blob("deps/v8/src/v8.h").vendored_? must beTrue

		// Prototype
		blob("public/javascripts/application.js").vendored_? must beFalse
		blob("public/javascripts/prototype.js").vendored_? must beTrue
		blob("public/javascripts/effects.js").vendored_? must beTrue
		blob("public/javascripts/controls.js").vendored_? must beTrue
		blob("public/javascripts/dragdrop.js").vendored_? must beTrue

		// jQuery
		blob("jquery.js").vendored_? must beTrue
		blob("public/javascripts/jquery.js").vendored_? must beTrue
		blob("public/javascripts/jquery.min.js").vendored_? must beTrue
		blob("public/javascripts/jquery-1.5.2.js").vendored_? must beTrue
		blob("public/javascripts/jquery-1.6.1.js").vendored_? must beTrue
		blob("public/javascripts/jquery-1.6.1.min.js").vendored_? must beTrue
		blob("public/javascripts/jquery.github.menu.js").vendored_? must beFalse

		// MooTools
		blob("public/javascripts/mootools-core-1.3.2-full-compat.js").vendored_? must beTrue
		blob("public/javascripts/mootools-core-1.3.2-full-compat-yc.js").vendored_? must beTrue

		// Dojo
		blob("public/javascripts/dojo.js").vendored_? must beTrue

		// MochiKit
		blob("public/javascripts/MochiKit.js").vendored_? must beTrue

		// YUI
		blob("public/javascripts/yahoo-dom-event.js").vendored_? must beTrue
		blob("public/javascripts/yahoo-min.js").vendored_? must beTrue
		blob("public/javascripts/yuiloader-dom-event.js").vendored_? must beTrue

		// LESS
		blob("public/javascripts/less-1.1.0.js").vendored_? must beTrue
		blob("public/javascripts/less-1.1.0.min.js").vendored_? must beTrue

		// WYS editors
		blob("public/javascripts/ckeditor.js").vendored_? must beTrue
		blob("public/javascripts/tiny_mce.js").vendored_? must beTrue
		blob("public/javascripts/tiny_mce_popup.js").vendored_? must beTrue
		blob("public/javascripts/tiny_mce_src.js").vendored_? must beTrue

		// Fabric
		blob("fabfile.py").vendored_? must beTrue

		// WAF
		blob("waf").vendored_? must beTrue
	}

	"indexable_?" in {
		blob("file.txt").indexable_? must beTrue
		blob("foo.rb").indexable_? must beTrue
		blob("defun.kt").indexable_? must beFalse
		blob("dump.sql").indexable_? must beFalse
		blob("github.po").indexable_? must beFalse
		blob("linguist.gem").indexable_? must beFalse
	}

	"language recognized correctly" in {
		Language("C") must_==           blob("hello.c").language
		Language("C") must_==           blob("hello.h").language
		Language("C++") must_==         blob("bar.h").language
		Language("C++") must_==         blob("bar.hpp").language
		Language("C++") must_==         blob("hello.cpp").language
		Language("C++") must_==         blob("cuda.cu").language
		Language("GAS") must_==         blob("hello.s").language
		Language("Objective-C") must_== blob("Foo.h").language
		Language("Objective-C") must_== blob("Foo.m").language
		Language("Objective-C") must_== blob("FooAppDelegate.h").language
		Language("Objective-C") must_== blob("FooAppDelegate.m").language
		Language("Objective-C") must_== blob("hello.m").language
		Language("OpenCL") must_==      blob("fft.cl").language
		Language("Ruby") must_==        blob("foo.rb").language
		Language("Ruby") must_==        blob("script.rb").language
		Language("Ruby") must_==        blob("wrong_shebang.rb").language
		Language("Arduino") must_==     blob("hello.ino").language
		None must_== blob("octocat.png").language

		// .pl disambiguation
		Language("Prolog") must_==      blob("test-prolog.pl").language
		Language("Perl") must_==        blob("test-perl.pl").language
		Language("Perl") must_==        blob("test-perl2.pl").language

		// .m disambiguation
		Language("Objective-C") must_== blob("Foo.m").language
		Language("Objective-C") must_== blob("hello.m").language
		Language("Matlab") must_== blob("matlab_function.m").language
		Language("Matlab") must_== blob("matlab_script.m").language

		// .r disambiguation
		Language("R") must_==           blob("hello-r.R").language
		Language("Rebol") must_==       blob("hello-rebol.r").language

		// .t disambiguation
		Language("Perl") must_==        blob("perl-test.t").language
		Language("Turing") must_==      blob("turing.t").language

		// ML
		Language("OCaml") must_==       blob("Foo.ml").language
		Language("Standard ML") must_== blob("Foo.sig").language
		Language("Standard ML") must_== blob("Foo.sml").language

		// Config files
		Language("INI") must_==   blob(".gitconfig").language
		Language("Shell") must_== blob(".bash_profile").language
		Language("Shell") must_== blob(".bashrc").language
		Language("Shell") must_== blob(".profile").language
		Language("Shell") must_== blob(".zlogin").language
		Language("Shell") must_== blob(".zshrc").language
		Language("VimL") must_==  blob(".gvimrc").language
		Language("VimL") must_==  blob(".vimrc").language
		Language("YAML") must_==  blob(".gemrc").language

		None must_== blob("blank").language
		None must_== blob("README").language

		// https://github.com/xquery/xprocxq/blob/master/src/xquery/xproc.xqm
		Language("XQuery") must_== blob("xproc.xqm").language

		// https://github.com/wycats/osx-window-sizing/blob/master/center.applescript
		Language("AppleScript") must_== blob("center.scpt").language
		Language("AppleScript") must_== blob("center.applescript").language

		// https://github.com/Araq/Nimrod/tree/master/examples
		Language("Nimrod") must_== blob("foo.nim").language

		// http://supercollider.sourceforge.net/
		// https://github.com/drichert/BCR2000.sc/blob/master/BCR2000.sc
		Language("SuperCollider") must_== blob("BCR2000.sc").language

		// https://github.com/harrah/xsbt/wiki/Quick-Configuration-Examples
		Language("Scala") must_== blob("build.sbt").language

		// https://github.com/gradleware/oreilly-gradle-book-examples/blob/master/ant-antbuilder/build.gradle
		Language("Groovy") must_== blob("build.gradle").language

		// http://docs.racket-lang.org/scribble/
		Language("Racket") must_== blob("scribble.scrbl").language

		// https://github.com/drupal/drupal/blob/7.x/modules/php/php.module
		Language("PHP") must_== blob("drupal.module").language

		// https://github.com/googleapi/googleapi/blob/master/demos/gmail_demo/gmail.dpr
		Language("Delphi") must_== blob("program.dpr").language

		// https://github.com/philiplaureano/Nemerle.FizzBuzz/blob/master/FizzBuzz/FizzBuzzer.n
		Language("Nemerle") must_== blob("hello.n").language

		// https://github.com/dharmatech/agave/blob/master/demos/asteroids.sps
		Language("Scheme") must_== blob("asteroids.sps").language

		// https://github.com/graydon/rust
		Language("Rust") must_== blob("hello.rs").language

		// https://github.com/olabini/ioke
		Language("Ioke") must_== blob("hello.ik").language

		// https://github.com/parrot/parrot
		Language("Parrot Internal Representation") must_== blob("hello.pir").language
		Language("Parrot Assembly") must_== blob("hello.pasm").language

		// http://gosu-lang.org
		Language("Gosu") must_== blob("Hello.gsx").language
		Language("Gosu") must_== blob("hello.gsp").language
		Language("Gosu") must_== blob("Hello.gst").language
		Language("Gosu") must_== blob("hello.vark").language

		// Groovy Server Pages
		Language("Groovy Server Pages") must_== blob("bar.gsp").language
		Language("Groovy Server Pages") must_== blob("hello-resources.gsp").language
		Language("Groovy Server Pages") must_== blob("hello-pagedirective.gsp").language
		Language("Groovy Server Pages") must_== blob("hello-var.gsp").language

		// https://github.com/Lexikos/AutoHotkey_L
		Language("AutoHotkey") must_== blob("hello.ahk").language

		// Haml
		Language("Haml") must_== blob("hello.haml").language
		Some("HTML") must_== blob("hello.haml").language.get.group

		// Sass
		Language("Sass") must_== blob("screen.sass").language
		Some("CSS") must_== blob("screen.sass").language.get.group
		Language("SCSS") must_== blob("screen.scss").language
		Some("CSS") must_== blob("screen.scss").language.get.group
	}

	"shebang script recognized properly" in {
		"sh" must_== blob("script.sh").shebang_script
		"bash" must_== blob("script.bash").shebang_script
		"zsh" must_== blob("script.zsh").shebang_script
		"perl" must_== blob("script.pl").shebang_script
		"ruby" must_== blob("script.rb").shebang_script
		"ruby" must_== blob("script2.rb").shebang_script
		"python" must_== blob("script.py").shebang_script
		"node" must_== blob("script.js").shebang_script
		"groovy" must_== blob("script.groovy").shebang_script
		"macruby" must_== blob("script.mrb").shebang_script
		"rake" must_== blob("script.rake").shebang_script
		"foo" must_== blob("script.foo").shebang_script
		"nush" must_== blob("script.nu").shebang_script
		"scala" must_== blob("script.scala").shebang_script
		"racket" must_== blob("script.rkt").shebang_script
		"" must_== blob("foo.rb").shebang_script
	}

	"shebang script language recognized correctly" in {
		Language("Shell") must_== blob("script.sh").shebang_language
		Language("Shell") must_== blob("script.bash").shebang_language
		Language("Shell") must_== blob("script.zsh").shebang_language
		Language("Perl") must_== blob("script.pl").shebang_language
		Language("Ruby") must_== blob("script.rb").shebang_language
		Language("Python") must_== blob("script.py").shebang_language
		Language("JavaScript") must_== blob("script.js").shebang_language
		Language("Groovy") must_== blob("script.groovy").shebang_language
		Language("Ruby") must_== blob("script.mrb").shebang_language
		Language("Ruby") must_== blob("script.rake").shebang_language
		Language("Nu") must_== blob("script.nu").shebang_language
		Language("Scala") must_== blob("script.scala").shebang_language
		Language("Racket") must_== blob("script.rkt").shebang_language
		None must_== blob("script.foo").shebang_language
		None must_== blob("foo.rb").shebang_language
	}
}