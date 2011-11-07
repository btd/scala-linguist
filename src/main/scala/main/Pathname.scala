package main

class Pathname(val path: String) {
	val basename = path.substring(path.lastIndexOf(java.io.File.separator) + 1)
	
	val extname = path.substring(path.lastIndexOf("."))
	
	val language: Option[Language] = Language.findByFilename(this)
}