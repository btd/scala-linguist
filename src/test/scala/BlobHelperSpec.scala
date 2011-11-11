import org.specs._

import main._
import java.io.File

class BlobHelperSpec extends Specification {
	val / = File.separator

	

	def fixtures_path = (new File(getClass.getResource("/fixtures").toURI)).getAbsolutePath

	def blob(name:String) = new BlobHelper(fixtures_path + / + name, Some(fixtures_path))

	"name extracted correctly" in {
		"foo.rb" must_== blob("foo.rb").name
	}

	"pathname extracted correctly" in {
		new Pathname("foo.rb") must_== blob("foo.rb").pathname
	}

	"data is correct" in {
		"module Foo\nend\n" must_== blob("foo.rb").data
	}

	/*
	def test_data
    assert_equal "module Foo\nend\n", blob("foo.rb").data
  end

  def test_lines
    assert_equal ["module Foo", "end", ""], blob("foo.rb").lines
  end

  def test_size
    assert_equal 15, blob("foo.rb").size
  end

  def test_loc
    assert_equal 3, blob("foo.rb").loc
  end

  def test_sloc
    assert_equal 2, blob("foo.rb").sloc
  end
	*/
	
}