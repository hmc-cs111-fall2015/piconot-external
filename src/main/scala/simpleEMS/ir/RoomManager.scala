package simpleEMS.ir

/**
 * @author kevin
*/

import simpleEMS.ir.Schedule._
import simpleEMS.ir.Room._

class RoomManager {
  /*
   * Contains rooms and handles external API calls
   */
  
  val mRooms = scala.collection.mutable.HashMap.empty[String, Room.getClass]
  
  def makeNewRoom(roomName: String): Unit = {
    mRooms += (roomName -> Schedule)
  }
  
  def getSchedule (roomName: String, day: Int): Schedule = {
    mRooms.get(roomName).getScheduleForDay(day)
  }
    
  def getAvailableRooms (hour: Int, endHour: Int, date: String): Seq[Room] = { 
    val rooms = Array.empty[Room]
    for ((k, v) <- mRooms) {
      if(v.isFree(hour)){
        rooms :+ v
      }
    }
    return rooms
  }
  
}