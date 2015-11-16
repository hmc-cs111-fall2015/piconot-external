package simpleEMS.ir

/**
 * @author mjeong
 */

// Looking for a way to remove global val

import simpleEMS.ir.Occupant._

object Schedule {
  /**
   * The schedule for a day
   */
  val DEFAULT_NAME = "\n"
  val hourly = Array.fill[Occupant](24)(new Occupant);
  
  def setOccupant(hour: Int, endHour: Int, occ: Occupant): Unit = {
    for(i  <- hour to endHour) {
      hourly(i) = occ
    }
  }
  
  def isFree(hour: Int, endHour: Int): Boolean = {
    for(i <- hour to endHour) {
      if(hourly(i).getName() != DEFAULT_NAME) {
        return false
      }
    }
    true
  }
}