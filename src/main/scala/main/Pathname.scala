package main

class Pathname(val path: String) {
	val basename = path.substring(path.lastIndexOf(java.io.File.separator) + 1)
	
	val extname = {
		val index = basename.substring(1).lastIndexOf('.')
		if(index != -1) basename.substring(index + 1) else ""
	}
	
	val language: Option[Language] = Language.findByFilename(this)

	override def toString = path

	override def equals(that: Any) : Boolean = {
		that.isInstanceOf[Pathname] && (this.path == that.asInstanceOf[Pathname].path)
	}

	override def hashCode = path.hashCode
}