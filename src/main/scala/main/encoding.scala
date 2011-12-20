package main


object StringEncoder {
	def encode(bytes: Array[Byte]) = {	
		try {
			val encoding = (new com.ibm.icu.text.CharsetDetector).setText(bytes).detect.getName

			new String(bytes, encoding)
		} catch {
			case _ => ""
		}
	}
}