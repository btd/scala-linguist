package main

trait FileBlob {

	def path: String

	def basePath: Option[String]

	require(basePath match {
		case None => true
		case Some(bp) => path.startsWith(bp)
	}, "path doesn't started from base path")

	def dataFromBytes(bytes: Array[Byte]) = {
		try {
			val encoding = (new com.ibm.icu.text.CharsetDetector).setText(bytes).detect.getName

			new String(bytes, encoding)
		} catch {
			case _ => ""
		}
	}



	def name: String
	
	def data: String
		
	def size: Long
}