package main

trait BlobHelper {this: FileBlob =>
	
	def pathname = new Pathname(name)
	
	def extname = pathname.extname
	
	def image_? = List(".png", ".jpg", ".jpeg", ".gif").contains(extname)

	private val MEGABYTE = 1024 * 1024

	def large_? = size > MEGABYTE

	def viewable_? = !image_? && !large_?

	private val vendoredRegExp = List(
		"""cache/"""r,
		"""^deps/"""r,
		"""^tools/"""r,
		"""^node_modules/"""r,
		"""^vendor/"""r,
		"""(^|/)jquery([^.]*)(\.min)?\.js$"""r,
		"""(^|/)jquery\-\d\.\d\.\d(\.min)?\.js$"""r,
		"""(^|/)prototype(.*)\.js$"""r,
		"""(^|/)effects\.js$"""r,
		"""(^|/)controls\.js$"""r,
		"""(^|/)dragdrop\.js$"""r,
		"""(^|/)mootools([^.]*)\d+\.\d+.\d+([^.]*)\.js$"""r,
		"""(^|/)dojo\.js$"""r,
		"""(^|/)MochiKit\.js$"""r,
		"""(^|/)yahoo-([^.]*)\.js$"""r,
		"""(^|/)yui([^.]*)\.js$"""r,
		"""(^|/)less([^.]*)(\.min)?\.js$"""r,
		"""(^|/)less\-\d+\.\d+\.\d+(\.min)?\.js$"""r,
		"""(^|/)ckeditor\.js$"""r,
		"""(^|/)tiny_mce([^.]*)\.js$"""r,
		"""^fabfile\.py$"""r,
		"""^waf$"""r,
		"""(^|/)Sparkle/"""r)

	def vendored_? = vendoredRegExp.exists(_.findFirstIn(name) match {
		case None => false
		case _ => true
	})

	lazy val lines: List[String] = if(viewable_?) data.split("\n", -1).toList else Nil

	lazy val loc = lines.size

	lazy val sloc = lines.filter(s => !s.trim.isEmpty).size

	lazy val average_line_length = lines.foldLeft(0)((all, l) => all + l.length)/loc

	lazy val generated_? = xcode_project_file_? || visual_studio_project_file_? || generated_coffeescript_? || minified_javascript_? || generated_net_docfile_? 		

    private def xcode_project_file_? = List(".xib", ".nib", ".pbxproj", ".xcworkspacedata", ".xcuserstate").contains(extname)

    private def visual_studio_project_file_? = List(".csproj", ".dbproj", ".fsproj", ".pyproj", ".rbproj", ".vbproj", ".vcxproj", ".wixproj", ".resx", ".sln", ".vdproj", ".isproj").contains(extname)
    
	private def minified_javascript_? = extname == ".js" && average_line_length > 100

	 def generated_coffeescript_? = {
		extname == ".js" && lines(0) == "(function() {" && 
			lines(loc - 2) == "}).call(this);" && 
			lines(loc - 1) == "" &&			
			lines.foldLeft(0)((score, line) => if ("""var """.r.findAllIn(line).size == 0) 
					score + 
					("""_fn|_i|_len|_ref|_results"""r).findAllIn(line).size + 
					3 * ("""__bind|__extends|__hasProp|__indexOf|__slice"""r).findAllIn(line).size
				else 0
				) >= 3
	}

	private def generated_net_docfile_? = extname.toLowerCase == ".xml" && loc > 3 && lines(1).contains("<doc>") && lines(2).contains("<assembly>") && lines(loc - 2).contains("</doc>")

	private val guessers = List(
			".h" -> { () =>
				if (lines.exists(l => """^@(interface|property|private|public|end)""".r.findAllIn(l).size != 0)) 
					Language("Objective-C")
				else if (lines.exists(l => """^class |^\s+(public|protected|private):""".r.findAllIn(l).size != 0)) 
					Language("C++")
				else Language("C")
			},
			".m" -> { () =>
				if (lines.exists(l => """^#import|@(interface|implementation|property|synthesize|end)""".r.findAllIn(l).size != 0)) Language("Objective-C")
				else if ("^function ".r.findAllIn(lines.head).size != 0) Language("Matlab")
				else if (lines.exists(l => "^%".r.findAllIn(l).size != 0)) Language("Matlab")
				else Language("Objective-C")				
			},
			".pl" -> { () =>
				if (shebang_script == "perl") Language("Perl")
				else if (lines.exists(l => ":-".r.findAllIn(l).size != 0)) Language("Prolog")
				else Language("Perl")		
			},
			".r" -> { () =>
				if (lines.exists(l => """(rebol|(:\s+func|make\s+object!|^\s*context)\s*\[)""".r.findAllIn(l).size != 0)) Language("Rebol")
				else Language("R")
			},
			".gsp" -> { () =>
				if (lines.exists(l => """<%|<%@|\$\{|<%|<g:|<meta name="layout"|<r:""".r.findAllIn(l).size != 0)) Language("Groovy Server Pages")
				else Language("Gosu")				
			},
			".t" -> { () => {
				var score = 0
				score += (if(lines.exists(l => ("""^% """.r.findAllIn(l).size != 0))) 1 else 0)
				score += """ := """.r.findAllIn(data).size
				score += """proc |procedure |fcn |function """.r.findAllIn(data).size
				score += """var \w+: \w+""".r.findAllIn(data).size

				if(lines.exists(l => """^(my )?(sub |\$|@|%)\w+""".r.findAllIn(l).size != 0)) score = 0

				if (score >= 3) Language("Turing")
				else Language("Perl")
				}
			}
		).toMap

	private def disambiguate_extension_language = if (Language.ambiguous_?(extname)) guessers(extname)() else None
    
    
    lazy val language = guess_language

    private def guess_language: Option[Language] = disambiguate_extension_language orElse pathname.language orElse first_line_language orElse shebang_language

    private def first_line_language = 
    	if(viewable_? && """^<\?php""".r.findAllIn(lines.head).size != 0) Language("PHP")
    	else None

    def shebang_script: String = 
    	if(viewable_?) {
    		val shebang1 = """^#!\s*(/(\w+))+.*$""".r
			val shebang2 = """^#!\s*(/(\w+))+\s+(\w+).*$""".r
			val exec = """\s*exec\s+(\w+).+"\$0"\s+"\$@".*""".r

		
    		(lines.head match {
    			case shebang2(_, bin, arg) if (bin == "env") => arg
    			case shebang1(_, bin) if (bin == "sh")=> {
    				(lines.take(5).filter(l => l match {
    					case exec(s) => true
    					case _ => false
    				})) match {
    					case Seq(exec(executable), rest @ _*) => executable
    					case _ => "sh"
    				}
    			}
    			case shebang1(_, bin) => bin
    			case _ => ""
    		}).split("""\d+""")(0)
    	} else ""

  def shebang_language = Language(shebang_script.capitalize)


  def indexable_? =
      if (language == None)
        false
      else if (!language.get.searchable_?)
        false
      else if (generated_?)
        false
      else if (size > 100 * 1024)
        false
      else
        true


      
}