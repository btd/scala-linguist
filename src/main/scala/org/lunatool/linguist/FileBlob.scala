package org.lunatool.linguist

trait FileBlob {

	def path: String

	def basePath: Option[String]

	require(basePath match {
		case None => true
		case Some(bp) => path.startsWith(bp)
	}, "path doesn't started from base path")



	def name: String
	
	def data: Option[String]
		
	def size: Long
}