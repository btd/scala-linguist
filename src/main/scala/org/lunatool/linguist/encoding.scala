package org.lunatool.linguist
import java.io._

trait Helper {
	val BUFFER_SIZE = 1024 * 2

	def inputStreamToByteArray(inStream: InputStream) = {
	  val outStream = new ByteArrayOutputStream
	  val buffer = new Array[Byte](BUFFER_SIZE)
	  var result: Option[Array[Byte]] = None
	  var finish = false
	  try {
	    while (!finish) 
	    	inStream.read(buffer) match {
	    		case -1 => finish = true
	    		case len => outStream.write(buffer, 0, len)
	    	}
	    outStream.flush
	    result = Some(outStream.toByteArray)
	  } catch {
	  	case _ => result = None
	  }
	   result
	}

	def guessString(bytes: Option[Array[Byte]]) = {
		bytes match {
			case None => None
			case Some(b) => {
				try {
					(new com.ibm.icu.text.CharsetDetector).setText(b).getString(b, null) match {
						case null => None
						case other => Some(other)
					}		
				} catch {
					case _ => None
				}
			}
		}
	}
}

object Helper extends Helper