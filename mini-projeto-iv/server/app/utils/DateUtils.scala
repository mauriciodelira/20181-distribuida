package utils

object DateUtils {
	def nowInSqlDate = new java.sql.Date(new java.util.Date().getTime)
}
