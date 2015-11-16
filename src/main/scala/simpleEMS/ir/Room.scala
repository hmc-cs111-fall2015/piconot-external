package simpleEMS.ir

/**
 * @author mjeong
*/
import simpleEMS.ir.Schedule._
import simpleEMS.ir.Weekday._

object Room  {
  val days = Array.fill[Schedule](7)(Schedule());

  def isFree(hour: Int, endHour: Int, day: Weekday): Boolean = {
    days.get(day).isFree(hour, endHour)
  }
  
  def getScheduleforDay(day: String, dayEnd: String): Schedule = {
    Schedule()
  }
}